import java.util.ArrayList;

public class WebPage {
    private String seed, html;
    private ArrayList<String> potentialLinks;
    private ArrayList<WebPage> edges;

    public WebPage(String s, String h) {
        seed = s;
        html = h;
        potentialLinks = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public void addPotentialLinks(ArrayList<String> links) {
        potentialLinks.addAll(links);
    }

    public void addEdge(WebPage page) {
        edges.add(page);
    }

    public String getSeed() {
        return seed;
    }

    public String getHtml() {
        return html;
    }

    public ArrayList<String> getPotentialLinks() {
        return potentialLinks;
    }

    public ArrayList<WebPage> getEdges() {
        return edges;
    }
}
