package app.BackEnd.GraphStructure.Classes;

import app.BackEnd.GraphStructure.Interfaces.Edge;
import app.BackEnd.GraphStructure.Interfaces.Vertex;

public class GraphEdge<V, E> implements Edge<V,E> {
    private E weight;
    private Vertex<V,E> start;
    private Vertex<V,E> end;

    /**
     * This constructor creates a new edge between the given vertices with the given weight
     * @param start the vertex that will be at the start of the edge
     * @param end the vertex that will be at the end of the edge
     * @param weight the weight of the edge
     */
    public GraphEdge(Vertex<V,E> start, Vertex<V,E> end, E weight){
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    /**
     * @return returns the weight of the edge
     */
    @Override
    public E getWeight() {
        return this.weight;
    }

    /**
     * @return returns the vertex at the start of the edge
     */
    @Override
    public Vertex<V, E> getStart() {
        return this.start;
    }

    /**
     * @return returns the vertex at the end of the edge
     */
    @Override
    public Vertex<V, E> getEndVertex() {
        return this.end;
    }

    /**
     * This method sets the weight for the edge
     *
     * @param weight value for the weight
     */
    @Override
    public void setWeight(E weight) {
        this.weight = weight;
    }

    /**
     * This method sets the vertex to be at the start of the edge
     *
     * @param startVertex the vertex at the start of the vertex
     */
    @Override
    public void setStart(Vertex<V, E> startVertex) {
        this.start = startVertex;
    }

    /**
     * This method sets the vertex to be at the end of the edge
     *
     * @param endVertex the vertex to be at the end of the edge
     */
    @Override
    public void setEnd(Vertex<V, E> endVertex) {
        this.end = endVertex;
    }

    /**
     * This method checks if the given vertex exists on the edge
     *
     * @param vertex the given vertex
     * @return returns true if it belongs to the edge else false
     */
    @Override
    public boolean contains(Vertex<V, E> vertex) {
        return (this.start.equals(vertex) || this.end.equals(vertex));
    }
}
