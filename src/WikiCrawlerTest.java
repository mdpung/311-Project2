import java.io.IOException;
import java.util.ArrayList;

public class WikiCrawlerTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList<String> topics = new ArrayList<String>();

        WikiCrawler wc = new WikiCrawler("/wiki/Computer_Science", 100, topics, "WikiCS.txt");

        wc.crawl();

        System.out.println();
    }
}
