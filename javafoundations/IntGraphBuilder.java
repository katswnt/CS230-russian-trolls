/**
 * IntGraphBuilder.java
 * 
 * @author: Ben Wood
 * 
 * @version 11/2019
 * Stella added complete javadoc
 * 
 * Purpose: a simple example on how to use the GraphBuilder abstarct class,
 * to create a graph of Strings, reading its vertices and arcs from a TGF file
 * 
 */
package javafoundations;

public class IntGraphBuilder extends GraphBuilder<Integer>{
    /**
     * Takes a line and simply returns it as a string.
     * Since the graph contains simply strings, trivially,
     * a String is created, and returned,out or the inputted string.
     * 
     * */
    
    public Integer createOneThing(String s) {
        return new Integer(s); 
    }
}
