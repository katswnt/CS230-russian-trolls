/**
 * Creates a graph of RAT objects using class RATgraph, then
 * analyzes the graph's RAT and story nodes and their connections.
 * Produces printed strings as output.
 *
 * @author Kathryn Swint
 * @version 12/05/2019
 */

import java.util.*;
import java.io.*; 
import javafoundations.*;

public class Investigate
{
    protected RATgraph g;

    protected int highestStories; //highest # of stories a RAT participated in
    protected int lowestStories; //lowest # of stories a RAT participated in

    protected int highestRATs; //highest # of RATs that participated in one story
    protected int lowestRATs; //lowest # of RATs that participated in one story

    protected int medStoriesParticipated; //median # of stories that each RAT participated in
    protected int avgStoriesParticipated; //average # of stories that each RAT participated in

    protected int medRATsPerStory; //median # of RATs that participated in each story
    protected int avgRATsPerStory; //average # of RATs that participated in each story

    protected int diameter;
    protected int radius;
    protected LinkedList<String> centerNodes;

    protected int numPops;

    protected Hashtable<String,RAT> mostActiveRATs;
    protected Hashtable<String,Integer> mostPopStories;
    protected Hashtable<String,Integer> leastPopStories;

    protected LinkedList<Integer> storiesPerRATValues; //used for calculating medians and averages
    protected LinkedList<Integer> RATsPerStoryValues; //used for calculating medians and averages

    /**
     * Constructor for objects of class Investigate
     * 
     * @param fileName the file to read from
     * @param numPops how many "popular" or "active" stories/RATs to show
     */
    public Investigate(String fileName, int numPops) {
        g = new RATgraph(fileName);

        this.numPops = numPops;

        storiesPerRATValues = new LinkedList<Integer>();
        RATsPerStoryValues = new LinkedList<Integer>();
        centerNodes = new LinkedList<String>();
        mostActiveRATs = new Hashtable<String,RAT>();
        mostPopStories = new Hashtable<String,Integer>();
        leastPopStories = new Hashtable<String,Integer>();
    }

    /**
     * Gets the number of RATs that participated in each story and determines
     * what the highest and lowest number of stories participated in is.
     */
    protected void RATsPerStory() {
        Iterator<String> iterator = g.stories.keySet().iterator(); //iterates over stories keys

        highestStories = 0;
        lowestStories = 5000; //assumes that no one participated in as many as 5000 stories
        // (given that there aren't 5000 stories)

        while(iterator.hasNext()) {
            String key = iterator.next(); //the next story in the Vector
            int currentRATs = g.stories.get(key); //the number of RATs that participated in
            //each story
            RATsPerStoryValues.add(currentRATs); //adds the number to a LinkedList of values for
            //later use
            if (currentRATs >= highestStories) { //if the RATs count is higher, updates highestStor
                highestStories = currentRATs;
            }

            if (currentRATs <= lowestStories) { //if the RATs count is lower, updates lowestStories
                lowestStories = currentRATs;
            }
        }
    }

    /**
     * Figures out the x (user input) popular stories based on the number of RATs
     * that participated in them.
     * 
     * @param x how many stories to return
     * @return a string representation of the x most popular stories
     */
    protected String mostPopularStories(int x) {
        Iterator<String> iterator = (g.stories.keySet()).iterator(); //iterates over stories keys
        String s = "";

        int count = 0; //counts how many stories have been added. Makes it possible to include
        //values that are tied for the highest participation that would exceed
        //the user input

        while(iterator.hasNext()) {
            String key = iterator.next(); //gets next key
            if (g.stories.get(key) == highestStories) { //if the story count was the highest, adds it!
                count++;
                s += ("\nStory: "  + key 
                    + "\t\tTimes Tweeted About: "  + g.stories.get(key));
                mostPopStories.put(key, g.stories.get(key));
            }
        }

        highestStories--; //subtracts by one to find the next highest stories

        if (count < x) { //if we haven't found x RATs, calls mostPopularStories recursively
            s += mostPopularStories(x - count);
        }

        return s;
    }

    /**
     * Figures out the x (user input) popular stories based on the number of RATs
     * that participated in them.
     * 
     * @param x how many stories to return
     * @return a string representation of the x most popular stories
     */
    protected String leastPopularStories(int x) {
        Iterator<String> iterator = (g.stories.keySet()).iterator(); //iterates over stories keys
        String s = "\n   Stories with " + lowestRATs + " participating:\n   \t";

        int count = 0; //counts how many stories have been added. Makes it possible to include
        //values that are tied for the highest participation that would exceed
        //the user input

        while(iterator.hasNext()) {
            String key = iterator.next(); //gets next key
            if (g.stories.get(key) == lowestStories) { //if the story count was the lowest, adds it!
                count++;
                s += (key + ", ");
                leastPopStories.put(key, g.stories.get(key));
            }
        }

        lowestStories++; //subtracts by one to find the next lowest stories

        if (count < x) { //if we haven't found x RATs, calls mostPopularStories recursively
            s += leastPopularStories(x - count);
        }

        s = s.substring(0, (s.length() - 2));

        return s;
    }

    /**
     * Gets the number of stories that each RAT participated in and determines
     * what the highest and lowest number of RATs per story is.
     */
    protected void storiesPerRAT() { //could just use story_count...
        // Collection Iterator
        Iterator<String> iterator = g.accounts.keySet().iterator(); //iterates over accounts keys

        highestRATs = 0; 
        lowestRATs = 5000; // there are fewer than 5000 RATs, so assumes this is high enough

        while(iterator.hasNext()) {
            String key = iterator.next(); //gets next ket
            RAT current = g.accounts.get(key); //gets next RAT

            int numStories = g.accounts.get(key).stories.size(); //gets # of stories the RAT
            //participated in
            storiesPerRATValues.add(numStories);//adds the number to a LinkedList of values for
            //later use
            if (numStories >= highestRATs) { //if the story count is higher, updates highestRATs
                highestRATs = numStories;
            }

            if (numStories <= lowestRATs) { //if the story count is lower, updates lowestRATs
                lowestRATs = numStories;
            }
        }
    }

    /**
     * Figures out the x (user input) most active RATs based on the number of stories
     * each RAT participated in
     * 
     * @param x how many RATs to return
     * @return a string representation of the x most active RATs
     */
    protected String mostActiveRATs(int x) {
        Iterator<String> iterator = (g.accounts.keySet()).iterator(); //iterates over accounts keys
        String s = "";

        int count = 0;  //counts how many RATs have been added. Makes it possible to include
        //values that are tied for the highest participation that would exceed
        //the user input
        while(iterator.hasNext()) {
            String key = iterator.next(); //gets next key
            RAT current = g.accounts.get(key); //gets next RAT
            int numStories = g.accounts.get(key).stories.size(); //how many stories the RAT
            //participated in
            if (numStories == highestRATs) { //sees if RAT participated in the highest # of stories
                count++;
                s += ("\nRat: "  + key 
                    + "\t\tNumber of Stories Tweeted About: "  + numStories);
                mostActiveRATs.put(key, current);
            }
        }

        highestRATs--; //decrements by one to check next highest participation level

        if (count < x) { //if we haven't found x RATs, calls mostActiveRATs recursively
            s += mostActiveRATs(x - count); 
        }

        return s;
    }

    /**
     * Figures out the x (user input) most active RATs based on the number of stories
     * each RAT participated in
     * 
     * @param x how many RATs to return
     * @return a string representation of the x most active RATs
     */
    protected String leastActiveRATs(int x) {
        Iterator<String> iterator = (g.accounts.keySet()).iterator(); //iterates over accounts keys
        String s = "\n   Tweeted about " + lowestRATs + " stories:\n   \t";

        int count = 0;  //counts how many RATs have been added. Makes it possible to include
        //values that are tied for the highest participation that would exceed
        //the user input
        while(iterator.hasNext()) {
            String key = iterator.next(); //gets next key
            RAT current = g.accounts.get(key); //gets next RAT
            int numStories = g.accounts.get(key).stories.size(); //how many stories the RAT
            //participated in
            if (numStories == lowestRATs) { //sees if RAT participated in the highest # of stories
                count++;
                s += (key + ", ");
            }
        }

        lowestRATs++; //decrements by one to check next highest participation level

        if (count < x) { //if we haven't found x RATs, calls mostActiveRATs recursively
            s += mostActiveRATs(x - count); 
        }

        s = s.substring(0, (s.length() - 2));

        return s;
    }

    /**
     * Figures out the average and median stories participated in per RAT AND
     * the average and median RATs per story. Prints both values. Assumes that
     * the lists will only be the ones created by this Investigate class. Throws
     * an error if an invalid list is given, but it still won't work on any list
     * except the specified two.
     * 
     * @param list the list to find the values from
     */
    protected void normalDistribution(LinkedList<Integer> list) {
        int sum = 0;

        for (int value : list) {
            sum += value;
        }

        if (list.equals(storiesPerRATValues)) {

            if (storiesPerRATValues.size()%2 == 0) {
                medStoriesParticipated = list.get(list.size() / 2);
            } else {
                medStoriesParticipated = list.get(list.size() / 2 + 1);
            }

            avgStoriesParticipated = sum / list.size();

            System.out.println("\nThe highest number of RATs that participated in a story is: " +
                highestStories);
            System.out.println("The lowest number of RATs that participated in a story is: " +
                lowestStories);
            System.out.println("The median number of RATs that participated in each story is: " +
                medStoriesParticipated);
            System.out.println("The average number of of RATs that participated in each story is: " +
                avgStoriesParticipated);        
        } else if (list.equals(RATsPerStoryValues)) {

            if (RATsPerStoryValues.size()%2 == 0) {
                medRATsPerStory = list.get(list.size() / 2);
            } else {
                medRATsPerStory = list.get(list.size() / 2 + 1);
            }

            avgRATsPerStory = sum / list.size();

            System.out.println("\nThe highest number of stories tweeted about by a RAT is: " +
                highestRATs);
            System.out.println("The lowest number of stories tweeted about by a RAT is: " +
                lowestRATs);
            System.out.println("The median number of stories tweeted about by the RATs is: " +
                medRATsPerStory);
            System.out.println("The average number of stories tweeted about by the RATs is: " +
                avgRATsPerStory);   
        } else {
            System.out.println("Please input a valid list.");
        }
    }

    /**
     * Uses breadth first search to find the diameter of the graph
     */
    public int getDiameter() {
        Iterator<String> iterator = g.accounts.keySet().iterator(); //iterates over accounts keys
        diameter = 0; //initial diameter, expected to grow

        boolean checked = false;
        int levels = 0;

        while(iterator.hasNext()) {
            String key = iterator.next();
            RAT current = g.accounts.get(key);

            LinkedList<String> currentBF = g.graph.BFtraversal(current.username); //BFS on current RAT            
            String first = currentBF.remove();
            levels = 0;

            for (String element : currentBF) {
                if ((g.accounts.containsKey(element)) && (checked == false)) {
                    checked = true;
                    levels++;
                } if (!g.accounts.containsKey(element) && (checked == true)) {
                    checked = false;
                    levels++;
                }
            }

            if (levels >= diameter) {
                diameter = levels; //diameter grows as soon as the BFS produces a larger value
            }
        }

        return levels;
    }

    /**
     * Uses breadth first search to find the radius of the graph
     */
    public int getRadius() {
        Iterator<String> iterator = g.accounts.keySet().iterator(); //iterates over accounts keys
        radius = diameter; //initial diameter, expected to shrink

        boolean checked = false;
        int levels = 0;

        while(iterator.hasNext()) {
            String key = iterator.next();
            RAT current = g.accounts.get(key);

            LinkedList<String> currentBF = g.graph.BFtraversal(current.username); //BFS on current RAT            
            String first = currentBF.remove();
            levels = 0;

            for (String element : currentBF) {
                if ((g.accounts.containsKey(element)) && (checked == false)) {
                    checked = true;
                    levels++;
                } if (!g.accounts.containsKey(element) && (checked == true)) {
                    checked = false;
                    levels++;
                }
            }

            if (levels == radius) {
                centerNodes.add(current.username); //adds the user to the list if they have an eccentricity that 
                                                   //matches the current eccentricity
            }
            if (levels < radius) { //if the user has an eccentricity smaller than the current eccentricity...
                radius = levels; //radius shrinks as soon as the BFS produces a smaller value
                centerNodes.clear(); //clears the previous centerNodes list
                centerNodes.add(current.username); //adds the current user to the now-empty centerNodes list
            }
        }
        
        return radius;
    }

    /**
     * Uses depth first search to determine whether the graph is connected or not.
     * Uses the first RAT just for ease of use, then compares all other RATs to the
     * list of RATs provided by the DFS to determine whether the graph is connected or not.
     * 
     * @return a boolean indicating the graph's connectivity
     */
    public boolean isConnected() {
        Iterator<String> iterator1 = g.accounts.keySet().iterator(); //iterates over the keys
        Iterator<String> iterator2 = g.stories.keySet().iterator(); //iterates over the keys
        String key = iterator1.next(); //gets the first RAT's key
        RAT current = g.accounts.get(key); //gets the first RAT

        LinkedList DFresults = g.graph.DFtraversal(current.username); //performs DFS on the RAT

        while(iterator1.hasNext()) {
            key = iterator1.next();
            if (!DFresults.contains(key)) {
                return false; //returns false as soon as a RAT in the graph is not found in the
                //DFS linked list
            }
        }
        
        while(iterator2.hasNext()) {
            key = iterator2.next();
            if (!DFresults.contains(key)) {
                return false; //returns false as soon as a RAT in the graph is not found in the
                //DFS linked list
            }
        }

        return true; //returns true if all RATs were found in the DFS linked list
    }

    /**
     * Determines how many RATs participated in both stories passed in as
     * parameters. Used to figure out if popular stories were often tweeted
     * about by the same RATs or if the groups differed.
     * 
     * @param story1 the first story
     * @param story2 the second story
     * 
     * @return a list of RATs that tweeted about both stories
     */
    public LinkedList<String> compareRATs(String story1, String story2) {
        Iterator<String> iterator = g.accounts.keySet().iterator(); //iterates over the keys

        LinkedList<String> overlaps = new LinkedList<String>();
        
        while(iterator.hasNext()) {
            String key = iterator.next(); //gets the first RAT's key
            RAT current = g.accounts.get(key); //gets the first RAT
            if ((current.stories).contains(story1) && (current.stories).contains(story2)) {
                overlaps.add(current.username);
            }
        }
        
        return overlaps;
    }
    
    /**
     * Determines how many RATs "overlapped" in the most popular stories.
     * 
     * @return a string representation of the overlapping rats
     */
    public String overlappingRATs(Hashtable<String,Integer> hash) {
        Iterator<String> iterator = hash.keySet().iterator(); //iterates over the keys
        String[] storyIDs = new String[hash.size()];
        int storyIDSize = 0;
        
        while(iterator.hasNext()) {
            String key = iterator.next(); //gets the first RAT's key
            storyIDs[storyIDSize++] = key;
        }
        
        String output = "";
        
        for (String s1 : storyIDs) {
            LinkedList<String> currentOverlaps = new LinkedList<String>();
            output += "\n\nStory " + s1 + ":";
            for (String s2: storyIDs) {
                if (!s1.equals(s2)) {
                    currentOverlaps = compareRATs(s1, s2);
                    output += "\n   Story " + s2 + " (" + currentOverlaps.size() + " overlaps):\t" + currentOverlaps;
                }
            }
        }
        
        return output;
    }

    public static void main(String[] args) {
        Investigate i = new Investigate("All_Russian-Accounts-in-TT-stories.csv.tsv", 10);

        i.RATsPerStory();
        i.storiesPerRAT();

        //prints the total number of RATs and total number of stories
        System.out.println("Total number of RATs: " + i.g.usernamesSize);
        System.out.println("Total number of stories: " + i.g.stories.size());

        //calculates the median and average RATs-per-story and stories-per-RAT values
        i.normalDistribution(i.storiesPerRATValues);
        i.normalDistribution(i.RATsPerStoryValues);

        //figures out (and prints) the most and least active RATs, determines
        //what the most popular stories were about
        System.out.println("\nThe " + i.numPops + " most popular stories were:" 
            + i.mostPopularStories(i.numPops));
        System.out.println("\nThe most popular stories were about: ");
        TwitterTrails t = new TwitterTrails(i.mostPopStories);
        System.out.println("\nThe " + i.numPops + " least popular stories were:" 
            + i.leastPopularStories(i.numPops));

        //figures out (and prints) the most and least popular stories
        System.out.println("\nThe " + i.numPops + " most active RATs were:" 
            + i.mostActiveRATs(i.numPops));
        System.out.println("\nThe " + i.numPops + " least active RATs were:" 
            + i.leastActiveRATs(i.numPops));    

        i.getDiameter();
        i.getRadius();

        System.out.println("\nThe diameter of the graph is: " + i.diameter);
        System.out.println("\nThe radius of the graph is: " + i.radius);
        System.out.println("\nThe center nodes of the graph are: " + i.centerNodes);

        System.out.println("\nThe graph is connected: " + i.isConnected());
        
        System.out.println("How the 10 most popular stories overlapped with eachother:\n" + i.overlappingRATs(i.mostPopStories));
        
        i.g.exportAccounts("accounts.csv");
        i.g.exportStories("stories.csv");
    }
}
