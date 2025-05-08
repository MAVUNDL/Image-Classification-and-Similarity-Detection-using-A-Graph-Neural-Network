package app.BackEnd.GraphStructure.Classes;

import app.BackEnd.DataStructures.Classes.ArrayListDS;
import app.BackEnd.DataStructures.Interfaces.ArrayList;
import app.BackEnd.GraphStructure.Interfaces.Edge;
import app.BackEnd.GraphStructure.Interfaces.Vertex;

import java.util.Iterator;

public class GraphVertex<V,E> implements Vertex<V,E> {
    private V element;
    private final ArrayList<Edge<V, E>> edgeList;

    /**
     * This constructor creates a vertex for the given element
     * @param element the given element
     */
    public GraphVertex(V element){
        this.element = element;
        this.edgeList = new ArrayListDS<>();
    }

    /**
     * @return This method returns the element stored by the vertex
     */
    @Override
    public V element() {
        return this.element;
    }

    /**
     * This method set the element stored by the vertex
     *
     * @param element the element
     */
    @Override
    public void setElement(V element) {
        this.element = element;
    }

    /**
     * This method adds an edge between the current vertex and the new vertex
     *
     * @param endVertex  the new vertex
     * @param edgeWeight the weight of the edge
     */
    @Override
    public void addEdge(Vertex<V, E> endVertex, E edgeWeight) {
        this.edgeList.add(new GraphEdge<V,E>(this, endVertex, edgeWeight));
    }

    /**
     * This method removes the edge between the current vertex and the given vertex
     *
     * @param endVertex the given vertex
     */
    @Override
    public void removeEdge(Vertex<V, E> endVertex) {
        Iterator<Edge<V,E>> iterator = this.edgeList.iterator();
        while (iterator.hasNext()){
            Edge<V,E> edge = iterator.next();
            if(edge.contains(endVertex)){
                this.edgeList.remove(edge);
            }
        }
    }

    /**
     * @return returns all the edges as a collection associated with the vertex
     */
    @Override
    public ArrayList<Edge<V, E>> getEdges() {
        return this.edgeList;
    }
}
