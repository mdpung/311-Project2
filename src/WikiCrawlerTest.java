import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class WikiCrawlerTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList<String> topics = new ArrayList<String>();

        WikiCrawler wc = new WikiCrawler("/wiki/Complexity_theory", 20, topics, "name");

        wc.crawl();

        System.out.println();
    }
}
