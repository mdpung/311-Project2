import java.io.IOException;
import java.util.ArrayList;

public class WikiCrawlerTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList<String> topics = new ArrayList<String>();
        topics.add("Cyclones");
        topics.add("Iowa State");

//        WikiCrawler wc = new WikiCrawler("/wiki/Iowa_State_University", 10, topics, "WikiISU.txt");
        WikiCrawler wc = new WikiCrawler("/wiki/Iowa_State_University", 100, topics, "WikiISU.txt");
//        WikiCrawler wc = new WikiCrawler("/wiki/Computer_Science", 100, topics, "WikiCS.txt");
        float start = System.nanoTime();
        wc.crawl();
        float end = System.nanoTime();
        System.out.println("Time: " + (end - start) / 1000000000);

    }
}
