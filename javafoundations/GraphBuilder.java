/**
 * GraphBuilder.java
 * 
 * @author Ben Wood
 * 
 * @version 4/11/2019
 * Stella added complete javadoc
 */

package javafoundations;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/* 
 * When we want to create a graph of some specific object type,
 * we need to extend this abstarct class. The child class will have to 
 * provide implementation for the "createOneThing()" method.
 * */
abstract class GraphBuilder<T> {

    /*
     * To be overriden in the extension of this class 
     * (like the PersonGraphBuilder, for example).
     * It will create and return an object of the specific 
     * type the graph will contain, from the input string s.
     * 
     * @param the String from which an object, of the specific type
     * the graph contains, will be created.
     * @return the object that was created. It is of the specific type 
     * the graph contains.
     * 
     * */
    abstract T createOneThing(String s);

    /*
     * Reads from the input .tgf file, line by line.
     * Creates the vertex objects, and adds them to the graph.
     * Then, adds the connections between the vertices.
     * 
     * PRECONDITION: the input file is in the TGF format
     * 
     * @param the file name to read from
     * @return the constructed graph, based on the input TGF file.
     * 
     * */
    public AdjListsGraph<T> build (String fileName) {
        //create an empty graph
        AdjListsGraph<T> g = new AdjListsGraph<T>();
        //open the file for reading
        try{ // to read from the tgf file
            Scanner scanner = new Scanner(new File(fileName));

            //read vertices
            while ( !scanner.next().equals("#")) {
                String line = "";
                //read a line as String
                line = scanner.nextLine().trim();
                //create an object of the specific type the graph contains,
                //based on the read string
                T thing = createOneThing(line);
                //add that object to the graph as a new vertex
                g.addVertex(thing); 
            }

            //read arcs
            while (scanner.hasNext()){
                //read from to vertices, in one line as integers
                String from = scanner.next();
                String to = scanner.next();
                g.addArc(createOneThing(from), createOneThing(to));
            }

            scanner.close();
        } catch (IOException ex) {
            System.out.println(fileName + " ***ERROR*** The file was not found: " + ex);
        }

        return g;

    }

}