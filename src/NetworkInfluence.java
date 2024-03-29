// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add member fields and additional methods)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may only include libraries of the form java.*)

/**
* @author Hugh Potter
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

//hi
public class NetworkInfluence
{
	/**
	 * String array alternating origin vertex followed by vertex it shares an edge with.
	 */
	private ArrayList<String> edges;
	
	private Comparator<String> c = new Comparator<String>(){

		@Override
		public int compare(String arg0, String arg1) {
			
			if(influence(arg0) > influence(arg1))
			{
				return -1;
			}
			else if(influence(arg0) == influence(arg1))
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
		
	};

	/**
	 * ArrayList of all vertices in the web graph
	 */
	private ArrayList<String> vertices;

	private HashMap<String, LinkedList<Vertex>> adjList;

	private HashMap<String, Vertex> vertexObjects;

	private ArrayList<String> lastElt;

	private boolean wasList;

	private ArrayList<Integer> distanceValues;

	/**
	 * Number of vertices in the Web Graph
	 */
	private int numVert;

	// NOTE: graphData is an absolute file path that contains graph data, NOT the raw graph data itself
	public NetworkInfluence(String graphData) throws FileNotFoundException {
		File f = new File(graphData);
		Scanner s = new Scanner(f);
		numVert = s.nextInt();
		edges = new ArrayList<String>();
		while(s.hasNext()){
			edges.add(s.next());
		}
		initVertices();
		initAdjacencyList();
		distanceValues = new ArrayList<Integer>();
	}

	private void initVertices(){
		vertices = new ArrayList<String>();
		vertexObjects = new HashMap<String, Vertex>();
		for(int i = 0; i < edges.size(); i++){
			if(!vertices.contains(edges.get(i))){
				vertices.add(edges.get(i));
			}
		}
		for(int i = 0; i < vertices.size(); i++){
			vertexObjects.put(vertices.get(i),new Vertex(vertices.get(i)));
        }
        numVert = vertices.size();
	}

	private void initAdjacencyList(){
		adjList = new HashMap<String, LinkedList<Vertex>>();
		for(int i = 0; i < vertices.size(); i++){ //Init Linked Lists
			adjList.put(vertices.get(i) , new LinkedList<Vertex>());
        }
		for(int i = 0; i < vertices.size(); i++){ //Add First element to each list
			LinkedList<Vertex> list = adjList.get(vertices.get(i));
			list.add(vertexObjects.get(vertices.get(i))); //hashmap will return the Vertex Object by using the name of the Vertex
		}
		for(int i = 0; i < edges.size(); i+=2){
			String origin = edges.get(i);
			String neighbor = edges.get(i+1);
			addPairToList(origin, neighbor);
		}
	}

	//Adds neighbors to adjacency list
	private void addPairToList(String origin, String neighbor){
		LinkedList<Vertex> list = adjList.get(origin);  //get list based on origin vertex
		list.add(vertexObjects.get(neighbor));
	}



	public int outDegree(String v)
	{
		LinkedList<Vertex> list = adjList.get(v);
		return list.size() - 1;
	}

	private Vertex addNearbyElementsToQueue(Vertex v, Queue<Vertex> q, String end){
	    String name = v.name;
	    LinkedList<Vertex> list = adjList.get(name);
		for(int x = 1; x < list.size(); x++){ //Go through all neighbors of origin vertex
			Vertex neighbor = list.get(x);
			if(neighbor.visited == false) {
				neighbor.prev = v;
				q.add(neighbor);
				if(neighbor.name.equals(end))
					return neighbor;
			}
		}
		return null;
    }

	private void resetVisitedValues(){
    	for(int i = 0; i < vertices.size(); i++){
    		vertexObjects.get(vertices.get(i)).visited = false;
    		vertexObjects.get(vertices.get(i)).prev = null;
		}
	}

    /**
     * Returns a BFS path from u to v. First vertex is u and last is v. If no path, return empty list
     * @param u
     * @param v
     * @return ArrayList of Strings.
     */
	public ArrayList<String> shortestPath(String u, String v)
	{
	    ArrayList<String> path = new ArrayList<String>();
	    if(vertices.contains(u) && vertices.contains(v)){
	    	if(u.equals(v)){
	    		path.add(u); path.add(u);
	    		return path;
			}
			Queue<Vertex> queue = new LinkedList<Vertex>();
            queue.add(vertexObjects.get(u));
            Vertex cur = null;
            while(queue.peek() != null){
                cur = queue.poll();
                cur.visited = true;
                cur = addNearbyElementsToQueue(cur, queue, v); //if end node found, will return a vertex, null if not found
                if(cur != null)
                	break;
            }
            while(cur != null){
                path.add(0, cur.name);
                cur = cur.prev;
            }
            resetVisitedValues();
            return path;
        }
        else{
	        return path;
        }
	}

	public int distance(String u, String v)
	{
		if(u.equals(v))
			return 0;
		int pathLength = shortestPath(u,v).size();
		return 	pathLength - 1;
	}

	public int distance(ArrayList<String> s, String v)
	{
		int min = 99999;
		for(String u : s){
			int dist = distance(u, v);
			if(dist == -1)
				continue;
			if(dist < min){
				min = dist;
			}
		}
		if(min == 99999)
			return -1;
		return min;
	}

	private void saveDistances(ArrayList<String> list, int dist){ //O(n)
		if(lastElt == null || !list.equals(lastElt)){
			lastElt = (ArrayList<String>) list.clone();
			distanceValues.clear();
			for(int i = 0; i < vertices.size(); i++){
				Vertex v = vertexObjects.get(vertices.get(i));
				distanceValues.add(distance(list, v.name));
			}
		}
	}

	private float getSetSizeOfSameDistVert(ArrayList<String> list, int dist){
		saveDistances(list, dist); //O (n) but will only run once or one call to influence, O(n^2 if list has more than 1 elt
		if(dist == 0)
			return list.size();
		else{
			int count = 0;
			for(int i = 0; i < vertices.size(); i++){ //O(n)
				if(distanceValues.get(i) == dist)
					count++;
			}
			return count;
		}
	}

	public float influence(String u)
	{
		float inf = 0;
		ArrayList<String> oneElt = new ArrayList<String>(); oneElt.add(u);
		for(int i = 0; i < numVert; i++){
			inf += (1.f / (Math.pow(2, i))) * getSetSizeOfSameDistVert(oneElt, i);
		}
		return inf;
	}

	public float influence(ArrayList<String> s)
	{
		float inf = 0;
		for(int i = 0; i < numVert; i++){
			inf += (1 / ((Math.pow(2, i))) * getSetSizeOfSameDistVert(s, i));
		}
		return inf;
	}

	public ArrayList<String> mostInfluentialDegree(int k)
	{

		//can it be done this way???
		ArrayList<String> topDegreeNodes = new ArrayList<String>();
		topDegreeNodes.add(vertices.get(0));
		String vertex1Name = vertices.get(1);
		
		if(outDegree(vertex1Name) > outDegree(topDegreeNodes.get(0)))
		{
			topDegreeNodes.add(0, vertex1Name);
		}
		else
		{
			topDegreeNodes.add(vertex1Name);
		}
		
		
		for(int i = 2; i < vertices.size(); i++)
		{
			int j = 0;
			String vertex = vertices.get(i);
			int newDegree = outDegree(vertex);
			int oldDegree = outDegree(topDegreeNodes.get(j));
			
			if(newDegree > outDegree(topDegreeNodes.get(0)))
			{
				topDegreeNodes.add(0, vertices.get(i));
			}
			else
			{
				topDegreeNodes.add(vertex);
				
				topDegreeNodes.sort(c);
			}	
		}
		
		topDegreeNodes.subList(k, topDegreeNodes.size()).clear();
		return topDegreeNodes;
	}
	

	public ArrayList<String> mostInfluentialModular(int k)
	{

		ArrayList<String> topDegreeNodes = new ArrayList<String>();
		
		topDegreeNodes.add(vertices.get(0));
		String vertex1Name = vertices.get(1);
		
		if(influence(vertex1Name) > influence(topDegreeNodes.get(0)))
		{
			topDegreeNodes.add(0, vertex1Name);
		}
		else
		{
			topDegreeNodes.add(vertex1Name);
		}
		
		
		for(int i = 2; i < vertices.size(); i++)
		{
			int j = 0;
			String vertex = vertices.get(i);
			float newDegree = influence(vertex);
			float oldDegree = influence(topDegreeNodes.get(j));
			
			if(newDegree > influence(topDegreeNodes.get(0)))
			{
				topDegreeNodes.add(0, vertices.get(i));
			}
			else
			{
				topDegreeNodes.add(vertex);
				
				topDegreeNodes.sort(c);
			}	
		}
		
		topDegreeNodes.subList(k, topDegreeNodes.size()).clear();
		return topDegreeNodes;
	}

	public ArrayList<String> mostInfluentialSubModular(int k)
	{
		ArrayList<String> topInfluenceSet = new ArrayList<String>();
		ArrayList<String> nodes = new ArrayList<String>();
		nodes.addAll(vertices);

		for(int i = 0; i < k; i++)
		{
			String vertexV = nodes.get(0);
			int count = 0;
			ArrayList<String> vertices1 = new ArrayList<String>();
			vertices1.addAll(topInfluenceSet);
			vertices1.add(vertexV);

			for(int j = 1; j < nodes.size(); j++)
			{
				String vertexU = nodes.get(j);

				ArrayList<String> vertices2 = new ArrayList<String>();
				vertices2.addAll(topInfluenceSet);
				vertices2.add(vertexU);
				float infV = influence(vertices1);
				float infU = influence(vertices2);

				if(infV >= infU)
				{
					count++;
				}
			}
			if(count == nodes.size() - 1)
			{
				topInfluenceSet.add(vertexV);
				
			}
			nodes.remove(0);
		}

		return topInfluenceSet;
	}
}