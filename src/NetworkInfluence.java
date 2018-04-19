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
	public ArrayList<String> edges;

	/**
	 * Array of LinkedLists. 1st element in each list is the 'origin' vertex, followed by its neighbors
	 */
	public LinkedList<Vertex>[] adjList;

	/**
	 * ArrayList of all vertices in the web graph
	 */
	public ArrayList<String> vertices;

	/**
	 * ArrayList of all vertices in graph represented as VERTEX Objects
	 */
	public HashMap<Vertex, LinkedList<Vertex>> vertexObjects;

	/**
	 * Number of vertices in the Web Graph
	 */
	public int numVert;

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
	}

	private void initVertices(){
		vertices = new ArrayList<String>();
		vertexObjects = new HashMap<>();
		for(int i = 0; i < edges.size(); i++){
			if(!vertices.contains(edges.get(i))){
				vertices.add(edges.get(i));
			}
		}
		for(int i = 0; i < vertices.size(); i++){
		    vertexObjects.add(new Vertex(vertices.get(i)));
        }
        numVert = vertices.size();
	}

	private void initAdjacencyList(){
		adjList = new LinkedList[numVert];
		for(int i = 0; i < adjList.length; i++){ //Init Linked Lists
		    adjList[i] = new LinkedList<Vertex>();
        }
		for(int i = 0; i < adjList.length; i++){ //Add First element to each list
			adjList[i].add(vertexObjects.get(i));
		}
		for(int i = 0; i < edges.size(); i+=2){
			String origin = edges.get(i);
			String neighbor = edges.get(i+1);
			addPairToList(origin, neighbor);
		}
	}

	private void addPairToList(String origin, String neighbor){
		for(int i = 0; i < adjList.length; i++){
			if(adjList[i].get(0).name.equals(origin)){
				int index = getVertexIndex(neighbor); //get index of vertex object
				adjList[i].add(vertexObjects.get(index));
				break;
			}
		}
	}



	public int outDegree(String v)
	{
		for(int i = 0; i < adjList.length; i++){
			if(adjList[i].get(0).name.equals(v)){
				return adjList[i].size() - 1; // Subtract the 'Origin' vertex and only get neighbors
			}
		}
		return -1;
	}

	private Vertex addNearbyElementsToQueue(Vertex v, Queue<Vertex> q, String end){
	    String name = v.name;
	    for(int i = 0; i < adjList.length; i++){ //Find vertex in adjacency list
	        if(adjList[i].get(0).name.equals(name)){
	            for(int x = 1; x < adjList[i].size(); x++){ //Go through all neighbors of origin vertex
					Vertex neighbor = adjList[i].get(x);
	                if(neighbor.visited == false) {
						neighbor.prev = v;
                        q.add(neighbor);
                        if(neighbor.name.equals(end))
                        	return neighbor;
                    }
                }
            }
        }
        return null;
    }

    private int getVertexIndex(String name){
		for(int i = 0; i < vertexObjects.size(); i++){
			if(vertexObjects.get(i).name.equals(name))
				return i;
		}
		return -1;
	}

	private void resetVisitedValues(){
    	for(int i = 0; i < vertexObjects.size(); i++){
    		vertexObjects.get(i).visited = false;
    		vertexObjects.get(i).prev = null;
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
			Queue<Vertex> queue = new LinkedList<Vertex>();
	        int index = getVertexIndex(u);
            queue.add(vertexObjects.get(index));
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
		return min;
	}

	private float getSetSizeOfSameDistVert(ArrayList<String> list, int dist){
		if(dist == 0)
			return list.size();
		else{
			int count = 0;
			for(Vertex vert : vertexObjects){
				if(distance(list, vert.name) == dist)
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
		topDegreeNodes.add(vertexObjects.get(0).name);
		String vertex1Name = vertexObjects.get(1).name;
		
		if(outDegree(vertex1Name) > outDegree(topDegreeNodes.get(0)))
		{
			topDegreeNodes.add(0, vertex1Name);
		}
		else
		{
			topDegreeNodes.add(vertex1Name);
		}
		
		
		for(int i = 2; i < vertexObjects.size(); i++)
		{
			int j = 0;
			String vertex = vertexObjects.get(i).name;
			int newDegree = outDegree(vertex);
			int oldDegree = outDegree(topDegreeNodes.get(j));
			
			if(newDegree > outDegree(topDegreeNodes.get(0)))
			{
				topDegreeNodes.add(0, vertexObjects.get(i).name);
			}
			else
			{
				while(j < topDegreeNodes.size() && newDegree < oldDegree)
				{
					j++;
				}
				if(j == topDegreeNodes.size())
				{
					topDegreeNodes.add(vertex);
				}
				else
				{
					topDegreeNodes.add(j, vertex);
				}	
			}	
		}
		
		topDegreeNodes.subList(k, topDegreeNodes.size()).clear();
		return topDegreeNodes;
	}
	

	public ArrayList<String> mostInfluentialModular(int k)
	{
		ArrayList<String> topDegreeNodes = new ArrayList<String>();
		topDegreeNodes.add(vertexObjects.get(0).name);
		String vertex1Name = vertexObjects.get(1).name;
		
		if(influence(vertex1Name) > influence(topDegreeNodes.get(0)))
		{
			topDegreeNodes.add(0, vertex1Name);
		}
		else
		{
			topDegreeNodes.add(vertex1Name);
		}
		
		
		for(int i = 2; i < vertexObjects.size(); i++)
		{
			int j = 0;
			String vertex = vertexObjects.get(i).name;
			float newDegree = influence(vertex);
			float oldDegree = influence(topDegreeNodes.get(j));
			
			if(newDegree > influence(topDegreeNodes.get(0)))
			{
				topDegreeNodes.add(0, vertexObjects.get(i).name);
			}
			else
			{
				while(j < topDegreeNodes.size() && newDegree < oldDegree)
				{
					j++;
				}
				if(j == topDegreeNodes.size())
				{
					topDegreeNodes.add(vertex);
				}
				else
				{
					topDegreeNodes.add(j, vertex);
				}	
			}	
		}
		
		topDegreeNodes.subList(k, topDegreeNodes.size()).clear();
		return topDegreeNodes;
	}

	public ArrayList<String> mostInfluentialSubModular(int k)
	{
		ArrayList<String> topInfluenceSet = new ArrayList<String>();
		ArrayList<Vertex> nodes = vertexObjects;

		for(int i = 0; i < k; i++)
		{
			String vertexV = nodes.get(i).name;
			int count = 0;
			ArrayList<String> verticies1 = topInfluenceSet;
			verticies1.add(vertexV);

			for(int j = 0; j < nodes.size(); i++)
			{
				String vertexU = nodes.get(j).name;

				ArrayList<String> verticies2 = topInfluenceSet;
				verticies2.add(vertexU);
				float infV = influence(verticies1);
				float infU = influence(verticies2);

				if(infV >= infU)
				{
					count++;
				}
			}
			if(count == nodes.size())
			{
				topInfluenceSet.add(vertexV);
				nodes.remove(i);
			}
		}

		return topInfluenceSet;
	}
}