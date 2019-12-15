/**
 * Represents (and constructs) a single RAT object.
 *
 * @author Kathryn Swint
 * @version 12/05/2019
 */

import java.util.LinkedList;

public class RAT
{
    // instance variables
    protected String username; 
    protected String userID;
    protected long tweetCount;
    protected long storyCount;
    protected LinkedList<String> stories;

    /**
     * Constructor for objects of class RAT
     * 
     * @param u the RAT's username
     * @param uID the RAT's userID
     * @param t the RAT's tweetCount
     * @param s the RAT's storyCount
     */
    public RAT(String u, String uID, long t, long s)
    {
        this.username = u;
        this.userID = uID;
        this.tweetCount = t;
        this.storyCount = s;
        stories = new LinkedList<String>();
    }

    /**
     * Adds a story to the RAT's LinkedList of stories.
     * 
     * @param story the story to be added
     */
    protected void addStory(String story) {
        try {
            if (!stories.contains(story)) {
                stories.add(story); //avoiding adding duplicate stories
            } 
        } catch (NullPointerException ex) {
            System.out.println(ex + " for story " + story);
        }       
    }

    /**
     * Creates a nicely formatted string representation of a RAT object.
     * 
     * @return a string representation of a RAT object
     */
    public String toString() {
        String allStories = "";
        for (String s : stories) {
            allStories += s + ", ";
        }
        allStories = allStories.substring(0, allStories.length()-3); //removes the last "," and space
        return username + "\t" + userID + "\t" 
        + tweetCount + "\t" + storyCount + "\t" + allStories; //nicely formatted string
    }

    public static void main(String[] args) {
    }
}
