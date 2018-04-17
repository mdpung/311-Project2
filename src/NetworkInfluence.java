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
import java.util.Scanner;

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
		vertices = new ArrayList<>();
		for(int i = 0; i < edges.length; i++){
			if(!vertices.contains(edges[i])){
				vertices.add(edges[i]);
			}
		}
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

	public ArrayList<String> shortestPath(String u, String v)
	{
		return null;
	}

	public int distance(String u, String v)
	{
		return -1;
	}

	public int distance(ArrayList<String> s, String v)
	{
		return -1;
	}

	public float influence(String u)
	{
		for(int i = 0; i < )
		return -1f;
	}

	public float influence(ArrayList<String> s)
	{
		return -1f;
	}

	public ArrayList<String> mostInfluentialDegree(int k)
	{
		return null;
	}

	public ArrayList<String> mostInfluentialModular(int k)
	{
		return null;
	}

	public ArrayList<String> mostInfluentialSubModular(int k)
	{
		return null;
	}
}