import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class Tests {

    private static NetworkInfluence influence;

    @BeforeClass
    public static void init() throws FileNotFoundException {
        influence = new NetworkInfluence("C:\\Users\\jeffy\\Desktop\\PA2-FML\\wikiCC.txt");
    }

    @Test
    public void testConstructor(){
        assertEquals(20, influence.numVert);
        //assertEquals(40, influence.edges.length);
    }

    @Test
    public void testInitVertices(){
        assertEquals(20, influence.vertices.size());
        assertEquals(20, influence.vertexObjects.size());
    }


    @Test
    public void testInitAdjacenyList(){
        assertEquals(20, influence.adjList.size());
    }

    @Test
    public void testOutDegree(){
        assertEquals(11, influence.outDegree(influence.vertices.get(0)));
        assertEquals(13, influence.outDegree(influence.vertices.get(1)));
        assertEquals(10, influence.outDegree(influence.vertices.get(2)));
    }

    //ATTENTION I THINK THERE IS A BUG HERE
    @Test
    public void testShortestPath(){
       assertEquals(0, influence.shortestPath("/wiki/Complexity_theory", "dogs").size());
       assertEquals(3, influence.shortestPath("/wiki/Complexity_theory", "/wiki/System").size());
    }


    @Test
    public void testInfluence() throws FileNotFoundException {
        //assertEquals();
        NetworkInfluence n = new NetworkInfluence("C:\\Users\\jeffy\\Desktop\\tester.txt");
        //ArrayList<String> s = new ArrayList<String>();
        //s.add("A"); s.add("C");
        System.out.println(n.influence("G"));
    }

}
