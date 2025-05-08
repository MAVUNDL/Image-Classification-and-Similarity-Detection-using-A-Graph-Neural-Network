package app.BackEnd.GraphStructure.Classes;

import app.BackEnd.DataStructures.Classes.ArrayListDS;
import app.BackEnd.DataStructures.Classes.MapDS;
import app.BackEnd.DataStructures.Interfaces.ArrayList;
import app.BackEnd.DataStructures.Interfaces.Map;
import app.BackEnd.GraphStructure.Interfaces.Edge;
import app.BackEnd.GraphStructure.Interfaces.Graph;
import app.BackEnd.GraphStructure.Interfaces.Vertex;

import java.util.Iterator;

public class GraphStructure<V,E>  implements Graph<V,E> {
    private final Map<V, Vertex<V,E>> vertexMap;
    private final Map<E, Edge<V,E>> eEdgeMap;

    /**
     * This constructor creates a new graph
     */
    public GraphStructure(){
        this.vertexMap = new MapDS<>();
        this.eEdgeMap = new MapDS<>();
    }

    /**
     * @return returns the number of vertices on the graph
     */
    @Override
    public int vertices() {
        return this.vertexMap.size();
    }

    /**
     * @return returns the number of edges on the graph
     */
    @Override
    public int edges() {
        return this.eEdgeMap.size();
    }

    /**
     * This method checks if there exists an edge between these two elements
     *
     * @param start the element that will be at the start of the edge
     * @param end   the element that will be at the end of the edge
     * @return returns true if there exists an edge between else false
     */
    @Override
    public boolean areAdjacent(V start, V end) {
        Vertex<V,E> startVertex = this.vertexMap.get(start);
        Vertex<V,E> endVertex = this.vertexMap.get(end);
        if(startVertex == null || endVertex == null) return false;
        return retrieveEdge(startVertex, endVertex) != null;
    }

    /**
     * This method takes the element and creates a vertex and adds it to the graph
     *
     * @param element the element to be stored on the vertex
     * @return returns the vertex created to store the element on the graph
     */
    @Override
    public Vertex<V, E> addVertex(V element) throws IllegalArgumentException{
        if(element == null){
            throw new IllegalArgumentException("Vertex cannot be null");
        }

        if(this.vertexMap.containsKey(element)){
            throw new IllegalArgumentException("Vertex already exits on the graph");
        }
        Vertex<V,E> vertex = new GraphVertex<>(element);
        this.vertexMap.put(vertex.element(), vertex);
        return vertex;
    }

    /**
     * This method removes the vertex that contains the given element
     *
     * @param element the given element
     * @return returns the removed vertex from the graph
     */
    @Override
    public Vertex<V, E> removeVertex(V element) throws RuntimeException {
        Vertex<V,E> vertex = this.vertexMap.get(element);
        if(vertex == null){
            throw new RuntimeException("This vertex does not exist on the graph");
        }
        Iterator<Edge<V,E>> iterator = vertex.getEdges().iterator();
        while (iterator.hasNext()){
            Edge<V,E> edge = iterator.next();
            this.eEdgeMap.remove(edge.getWeight());
            Vertex<V,E> opp = edge.getEndVertex();
            opp.getEdges().remove(edge);
        }
        this.vertexMap.remove(vertex.element());
        return vertex;
    }

    /**
     * This method takes two elements and creates an edge between with the given weight
     *
     * @param start  the element that will be at the start of the edge
     * @param end    the element that will be at the end of the edge
     * @param weight the weight of the edge between the elements
     * @return returns the created edge that connects the two elements on the graph
     */
    @Override
    public Edge<V, E> addEdge(V start, V end, E weight) {
        Vertex<V,E> startVertex = this.vertexMap.get(start);
        Vertex<V,E> endVertex = this.vertexMap.get(end);
        if(startVertex == null || endVertex == null){
            throw new RuntimeException("One of or both the vertices does not exist on the graph");
        }
        Edge<V,E> edge = new GraphEdge<>(startVertex, endVertex, weight);
        this.eEdgeMap.put(edge.getWeight(), edge);
        startVertex.getEdges().add(edge);
        endVertex.getEdges().add(edge);
        return edge;
    }

    /**
     * This method removes the edge that connects the two elements
     *
     * @param start the element that will be at the start of the edge
     * @param end   the element that will be at the end of the edge
     * @return returns the removed edge from the graph that connected the two elements
     */
    @Override
    public Edge<V, E> removeEdge(V start, V end) throws RuntimeException{
        Vertex<V,E> startVertex = this.vertexMap.get(start);
        Vertex<V,E> endVertex = this.vertexMap.get(end);
        if(startVertex == null || endVertex == null){
            throw new RuntimeException("One of or both the vertices does not exist on the graph");
        }
        Edge<V,E> edge = retrieveEdge(startVertex, endVertex);
        if(edge == null){
            throw new RuntimeException("There is no edge on the graph that connects the these vertices");
        }
        this.eEdgeMap.remove(edge.getWeight());
        return edge;
    }

    /**
     * @return returns a collection of the vertices on the graph
     */
    @Override
    public ArrayList<Vertex<V, E>> getVertices() {
        ArrayList<Vertex<V,E>> list = new ArrayListDS<>();
        Iterator<Vertex<V,E>> iterator = this.vertexMap.values();
        while (iterator.hasNext()){
            Vertex<V,E> vertex = iterator.next();
            list.add(vertex);
        }
        return list;
    }

    /**
     * @return returns a collection of the edges on the graph
     */
    @Override
    public ArrayList<Edge<V, E>> getEdges() {
        ArrayList<Edge<V,E>> list = new ArrayListDS<>();
        Iterator<Edge<V,E>> iterator = this.eEdgeMap.values();
        while (iterator.hasNext()){
            Edge<V,E> edge = iterator.next();
            list.add(edge);
        }
        return list;
    }

    /**
     * This method retrieves the vertex that store this element on the graph
     *
     * @param element the element
     * @return returns the vertex storing this element
     */
    @Override
    public Vertex<V, E> getVertex(V element) {
        return this.vertexMap.get(element);
    }

    /**
     * This method returns the edge that is weighted by this value
     *
     * @param weight weight value
     * @return returns the edge weighted by this value on the graph
     */
    @Override
    public Edge<V, E> getEdge(E weight) {
        return this.eEdgeMap.get(weight);
    }

    /**
     * This method retrieves the edge that connects the two vertices on th graph
     * @param start start vertex
     * @param end end vertex
     * @return returns the edge that connects the two vertices
     */
    private Edge<V,E> retrieveEdge(Vertex<V,E> start, Vertex<V,E> end){
        Iterator<Edge<V,E>> iterator = this.eEdgeMap.values();
        while (iterator.hasNext()){
            Edge<V,E> edge = iterator.next();
            if(edge.getStart().equals(start) && edge.getEndVertex().equals(end)){
                return edge;
            }
        }
        return null;
    }

}
