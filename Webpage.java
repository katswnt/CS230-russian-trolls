/**
 * Creates a single webpage object with a URL, a number of lines
 * on the page, and the HTML on the page. Stores up to the first
 * 30 characters. 
 *
 * @author Kat Swint, adapted from work done with Anushe Sheikh
 * @version 12/04/2019
 */

import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.lang.Exception;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Webpage implements Comparable<Webpage>
{
    protected URL url;
    protected int numLines = 0;
    protected String contents;

    /**
     * Constructor for objects of class Webpage
     * @param u url
     */
    public Webpage(String u)
    {
        try {
            url = new URL(u);
            readWebpage(u); // Instantiates numLines and content & prevents
            // having to create object and read lines separately. 
        } catch (MalformedURLException ex){ // makes sure that the url is valid
            System.out.println(ex);
        }
    }

    /**
     * Reads from the webpage one line at a time, instantiates the
     * numLines and content variables in the Webpage object
     * 
     * @exception IOException thrown when the input is invalid
     * @param urlName the URL of the page we read from
     */
    public void readWebpage(String urlName) {
        try {
            URL u = new URL(urlName);
            Scanner urlScan = new Scanner(u.openStream()); 
            int count = 0;
            String allContents = "";
            contents = "";
            
            while (urlScan.hasNext()) {
                count += 1;
                allContents += urlScan.nextLine(); //concatenates each line into one string
            }

            numLines = count; 
            allContents = allContents.substring(465,allContents.length());
            
            for (int i = 0; i < (allContents.length() - 13); i++) {
                if (!allContents.substring(i,(i + 13)).equals("TwitterTrails")) {
                    contents += allContents.charAt(i);
                } else {
                    break;
                }
            }
            
            if (contents.substring(contents.length() - 3, contents.length()).equals(" - ")) {
                contents = contents.substring(0, contents.length() - 3);
            }
        } catch (IOException ex){ 
            System.out.println(ex);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println(ex);
        } 
    }

    /**
     * Assumes that if two webpage objects have the same URL, they're
     * the same pages. Returns a positive number (1) if they're 
     * identical. 
     * 
     * @param w2 the webpage you want to compare it to
     */
    public int compareTo(Webpage w2){
        URL w2URL = w2.getURL(); 
        if (w2URL.equals(url)) {
            return 1; 
        }
        else {
            return -1; 
        }
    }

    /**
     * Getter for URL.
     * 
     * @return the URL
     */
    public URL getURL() {
        return url;
    }

    /**
     * Getter for numLines.
     * 
     * @return numLines
     */
    public int getNumLines() {
        return numLines;
    }

    /**
     * Getter for content.
     * 
     * @return contents
     */
    public String getContents() {
        return contents;
    }

    /**
     * toString method. 
     * 
     * @return a formatted string displaying a single webpage object
     */
    public String toString()
    {
        String u = url.toString();
        int storyLength = u.length() - 70;
        return ("Story " + u.substring(u.length() - storyLength, u.length()) + ": " + contents);
    }

    /**
     * Main method with testing code
     */
    public static void main(String[] args) {
        // System.out.println("*****Testing readWebpage*****");
        // Webpage w1 = new Webpage("https://cs.wellesley.edu/~cs230/");
        // System.out.println(w1);

        // Webpage w2 = new Webpage("https://docs.oracle.com/javase/8/docs/api/java/io/FileNotFoundException.html");
        // System.out.println(w2);

        // Webpage w3 = new Webpage("https://www.nytimes.com");
        // System.out.println(w3);

        // Webpage w4 = new Webpage("https://cs.wellesley.edu/~cs230/");
        // System.out.println(w4);

        // Webpage w5 = new Webpage("https://www.wellesley.edu");
        // System.out.println(w5);

        // Webpage w6 = new Webpage("https://www.wired.com");
        // System.out.println(w6);

        // Webpage w7 = new Webpage("https://cs.wellesley.edu/~cs230/tmp/aFewWebPagesForTesting/0lines.txt");
        // System.out.println(w7);

        // Webpage w8 = new Webpage("https://cs.wellesley.edu/~cs230/tmp/aFewWebPagesForTesting/1lines.txt");
        // System.out.println(w8);

        // Webpage w9 = new Webpage("https://cs.wellesley.edu/~cs230/tmp/aFewWebPagesForTesting/10lines.txt");
        // System.out.println(w9);

        // Webpage w10 = new Webpage("https://calendar.google.com/calendar/b/1/r/week?pli=1");
        // System.out.println(w10);

        // System.out.println("\n*****Testing compareTo*****");
        // System.out.println("Comparing first webpage to second. Expect -1: " + w1.compareTo(w2));
        // System.out.println("Comparing first webpage to itself. Expect 1: " + w1.compareTo(w1));
        // System.out.println("Comparing first webpage to fourth. Expect 1: " + w1.compareTo(w4));
    }
}
