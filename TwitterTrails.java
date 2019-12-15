/**
 * Adapted from the Cyberwalk class from a previous PSET.
 * "TwitterTrails is used to contain and maintain a collection of Webpage objects.
 * it provides functionality related to storing, retrieving, and/or printing 
 * webpages in a LIFO manner, one per line." - Assignment instructions
 *
 * @author Kat Swint, adapted from work done with Anushe Sheikh
 * @version 12/06/2019
 */

import java.util.Scanner;
import java.util.Iterator;
import java.io.*;
import javafoundations.ArrayStack;
import java.util.Hashtable;

public class TwitterTrails
{
    private ArrayStack<Webpage> collection;

    /**
     * Constructor for objects of class TwitterTrails
     * 
     * @param hashtable the hashtable made in Investigate that we want to process
     */
    public TwitterTrails(Hashtable<String,Integer> hashtable)
    {
        collection = new ArrayStack<Webpage>();
        readStories(hashtable);
        System.out.println(collection);
    }

    /**
     * Reads from the input file one URL at a time, creates a new 
     * Webpage object with the URL it reads, and pushes the new
     * Webpage object into the collection of URLs made by TwitterTrails.
     * 
     * @exception FileNotFoundException thrown when the input file 
     * is not found
     */
    public void readStories(Hashtable<String,Integer> hashtable){
        Iterator<String> iterator = hashtable.keySet().iterator(); //iterates over stories keys

        while(iterator.hasNext()) {
            String key = iterator.next(); //the next story in the Vector
            String url = "http://twittertrails.wellesley.edu/~trails/stories/investigate.php?id=" + key;
            Webpage w = new Webpage(url);
            collection.push(w);
        }
    }

    /**
     * toString method. 
     * 
     * @return a formatted string displaying a all webpage objects in a 
     *         collection and the webpage with the most number of lines
     */
    public String toString(){
        ArrayStack<Webpage> tempStack = new ArrayStack<Webpage>(); // creates a new tempArray stack to aid printing
        String s = "\n";
        int mostLines = 0; 
        String mL = ""; // records which website has the most content on page, updated in if-while loop below

        if (!collection.isEmpty()) { 
            // loops through webpage objects to print and compare their content lengths
            while (!collection.isEmpty()) { 
                Webpage page = collection.pop();
                s += page.toString() + "\n";
                tempStack.push(page);
                if (page.numLines > mostLines) { // updates which webpage has most number of lines
                    mostLines = page.numLines;
                    mL = page.toString();
                }
            }

            // restores the original stack by transferring objects from temporary stack
            while (!tempStack.isEmpty()) { 
                collection.push(tempStack.pop());
            }

            s += "\nThe largest Webpage was: " + mL;
        }

        return s;
    }

    /**
     * Main method with testing code
     */
    public static void main(String[] args){
        // System.out.println("\n*****Testing readFromFile*****");
        // TwitterTrails c1 = new TwitterTrails();
        // c1.readFromFile("urls.txt");
        // System.out.println(c1);

        // System.out.println("\n*****Testing readFromKeyboard*****");
        // TwitterTrails c2 = new TwitterTrails();
        // c2.readFromKeyboard();
        // System.out.println(c2);

        // System.out.println("\n*****Testing readFromFile*****");
        // TwitterTrails c3 = new TwitterTrails();
        // c3.readFromFile("websites1.txt");
        // System.out.println(c3);

        // System.out.println("\n*****Testing toString when the colletion is empty*****");
        // TwitterTrails c4 = new TwitterTrails();
        // System.out.println(c4);
        
    }
}

