import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class NetworkInfluenceTest2 {

	NetworkInfluence ni = new NetworkInfluence("C://Users//Justin//Desktop//311-Project2//src//inftest.txt");


	public NetworkInfluenceTest2() throws FileNotFoundException {
    }
	
	@Test
	public void testOutDegree() {		
		int outDegree_A = ni.outDegree("A");
		int outDegree_G = ni.outDegree("G");
		int outDegree_H = ni.outDegree("H");
		Assert.assertEquals(outDegree_A,3);
		Assert.assertEquals(outDegree_G,0);
		Assert.assertEquals(outDegree_H,1);	
	}

	@Test
	public void testShortestPath() {
		ArrayList<String> s1 = ni.shortestPath("A","A");
		Assert.assertEquals(s1,new ArrayList<String>(Arrays.asList(new String[] {"A","A"})));
		ArrayList<String> s2 = ni.shortestPath("A","E");
		Assert.assertEquals(s2,new ArrayList<String>(Arrays.asList(new String[] {"A","B","E"})));
		ArrayList<String> s3 = ni.shortestPath("A","H");
		Assert.assertEquals(s3,new ArrayList<String>(Arrays.asList(new String[] {"A","D","H"})));
		ArrayList<String> s4 = ni.shortestPath("D","F");
		Assert.assertEquals(s4,new ArrayList<String>(Arrays.asList(new String[] {"D","H","F"})));
		ArrayList<String> s5 = ni.shortestPath("X","Y");//vertices are not exist
		Assert.assertEquals(s5,new ArrayList<String>(Arrays.asList(new String[] {})));		
	}

	@Test
	public void testDistanceStringString() {
		int d1 = ni.distance("A", "A");
		Assert.assertEquals(d1,0);
		int d2 = ni.distance("A", "E");
		Assert.assertEquals(d2,2);
		int d3 = ni.distance("A", "H");
		Assert.assertEquals(d3,2);
		int d4 = ni.distance("D", "F");
		Assert.assertEquals(d4,2);
		int d5 = ni.distance("X", "Y");//no path
		Assert.assertEquals(d5,-1);
		int d6 = ni.distance("D", "E");//no path 
		Assert.assertEquals(d6,-1);
	}

	@Test
	public void testDistanceArrayListOfStringString() {
		ArrayList<String> arr = new ArrayList<>(Arrays.asList(new String[] {"A","H"}));
		int d1 = ni.distance(arr, "A");
		Assert.assertEquals(d1,0);
		int d2 = ni.distance(arr, "E");
		Assert.assertEquals(d2,2);
		int d3 = ni.distance(arr, "F");
		Assert.assertEquals(d3,1);
		int d4 = ni.distance(arr, "C");
		Assert.assertEquals(d4,1);
		ArrayList<String> arr2 = new ArrayList<>(Arrays.asList(new String[] {"G","H"}));
		int d5 = ni.distance(arr2, "A");//no path
		Assert.assertEquals(d5,-1);

	}

	@Test
	public void testInfluenceString() {
		float inf1 = ni.influence("A");
		Assert.assertEquals(inf1,3.5,0.0f);
		float inf2 = ni.influence("B");
		Assert.assertEquals(inf2,3.0625,0.0f);
		float inf3 = ni.influence("C");
		Assert.assertEquals(inf3,1.5,0.0f);
		float inf4 = ni.influence("D");
		Assert.assertEquals(inf4,1.75,0.0f);
		float inf5 = ni.influence("E");
		Assert.assertEquals(inf5,2.625,0.0f);	
		float inf6 = ni.influence("F");
		Assert.assertEquals(inf6,1,0.0f);
		float inf7 = ni.influence("G");
		Assert.assertEquals(inf7,1,0.0f);
		float inf8 = ni.influence("H");
		Assert.assertEquals(inf8,1.5,0.0f);
	}

	@Test
	public void testInfluenceArrayListOfString() {
		ArrayList<String> arr = new ArrayList<>(Arrays.asList(new String[] {"A","H"}));
		float inf1 = ni.influence(arr);
		Assert.assertEquals(inf1,4.5,0.0f);
		arr.add("B");
		float inf2 = ni.influence(arr);
		Assert.assertEquals(inf2,5.5,0.0f);
		arr.add("G");
		float inf3 = ni.influence(arr);
		Assert.assertEquals(inf3,6,0.0f);
	}

	@Test
	public void testMostInfluentialDegree() {
		ArrayList<String> most = ni.mostInfluentialDegree(2);
		Assert.assertEquals(most,new ArrayList<String>(Arrays.asList(new String[] {"A","B"})));
		//Your output array may be in a different order
	}

	@Test
	public void testMostInfluentialModular() {
		ArrayList<String> most = ni.mostInfluentialModular(3);
		Assert.assertEquals(new ArrayList<String>(Arrays.asList(new String[] {"A","B","E"})), most);
		//Your output array may be in a different order
	}


	@Test
	public void testMostInfluentialSubModular() {
		ArrayList<String> most = ni.mostInfluentialSubModular(3);
		Assert.assertEquals(most,new ArrayList<String>(Arrays.asList(new String[] {"A","B","D"})));
		//Your output array may be in a different order
	}

	
}
