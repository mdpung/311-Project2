// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add additional methods and fields)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may include java.util.ArrayList etc. here, but not junit, apache commons, google guava, etc.)

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import java.net.URL;
import java.net.MalformedURLException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

import java.lang.StringBuilder;
import java.lang.Thread;


public class WikiCrawler {
    private static final String BASE_URL = "https://en.wikipedia.org";
    private static final String P_TAG = "<p>";
    private static final String A_TAG = "<a";
    private static final String A_TAG_AND_LI_TAG = "<li><a";

    private int max;
    private ArrayList<String> topics;
    private String fileName, seed;
    HashMap<String, ArrayList<String>> webGraph;


    /**
     * @param seedUrl  - A string seedUrl–relative address of the seed url (within Wiki domain).
     * @param max      - An integer max representing Maximum number pages to be crawled.
     * @param topics   - An array list of keywords called topics. The keywords describe a particular topic.
     * @param fileName - A string fileName representing name of a file–The graph will be written to this file.
     */
    public WikiCrawler(String seedUrl, int max, ArrayList<String> topics, String fileName) throws MalformedURLException, FileNotFoundException {
        seed = seedUrl;
        this.max = max;
        this.topics = topics;
        this.fileName = fileName;
    }

    /**
     * This method gets a string (that represents contents of a .html
     * file) as parameter. This method should return an array list (of Strings)
     * consisting of links from doc.
     *
     * @param doc
     *      HTML code to parse through
     * @return
     *      ArrayList of links that the HTML doc contains
     */
    public ArrayList<String> extractLinks(String doc) {
        boolean afterFirstPTag = false;

        ArrayList<String> links = new ArrayList<>();
        String line, sub, modifedString;
        Scanner sc, s;
        sc = new Scanner(doc);

        //Reads entire document
        while (sc.hasNext()) {
            line = sc.nextLine();

            //Makes sure we are after the first <p>
            if (!afterFirstPTag && line.contains(P_TAG))
                afterFirstPTag = true;

            if (afterFirstPTag) {

                s = new Scanner(line);

                //Scans each substring of line for <a
                while (s.hasNext()) {

                    sub = s.next();
                    //If <a then our href is in this substring
                    if (sub.contains(A_TAG) || sub.contains(A_TAG_AND_LI_TAG)) {

                        while (s.hasNext()) {
                            sub = s.next();
                            if (sub.length() > 6) {
                                if (sub.substring(0, 6).equals("href=\"")) {
                                    modifedString = modifyString(sub);

                                    //Finding links of the correct format
                                    if (modifedString.contains("/wiki/") && !modifedString.contains("#")
                                            && !modifedString.contains(":") && !modifedString.contains(".org")) {
                                        links.add(modifedString);
                                    }
                                }
                            }
                        }
                    }
                }
                s.close();

            }
        }


        sc.close();
        return links;
    }

    /**
     * This method should construct the web graph over following pages: If seedUrl does
     * not contain all words from topics, then the graph constructed is empty graph(0 vertices and 0
     * edges). Suppose that seedUrl contains all words from topics. Consider the first max many pages
     * (including seedUrl page), that contain every word from the list topics, that are visited when you
     * do a BFS with seedUrl as root. Your program should construct the web graph only over those
     * pages. and writes the graph to the file fileName.
     */
    public void crawl() throws IOException, InterruptedException {

        int currentCount = 1;
        int openingLinks = 0;

        PrintWriter pw = new PrintWriter(fileName);

        boolean enter = false;
        webGraph = new HashMap<>();
        LinkedList<String> queue = new LinkedList<>();
        ArrayList<String> mainLinks = new ArrayList<>();
        String currentUrl = seed;

        String currHTML = "";
        queue.add(currentUrl);
        pw.println(max);

        while (!queue.isEmpty()) {
            ArrayList<String> links = new ArrayList<>();
            ArrayList<String> potentialLinks = new ArrayList<>();
            currentUrl = queue.poll();
            System.out.println(currentUrl);
            if (!webGraph.containsKey(currentUrl)) {
                currHTML = htmlToString(currentUrl);


                if (openingLinks % 25 == 0) {
                    Thread.sleep(3000);
                }

                openingLinks++;
                potentialLinks = extractLinks(currHTML);
            }

            for (String linkToCheck : potentialLinks) {
                if ((containsTopics(currHTML) || topics.isEmpty())) {

                    if ((currentCount < max && !links.contains(linkToCheck)) && !currentUrl.equals(linkToCheck)) {
                        links.add(linkToCheck);

                        mainLinks.add(linkToCheck);

                        queue.add(linkToCheck);
                        currentCount++;

                        pw.println(currentUrl + " " + linkToCheck);
                    }
                }
            }

            if (enter) {
                for (String linkToCheck : potentialLinks) {
                    if ((mainLinks.contains(linkToCheck) && !links.contains(linkToCheck)) && !currentUrl.equals(linkToCheck)) {
                        links.add(linkToCheck);

                        pw.println(currentUrl + " " + linkToCheck);
                    }
                }
            }
            enter = true;

            webGraph.put(currentUrl, links);
        }
        pw.close();
    }

    /**
     * *---------------------------------------------------------*
     * |          The following are helper methods.              |
     * *---------------------------------------------------------*
     */

    //Cuts out href="" from the substring
    private String modifyString(String s) {
        String modifedString;
        if (s.charAt(s.length() - 1) == '\"')
            modifedString = s.substring(6, s.length() - 1);
        else
            modifedString = s.substring(6, s.substring(6, s.length()).indexOf("\"") + 6);

        return modifedString;
    }

    public boolean containsTopics(String doc) throws IOException {
        for (String topic : topics) {
            if (!doc.contains(topic)) {
                return false;
            }
        }
        return true;
    }

    //Turns an html into a String and returns it.
    public String htmlToString(String seedUrl) throws IOException, MalformedURLException {
        StringBuilder sb = new StringBuilder();
        URL docURL = new URL(BASE_URL + seedUrl);
        InputStream is = docURL.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        br.close();

        return sb.toString();
    }

}