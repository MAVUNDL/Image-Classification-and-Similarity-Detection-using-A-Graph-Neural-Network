package BackEnd.GraphStructure.Interfaces;

import BackEnd.DataStructures.Interfaces.ArrayList;


public interface Vertex<V, E> {
    /**
     * @return This method returns the element stored by the vertex
     */
    V element();

    /**
     * This method set the element stored by the vertex
     * @param element the element
     */
    void  setElement(V element);

    /**
     * This method adds an edge between the current vertex and the new vertex
     * @param endVertex the new vertex
     * @param edgeWeight the weight of the edge
     */
    void addEdge(Vertex<V, E> endVertex, E edgeWeight);

    /**
     * This method removes the edge between the current vertex and the given vertex
     * @param endVertex the given vertex
     */
    void removeEdge(Vertex<V,E> endVertex);

    /**
     @return returns all the edges as a collection associated with the vertex
     */
    ArrayList<Edge<V, E>> getEdges();
}

