/**
 * Creates a graph of RAT objects based on information from a TSV.
 *
 * @author Kathryn Swint
 * @version 12/05/2019
 */

import javafoundations.*;
import java.util.*;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RATgraph
{
    // instance variables
    protected AdjListsGraph<String> graph; //how we'll graph the RATs and stories

    protected Hashtable<String,RAT> accounts; //a hashtable of RATs. 
    //key = username, value = RAT object
    protected Hashtable<String,Integer> stories; //a hashtable of stories. key = story, 
    //value = # of times the story was tweeted about
    protected String[] usernames; //array of RAT usernames
    protected String[] userIDs; //array of RAT userIDs

    protected int usernamesSize; //number of usernames in the usernames array
    protected int userIDsSize; //number of userIDs in the userIDs array

    /**
     * Constructor for objects of class RATgraph
     * 
     * @param fileName the name of the file to read from
     */
    public RATgraph(String fileName)
    {
        // initialise instance variables
        accounts = new Hashtable<String,RAT>();
        stories = new Hashtable<String,Integer>();

        usernames = new String[300]; //assumes there are <=300 RATs
        userIDs = new String[300];

        usernamesSize = 0;
        userIDsSize = 0;

        readFromFile(fileName); //reads RATs from file

        graph = new AdjListsGraph<String>(); 
        createGraph(fileName); //creates the graph of RATs
    }

    /**
     * Reads from a TSV and creates a new RAT object with the information on each
     * line. 
     * 
     * @param fileName the file to read from
     */
    public void readFromFile(String fileName)
    {
        try{
            Scanner scan = new Scanner(new File(fileName));
            String header = scan.nextLine(); //takes care of file header

            while (scan.hasNext()) {
                String[] input = scan.nextLine().split("\t"); //splits @ tabs since file is a TSV

                String user = input[0];
                String accountNum = input[1];
                long tweetCount = Long.parseLong(input[2]);
                long storyCount = Long.parseLong(input[3]);

                addUsername(user); //adds username to the username array
                addUserID(accountNum); //adds userID to the userID array

                RAT current = new RAT(user, accountNum, tweetCount, storyCount);
                String[] currentStories = input[4].split(","); //splits the stories at commas

                for (String s : currentStories) {
                    current.addStory(s); //adds the story to the current RAT
                    if (!stories.containsKey(s)) {
                        stories.put(s, 1); //if the story is not in the stories Hashtable, 
                        //adds it with a "tweeted about" count of 1
                    } else {
                        stories.replace(s, (1 + stories.get(s))); //increases "tweeted about" count
                    }
                }

                accounts.put(user, current); //adds this RAT to the hashtable of RATs
            }

            scan.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File " + fileName + " was not found.");
        } catch (NumberFormatException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Adds a username to the usernames array.
     * 
     * @param u the username to add
     */
    private void addUsername(String u) {
        if (usernamesSize == usernames.length) {
            String[] temporary = new String[usernames.length * 2];

            for (int i = 0; i < usernames.length; i++) {
                temporary[i] = usernames[i];
            }

            usernames = temporary;
        }

        usernames[usernamesSize++] = u; //adds the username to the array & increases usernamesSize
    }

    /**
     * Adds a userID to the userIDs array
     * 
     * @param u the userID to add
     */
    private void addUserID(String u) {
        if (userIDsSize == userIDs.length) {
            String[] temporary = new String[userIDs.length * 2];

            for (int i = 0; i < userIDs.length; i++) {
                temporary[i] = userIDs[i];
            }

            userIDs = temporary;
        }

        userIDs[userIDsSize++] = u; //adds the userID to the array & increases userIDsSize
    }

    /**
     * Creates a graph from the accounts and stories Vertex structures. 
     *
     * @param fileName the fileName you read from previously (used here
     *          for the exception).
     */
    protected void createGraph(String fileName)
    {
        try {
            String currentUser;
            LinkedList<String> currentStories;
            RAT currentRAT;
            String currentU;

            for (int i = 0; i < usernamesSize; i++) {
                currentU = usernames[i]; //gets username from usernames array
                currentRAT = accounts.get(currentU); //gets RAT from accounts using username as key

                graph.addVertex(currentU); //adds username as a vertex to the graph
                for (String story : currentRAT.stories) {
                    graph.addVertex(story); //adds the story to the graph

                    //used arcs instead of edges here to ensure that yEd correctly displayed
                    //our bipartite graph. We're unsure why edges created a bug in yEd.
                    graph.addArc(currentU, story); //creates an arc between the user and story
                    graph.addArc(story, currentU); //creates an arc between the story and use
                }
            }
        } catch (NullPointerException ex) {
            System.out.println(ex + " for file " + fileName);
        }
    }

    public void exportAccounts(String fileName) {
        try {
            Iterator<String> iterator = accounts.keySet().iterator(); //iterates over accounts keys
            String s = "Username,storyCount,tweetCount\n";

            while(iterator.hasNext()) {
                String key = iterator.next();
                RAT current = accounts.get(key);
                s += key + "," + current.storyCount  + "," + current.tweetCount + "\n";
            }

            PrintWriter w = new PrintWriter(new File(fileName));
            w.println(s);
            w.close(); //for tidiness!
        }
        catch (IOException e) {
            System.out.println (e);
        }
    }
    
    public void exportStories(String fileName) {
        try {
            Iterator<String> iterator = stories.keySet().iterator(); //iterates over accounts keys
            String s = "storyID,times_tweeted_about";

            while(iterator.hasNext()) {
                String key = iterator.next();
                int current = stories.get(key);
                s += key + "," + current + "\n";
            }

            PrintWriter w = new PrintWriter(new File(fileName));
            w.println(s);
            w.close(); //for tidiness!
        }
        catch (IOException e) {
            System.out.println (e);
        }
    }

    public static void main(String[] args) {
        RATgraph r = new RATgraph("All_Russian-Accounts-in-TT-stories.csv.tsv");
        //RATgraph r = new RATgraph("TestFile1.txt");

        System.out.println(r.accounts);
        System.out.println(r.stories);
        System.out.println(r.accounts.size());

        System.out.println(r.graph);
        r.graph.saveToTGF("RATgraph.tgf");

        System.out.println("Testing DFTraversal on 155892608: " + r.graph.DFtraversal("155892608"));
    }
}
