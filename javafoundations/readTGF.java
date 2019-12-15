
/**
 * Write a description of class readTGF here.
 *
 * @author Kathryn Swint and Efua Akonor
 * @version 12/05/2019
 */

package javafoundations;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class readTGF<T>
{
    // instance variables - replace the example below with your own
    private AdjListsGraph<T> g = new AdjListsGraph<T>();

    // /**
     // * Constructor for objects of class readTGF
     // */
    // public readTGF(String inputFile)
    // {
        // // initialise instance variables
        // try{
            // Scanner scan = new Scanner(new File(inputFile));
            
            // while (scan.hasNext() && !scan.nextLine().equals("#")) {
                // T input = scan.nextLine();
                // g.addVertex(input);
            // }
            
            // T throwaway = scan.nextLine();
            
            // while (scan.hasNext() && !scan.nextLine().equals("#")) {
                // T[] input = scan.nextLine().split(",");
                // g.addArc(input[0], input[1]);
            // }
            
            // scan.close();
        // } catch (FileNotFoundException ex) {
            // System.out.println("File " + inputFile + " was not found.");
        // }
    // }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public AdjListsGraph<T> sampleMethod()
    {
        // put your code here
        return g;
    }
}
