/**
 * Created by Jeff on 4/14/2018.
 */
public class Vertex {

    public String name;

    public Vertex prev;

    public boolean visited;

    public Vertex(String name){
        this.name = name;
        this.visited = false;
        prev = null;
    }
}
