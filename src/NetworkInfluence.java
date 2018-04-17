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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
//hi
public class NetworkInfluence
{
	/**
	 * String array alternating origin vertex followed by vertex it shares an edge with.
	 */
	private String[] edges;

	private int[][] adjMatrix;

	/**
	 * Array of LinkedLists. 1st element in each list is the 'origin' vertex, followed by its neighbors
	 */
	private LinkedList<String>[] adjList;

	/**
	 * ArrayList of all vertices in the web graph
	 */
	private ArrayList<String> vertices;

	private ArrayList<Vertex> vertexObjects;

	/**
	 * Number of vertices in the Web Graph
	 */
	private int numVert;

	// NOTE: graphData is an absolute file path that contains graph data, NOT the raw graph data itself
	public NetworkInfluence(String graphData) throws FileNotFoundException {
		File f = new File(graphData);
		Scanner s = new Scanner(f);
		numVert = s.nextInt();
		edges = new String[numVert * 2];
		int index = 0;
		while(s.hasNext()){
			edges[index] = s.next();
			index++;
		}
		initVertices();
		initAdjacencyList();
		initMatrix();
	}

	private void initVertices(){
		vertices = new ArrayList<String>();
		vertexObjects = new ArrayList<>();
		for(int i = 0; i < edges.length; i++){
			if(!vertices.contains(edges[i])){
				vertices.add(edges[i]);
			}
		}
		for(int i = 0; i < vertices.size(); i++){
		    vertexObjects.add(new Vertex(vertices.get(i)));
        }
        numVert = vertices.size();
	}

	private void initAdjacencyList(){
		adjList = new LinkedList[numVert];
		for(int i = 0; i < adjList.length; i++){
		    adjList[i] = new LinkedList<String>();
        }
		for(int i = 0; i < adjList.length; i++){
			adjList[i].add(vertices.get(i));
		}
		for(int i = 0; i < edges.length; i+=2){
			String origin = edges[i];
			String neighbor = edges[i+1];
			addPairToList(origin, neighbor);
		}
	}

	private void addPairToList(String origin, String neighbor){
		for(int i = 0; i < adjList.length; i++){
			if(adjList[i].get(0).equals(origin)){
				adjList[i].add(neighbor);
				break;
			}
		}
	}

	/**
	 * Initializes the adjacency matrix
	 */
	private void initMatrix(){
		adjMatrix = new int[numVert][numVert];
	}

	public int outDegree(String v)
	{
		for(int i = 0; i < adjList.length; i++){
			if(adjList[i].get(0).equals(v)){
				return adjList[i].size() - 1; // Subtract the 'Origin' vertex and only get neighbors
			}
		}
		return -1;
	}

	private void addNearbyElementsToQueue(Vertex v, Queue<Vertex> q){
	    String name = v.name;
	    for(int i = 0; i < adjList.length; i++){
	        if(adjList[i].get(0).equals(name)){
	            for(int x = 1; x < adjList[i].size(); x++){
	            	int index = getArraylistIndex(adjList[i].get(x));
	                if(vertexObjects.get(index).visited == false) {
                        vertexObjects.get(index).prev = v;
                        q.add(vertexObjects.get(index));
                    }
                }
            }
        }
    }

    private int getArraylistIndex(String name){
		for(int i = 0; i < vertexObjects.size(); i++){
			if(vertexObjects.get(i).name.equals(name))
				return i;
		}
		return -1;
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
        Queue<Vertex> queue = new LinkedList<Vertex>();
	    if(vertices.contains(u)){
	        int index = getArraylistIndex(u);
	        vertexObjects.get(index).visited = true;
	     //objectPath.add(vertexObjects.get(index));
            queue.add(vertexObjects.get(index));
            Vertex cur = null;
            while(queue.peek() != null){
                cur = queue.poll();
                if(cur.name.equals(v)){
                    break;
                }
                cur.visited = true;
                addNearbyElementsToQueue(cur, queue);
            }
            while(cur != null){
                path.add(0, cur.name);
                cur = cur.prev;
            }
            return path;
        }
        else{
	        return path;
        }
	}

	public int distance(String u, String v)
	{
		return shortestPath(u,v).size();
	}

	public int distance(ArrayList<String> s, String v)
	{
		int max = 0;
		for(String u : s){
			int dist = distance(u, v);
			if(dist > max){
				max = dist;
			}
		}
		return max;
	}

	private int getSetSizeOfSameDistVert(String u, int dist){
		if(dist == 0)
			return 0;
		else{
			int count = 0;
			for(Vertex vert : vertexObjects){
				if(distance(u, vert.name) == dist)
					count++;
			}
			return count;
		}
	}

	private int getSetSizeOfSameDistVert(ArrayList<String> list, int dist){
		if(dist == 0)
			return 0;
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
		for(int i = 0; i < numVert; i++){
			inf += (1 / (Math.pow(2, i))) * getSetSizeOfSameDistVert(u, i);
		}
		return inf;
	}

	public float influence(ArrayList<String> s)
	{
		float inf = 0;
		for(int i = 0; i < numVert; i++){
			inf += (1 / (Math.pow(2, i))) * getSetSizeOfSameDistVert(s, i);
		}
		return inf;
	}

	public ArrayList<String> mostInfluentialDegree(int k)
	{
//		ArrayList<Vertex> topDegreeNodes = new ArrayList<Vertex>(k);
//		for(int i = 0; i < vertexObjects.size(); i++)
//		{
//			Vertex vertex = vertexObjects.get(i);
//			int newOutDegree = outDegree(vertex.name);
//			int oldOutDegree = 0;
//			int j = 0;
//			if(!(topDegreeNodes.isEmpty()))
//			{
//				oldOutDegree = outDegree(topDegreeNodes.get(0).name);
//			}
//			
//			if(i < k)
//			{
//				//Add first Element to array
//				if(topDegreeNodes.isEmpty())
//				{
//					topDegreeNodes.add(vertex);
//				}
//				//See if second Element is greater or less than the first and put after/before respectively
//				else if(topDegreeNodes.size() == 1)
//				{
//					if(newOutDegree > outDegree(topDegreeNodes.get(0).name))
//					{
//						topDegreeNodes.add(vertex);
//					}
//					else
//					{
//						topDegreeNodes.add(0, vertex);
//					}
//				}
//				//puts remaining elements in the arraylist in order of least to greatest outDegrees
//				else
//				{
//				
//					
//				}
//				
//			}
//			else
//			{
//				
//				if(newOutDegree > oldOutDegree)
//				{
//					
//					while(j < topDegreeNodes.size() && newOutDegree > outDegree(topDegreeNodes.get(j).name))
//					{
//						j++;
//					}
//					if(j == topDegreeNodes.size())
//					{
//						topDegreeNodes.add(vertex);
//					}
//					else
//					{
//						topDegreeNodes.add(j, vertex);
//					}
//					topDegreeNodes.remove(0);
//				}
//			}
//		}
		
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
		ArrayList<String> topInfluenceNodes = new ArrayList<String>();
		topInfluenceNodes.add(vertexObjects.get(0).name);
		String vertex1Name = vertexObjects.get(1).name;
		
		if(influence(vertex1Name) > influence(topInfluenceNodes.get(0)))
		{
			topInfluenceNodes.add(0, vertex1Name);
		}
		else
		{
			topInfluenceNodes.add(vertex1Name);
		}
		
		
		for(int i = 2; i < vertexObjects.size(); i++)
		{
			int j = 0;
			String vertex = vertexObjects.get(i).name;
			float newDegree = influence(vertex);
			float oldDegree = influence(topInfluenceNodes.get(j));
			
			if(newDegree > influence(topInfluenceNodes.get(0)))
			{
				topInfluenceNodes.add(0, vertexObjects.get(i).name);
			}
			else
			{
				while(j < topInfluenceNodes.size() && newDegree < oldDegree)
				{
					j++;
				}
				if(j == topInfluenceNodes.size())
				{
					topInfluenceNodes.add(vertex);
				}
				else
				{
					topInfluenceNodes.add(j, vertex);
				}	
			}	
		}
		
		topInfluenceNodes.subList(k, topInfluenceNodes.size()).clear();
		return topInfluenceNodes;
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
		
		return null;
	}
	
	public static void main(String[] args) {
		ArrayList<String> a = new ArrayList<String>();
		a.add("okay");
		a.add("k");
		a.add("bb");
		a.add("ok");
		a.add("okayyyyyyy");
		a.add("nerd");
		a.add("wut");
		
		a.subList(3, a.size()).clear();
		for(int i = 0; i < a.size(); i++)
		{
			System.out.println(a.get(i));
		}
	}
}