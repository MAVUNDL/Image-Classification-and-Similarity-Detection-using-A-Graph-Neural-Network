package app.BackEnd.GraphStructure.Interfaces;

import app.BackEnd.DataStructures.Interfaces.ArrayList;

public interface Graph<V,E> {
    /**
     * @return returns the number of vertices on the graph
     */
    int vertices();

    /**
     * @return returns the number of edges on the graph
     */
    int edges();

    /**
     * This method checks if there exists an edge between these two elements
     * @param start the element that will be at the start of the edge
     * @param end the element that will be at the end of the edge
     * @return returns true if there exists an edge between else false
     */
    boolean areAdjacent(V start, V end);

    /**
     * This method takes the element and creates a vertex and adds it to the graph
     * @param element the element to be stored on the vertex
     * @return returns the vertex created to store the element on the graph
     */
    Vertex<V,E> addVertex(V element) throws IllegalArgumentException;

    /**
     * This method removes the vertex that contains the given element
     * @param element the given element
     * @return returns the removed vertex from the graph
     */
    Vertex<V, E> removeVertex(V element) throws RuntimeException ;

    /**
     * This method takes two elements and creates an edge between with the given weight
     * @param start the element that will be at the start of the edge
     * @param end the element that will be at the end of the edge
     * @param weight the weight of the edge between the elements
     * @return returns the created edge that connects the two elements on the graph
     */
    Edge<V,E> addEdge(V start, V end, E weight);

    /**
     * This method removes the edge that connects the two elements
     * @param start the element that will be at the start of the edge
     * @param end the element that will be at the end of the edge
     * @return returns the removed edge from the graph that connected the two elements
     */
    Edge<V,E> removeEdge(V start, V end) throws RuntimeException;

    /**
     * @return returns a collection of the vertices on the graph
     */
    ArrayList<Vertex<V,E>> getVertices();

    /**
     * @return returns a collection of the edges on the graph
     */
    ArrayList<Edge<V,E>> getEdges();

    /**
     * This method retrieves the vertex that store this element on the graph
     * @param element the element
     * @return returns the vertex storing this element
     */
    Vertex<V,E> getVertex(V element);

    /**
     * This method returns the edge that is weighted by this value
     * @param weight weight value
     * @return returns the edge weighted by this value on the graph
     */
    Edge<V,E> getEdge(E weight);
}
