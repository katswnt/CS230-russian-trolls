/**
 * Creates an adjacency list graph.
 *
 * @author Kathryn Swint
 * @version 12/04/2019
 */
package javafoundations;

import java.util.*;
import java.io.*;
import java.lang.StackOverflowError;
import java.text.Format;
import javafoundations.exceptions.*;
import java.lang.reflect.Array;

public class AdjListsGraph<T> implements Graph<T>{
    // instance variables
    protected Vector<T> vertices;
    protected Vector<LinkedList<T>> arcs;

    /**
     * Constructor for objects of class AdjListsGraph
     */
    public AdjListsGraph()
    {
        vertices = new Vector<T>();
        arcs = new Vector<LinkedList<T>>();
    }

    /**
     * Determines whether a graph is empty
     * 
     * @return a boolean indicating whether the graph is empty
     */
    public boolean isEmpty() 
    {
        return vertices.size() == 0;
    }

    /**
     * Checks the number of vertices in the graph.
     * 
     * @return an integer representation of the number of vertices
     */
    public int getNumVertices() {
        return vertices.size();
    }

    /**
     * Checks the total number of arcs in the graph.
     * 
     * @return an integer representation of the number of arcs
     */
    public int getNumArcs(){
        int total = 0;
        for (int i = 0; i < arcs.size(); i++) {
            total += arcs.get(i).size();
        }
        return total;
    }

    /**
     * Determines whether two vertices are connected by an arc.
     * 
     * @param v1 the first vertex
     * @param v2 the second vertex
     * @returns boolean indicating whether the two vertices are connected
     */
    public boolean isArc(T v1, T v2){
        boolean connected;
        if (vertices.contains(v1) && vertices.contains(v2)) {
            int origin = vertices.indexOf(v1); //beginning vertex
            int destination = vertices.indexOf(v2); //vertex to connect to
            connected = arcs.get(origin).contains(v2); //checks if they're connected
        } else {
            connected = false;
            System.out.println("Tried to add edge to one or more vertex that doesn't exist.");
        }
        return connected;
    }

    /**
     * Determines whether two vertices are connected by an edge.
     * 
     * @param v1 the first vertex
     * @param v2 the second vertex
     * @returns boolean indicating whether the two vertices are connected by an edge
     */
    public boolean isEdge(T v1, T v2){
        return (isArc(v1, v2) && isArc(v2, v1));
    }

    /**
     * Determines whether the graph is undirected.
     * 
     * @returns boolean indicating whether the graph is undirected
     */
    public boolean isUndirected(){
        for (int i = 0; i < arcs.size(); i++) {
            LinkedList<T> current = arcs.get(i);
            for (int j = 0; j < current.size(); j++) {
                // as soon as two vertices are found that are not connected by
                // an edge, returns false
                if (!isEdge(vertices.get(i), current.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Adds a vertex to the graph
     * 
     * @param v the vertex to be added
     */
    public void addVertex(T v){
        if (!vertices.contains(v)) {
            vertices.add(v);
            LinkedList<T> vertexEdges = new LinkedList<T>();
            arcs.add(vertexEdges);
        }
    }

    /**
     * Removes a vertex from the graph
     * 
     * @param v the vertex to be removed
     */
    public void removeVertex(T v){
        if (vertices.contains(v)) { //checks that the vertex is in the graph
            for (int i = 0; i < vertices.size(); i++) {
                // remove connections to the vertex from all other vertices
                removeArc(v, vertices.get(i));
                removeArc(vertices.get(i), v);
            }
            int index = vertices.indexOf(v); 
            vertices.remove(index);
            arcs.remove(index);
        } else {
            System.out.println("Tried to remove vertex that doesn't exist.");
        }
    }

    /**
     * Adds an arc between two vertices
     * 
     * @param v1 the origin vertex
     * @param v2 the destination vertex
     */
    public void addArc(T v1, T v2){
        // checks that both vertices exist
        if (vertices.contains(v1) && vertices.contains(v2)) {
            int origin = vertices.indexOf(v1);
            int destination = vertices.indexOf(v2);
            // checks if the vertices are already connected in the specified direction
            if (!arcs.get(origin).contains(v2)){
                arcs.get(origin).add(v2);
            }
        } else {
            System.out.println("Tried to add edge to one or more vertex that doesn't exist.");
        }
    }

    /**
     * Removes an arc between two vertices
     * 
     * @param v1 the origin vertex
     * @param v2 the destination vertex
     */
    public void removeArc(T v1, T v2){
        // checks that both vertices exist
        if (vertices.contains(v1) && vertices.contains(v2)) {
            int origin = vertices.indexOf(v1);
            int destination = vertices.indexOf(v2);
            arcs.get(origin).remove(v2);
        } else {
            System.out.println("Tried to remove edge from one or more vertex that doesn't exist.");
        }
    }

    /**
     * Adds an edge between two vertices
     * 
     * @param v1 the first vertex
     * @param v2 the second vertex
     */
    public void addEdge(T v1, T v2){
        addArc(v1, v2);
        addArc(v2, v1);
    }

    /**
     * Removes an edge between two vertices
     * 
     * @param v1 the first vertex
     * @param v2 the second vertex
     */
    public void removeEdge(T v1, T v2){
        removeArc(v1, v2);
        removeArc(v2, v1);
    }

    /**
     * Gets two levels of successors for a given vertex
     * 
     * @return a linked list with two levels of arcs
     * 
     * @param v the vertex to check 
     */
    public LinkedList<T> getArcs(T v) {
        LinkedList<T> allArcs = arcs.get(vertices.indexOf(v));
        LinkedList<T> temp = new LinkedList<T>();
        for (T vertex : allArcs) {
            temp.addAll(arcs.get(vertices.indexOf(vertex)));
        }
        return allArcs;
    }

    /**
     * Returns a linked list of the successors of vertex v.
     * Only checks the next level down from v.
     * 
     * @return a linked list with the successors of v
     * @param v the vertex you want the successors of
     */
    public LinkedList<T> getSuccessors(T v){
        LinkedList<T> successors = arcs.get(vertices.indexOf(v));
        return successors;
    }

    /**
     * Returns a linked list of the predecessors of vertex v.
     * Only checks the next level up from v.
     * 
     * @return a linked list with the predecessors of v
     * @param v the vertex you want the predecessors of
     */
    public LinkedList<T> getPredecessors(T v){
        LinkedList<T> predecessors = new LinkedList<T>();
        LinkedList<T> currentSuccessors;
        T currentVertex;

        for (int i = 0; i < vertices.size(); i++) {
            currentVertex = vertices.get(i);
            currentSuccessors = getSuccessors(currentVertex);
            if (currentSuccessors.contains(v)){
                predecessors.add(currentVertex);
            }
        }

        return predecessors;
    }

    /**
     * Creates a TGF file with the vertices and arcs of this graph.
     * 
     * @param fileName the name you want the file to be saved with
     */
    public void saveToTGF(String fileName){
        try {
            PrintWriter w = new PrintWriter(new File(fileName));
            String s = "";
            // prints all the vertices in the right format
            for (int i = 0; i < vertices.size(); i++) {
                s += (i + 1) + " " + vertices.get(i) + "\n";
            }

            s += "#";

            for (int i = 0; i < arcs.size(); i++) {
                LinkedList<T> current = arcs.get(i);
                for (T vertex : current) {
                    if (isEdge(vertices.get(i), vertex)) {
                        s += "\n" + i + " " + vertices.indexOf(vertex);
                    }
                }
            }

            w.println(s);
            w.close(); //for tidiness!
        }
        catch (IOException e) {
            System.out.println (e);
        }  
    }

    /**
     * Performs a breadth-first traversal of the graph, beginning at the
     * user-specificed vertex.
     * 
     * @return a linked list with all vertexes visited in the traversal
     * @param v the vertex you want to begin your traversal from
     */
    public LinkedList<T> BFtraversal(T v){
        LinkedList<T> path = new LinkedList<T>();
        LinkedList<T> checked = new LinkedList<T>();
        LinkedList<T> current = arcs.get(vertices.indexOf(v));
        LinkedQueue<T> queue = new LinkedQueue<T>();

        queue.enqueue(v);

        while (queue.size() != 0) {
            if (!checked.contains(v)) {
                for (T vertex : arcs.get(vertices.indexOf(v))) {
                    queue.enqueue(vertex);
                }
                checked.add(v);
            }

            T currentVertex = queue.dequeue();

            if (!checked.contains(currentVertex)) {
                for (T vertex : arcs.get(vertices.indexOf(currentVertex))) {
                    queue.enqueue(vertex);
                }
                checked.add(currentVertex);
            } 

            if (!path.contains(currentVertex)) {
                path.add(currentVertex);
            }
        }

        return path;
    }

    /**
     * Performs a depth-first traversal of the graph, beginning at the
     * user-specificed vertex.
     * 
     * @return a linked list with all vertexes visited in the traversal
     * @param v the vertex you want to begin your traversal from
     */
    public LinkedList<T> DFtraversal(T v)
    {
        int startIndex = vertices.indexOf(v);
        T currentVertex; 
        LinkedStack<T> traversalStack = new LinkedStack<T>();
        ArrayIterator<T> iter = new ArrayIterator<T>(); 
        boolean[] visited = new boolean[vertices.size()]; 
        boolean found;
        LinkedList<T> results = new LinkedList<T>();

        if (!vertices.contains(v))
            return results;

        for (int vertexIdx = 0; vertexIdx < vertices.size(); vertexIdx++)
            visited[vertexIdx] = false;

        traversalStack.push(vertices.get(startIndex)); 
        iter.add(vertices.get(startIndex)); 
        visited[startIndex] = true;

        while (!traversalStack.isEmpty()) {
            currentVertex = traversalStack.peek(); 
            found = false;
            for (int vertexIdx = 0; vertexIdx < vertices.size() && !found; vertexIdx++)
                if (isArc((currentVertex),(vertices.get(vertexIdx))) && !visited [vertexIdx]) {
                    traversalStack.push(vertices.get(vertexIdx)); 
                    iter.add(vertices.get(vertexIdx)); 
                    visited[vertexIdx] = true; 
                    found = true;
                }
            if (!found && !traversalStack.isEmpty()) 
                traversalStack.pop();
        }

        for (T element : iter.toArray()) {
            if (element != null) {
                results.add(element);
            }
        }

        return results;
    }

    /**
     * Standard toString method
     * 
     * @return a string representation of the graph
     */
    public String toString() {
        String result = "Vertices:\n" + vertices + "\nEdges:\n";
        for (int i = 0; i < vertices.size(); i++) {
            result += "from " + vertices.get(i) + ":\t" + arcs.get(i) + "\n";
        }
        return result;
    }

    public static void main(String args[]) {
        AdjListsGraph<String> g = new AdjListsGraph<String>();

        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addVertex("D");
        g.addVertex("E");
        g.addVertex("F");
        g.addVertex("G");
        g.addVertex("H");
        g.addVertex("I");
        g.addVertex("J");
        g.addVertex("K");
        g.addVertex("L");
        g.addVertex("M");
        g.addVertex("N");
        g.addVertex("O");
        g.addVertex("P");
        g.addVertex("Q");
        g.addVertex("R");
        g.addVertex("S");
        g.addVertex("T");
        g.addVertex("U");
        g.addVertex("V");
        g.addVertex("W");
        g.addVertex("X");
        g.addVertex("Y");
        g.addVertex("Z");
        g.addVertex("1");
        g.addVertex("2");
        g.addVertex("3");
        g.addVertex("4");
        g.addVertex("5");

        // g.removeVertex("P");
        // g.removeVertex("J");
        // g.removeVertex("G");
        // g.removeVertex("L");
        // g.removeVertex("Q");
        // g.removeVertex("Z");

        g.addArc("A", "B");
        g.addArc("A", "C");
        g.addArc("B", "D");
        g.addArc("B", "E");
        g.addArc("C", "F");
        g.addArc("C", "G");
        g.addArc("D","H");
        g.addArc("D","I");
        g.addArc("E","J");
        g.addArc("E","K");
        g.addArc("F","L");
        g.addArc("F","M");
        g.addArc("G","N");
        g.addArc("G","O");
        g.addArc("H","P");
        g.addArc("H","Q");
        g.addArc("I","R");
        g.addArc("I","S");
        g.addArc("J","T");
        g.addArc("J","U");
        g.addArc("K","V");
        g.addArc("K","W");
        g.addArc("L","X");
        g.addArc("L","Y");
        g.addArc("M","Z");
        g.addArc("M","1");
        g.addArc("N","2");
        g.addArc("N","3");
        g.addArc("O","4");
        g.addArc("O","5");

        // g.addEdge("C", "D");
        // g.addEdge("C", "E");
        // g.addArc("D", "E");
        // g.addArc("D", "E");
        // g.addArc("D", "F");
        // g.addArc("D", "G");
        // g.addArc("F", "I");
        // g.addArc("I", "K");
        // g.addArc("K", "M");
        // g.addArc("M", "N");
        // g.addArc("M", "D");
        // g.addArc("O", "M");
        // g.addArc("O", "T");
        // g.addArc("H", "R");
        // g.addArc("R", "T");
        // g.addArc("T", "A");
        // g.addArc("S", "A");
        // g.addArc("A", "S");
        // g.addArc("N", "K");
        // g.addArc("T", "K");
        // g.addArc("S", "M");
        // g.addArc("F", "N");
        // g.addArc("F", "D");
        // g.addArc("H", "M");
        // g.addArc("H", "T");
        // g.addArc("I", "R");
        // g.addArc("I", "T");
        // g.addArc("K", "A");
        // g.addArc("O", "A");
        // g.addArc("K", "S");
        // g.addArc("O", "F");

        // System.out.println("\nTesting isArc() with A to B: \t" + g.isArc("A", "B"));
        // System.out.println("Testing isArc() with A to C: \t" + g.isArc("A", "C"));
        // System.out.println("Testing isArc() with A to F: \t" + g.isArc("A", "F"));
        // System.out.println("Testing isArc() with I to F: \t" + g.isArc("I", "F"));

        // System.out.println("\nTesting isEdge() with A and B:\t" + g.isEdge("A", "B"));
        // System.out.println("Testing isEdge() with B and E:\t" + g.isEdge("B", "E"));
        // System.out.println("Testing isEdge() with H and R:\t" + g.isEdge("H", "R"));

        //System.out.println("\nTesting isUndirected():\t" + g.isUndirected());

        System.out.println("\n" + g);

        System.out.println("Testing getPredecessors() with G:\t" + g.getPredecessors("G"));
        System.out.println("\nTesting getSuccessors() with A: \t" + g.getSuccessors("A"));

        //System.out.println("\n" + g);

        //System.out.println(g.arcs.get(4));

        System.out.println("\nTesting BFtraversal() with A:\t" + g.BFtraversal("A"));

        System.out.println("\nTesting DFtraversal() with A:\t" + g.DFtraversal("A"));

        //g.saveToTGF("GraphOutput.tgf");
    }
}
