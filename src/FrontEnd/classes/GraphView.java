package FrontEnd.classes;

import BackEnd.DataStructures.Classes.ArrayListDS;
import BackEnd.DataStructures.Classes.MapDS;
import BackEnd.DataStructures.Classes.QueueDS;
import BackEnd.DataStructures.Interfaces.ArrayList;
import BackEnd.DataStructures.Interfaces.Map;
import BackEnd.DataStructures.Interfaces.Queue;
import BackEnd.GraphStructure.Interfaces.Edge;
import BackEnd.GraphStructure.Interfaces.Graph;
import BackEnd.GraphStructure.Interfaces.Vertex;
import FrontEnd.interfaces.EdgeUI;
import FrontEnd.interfaces.VertexUI;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Iterator;
import java.util.Random;

/**
 * This class defines a Pane where the graph will be drawn
 * @param <V> type parameter for the vertices
 * @param <E> type parameter for the edges
 */
public class GraphView<V,E>  extends Pane {
    private final Graph<V,E> KNNGraph;
    private final Map<Vertex<V,E>, VertexUI<V,E>> vertexUIMap;
    private final Map<Edge<V,E>, EdgeUI<V,E>> edgeUIMap;
    private final Queue<Color> colorQueue;

    /**
     * This constructor creates a graph view where the KNN graph will be drawn on the UI
     * @param graph the KNN graph
     */
    public GraphView(Graph<V,E> graph){
        this.KNNGraph = graph;
        this.vertexUIMap = new MapDS<>();
        this.edgeUIMap = new MapDS<>();
        this.colorQueue = new QueueDS<>();
        //this.setStyle("-fx-background-color: #F4FFFB;");
        //this.getStyleClass().add("graph");
        //this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("graph.css")).toExternalForm());
    }

    /**
     * This method build the KNN graph and places the vertices is such a way that it forms a full circle
     * @param width the width of the area to plot the graph
     * @param height the height of the area to plot the graph
     */
    public void buildGraph(double width, double height){
        Random random = new Random();
        ArrayList<Vertex<V,E>> vertices = KNNGraph.getVertices(); // get all vertices from the graph
        // compute dimensions
        double centerX = width / 2.0;
        double centerY = height / 2.0;
        double radius = Math.min(width, height) / 2.0 * 0.97; // 97% of half the smallest dimension
        createColors(vertices.size());

        // for each vertex in the graph
        for(int i = 0; i < vertices.size(); i++){
            Vertex<V,E> vertex = vertices.get(i); // get current vertex
            // Calculate angle starting from the top 90 degrees
            double angle = (2 * Math.PI * i) / vertices.size() - Math.PI / 2;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);

            // create UI node
            VertexUI<V,E> vertexUI = new VertexNode<>(vertex, x, y, 8, generateVertexColor());
            vertexUIMap.put(vertex, vertexUI); // store
            this.getChildren().add((Node) vertexUI); // add to GUI
        }

        // Now building Edges between
        for(int i = 0; i < vertices.size(); i++){
            Vertex<V,E> vertex = vertices.get(i); // get current vertex
            VertexUI<V,E> vertexUI = vertexUIMap.get(vertex); // get UI node
            ArrayList<Edge<V,E>> adjacencyList = vertex.getEdges(); // get adjacency list

            for(int j = 0; j < adjacencyList.size(); j++){
                Edge<V,E> edge = adjacencyList.get(j); // get connecting edge
                Vertex<V, E> adjacentVertex = edge.getEndVertex();
                VertexUI<V,E> neighbor = vertexUIMap.get(adjacentVertex);

                if(neighbor != null){
                    EdgeUI<V,E> edgeUI = new EdgeNode<>(edge, vertexUI, neighbor); // daw edge
                    edgeUI.StyleEdge(vertexUI.getColor());
                    edgeUIMap.put(edge, edgeUI); // store
                    this.getChildren().add((Node) edgeUI); // show on UI
                }
            }
        }
    }

    /**
     * @return returns a random color
     */
    private Color generateVertexColor(){
        return colorQueue.dequeue();
    }

    /**
     * This method generate a random color for each vertex and ensures its unique
     * @param numb number of vertices
     */
    private void createColors(int numb) {
        Random random = new Random();
        ArrayList<Color> colorList = new ArrayListDS<>();

        while (colorList.size() < numb) {
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);
            Color newColor = Color.rgb(red, green, blue);

            boolean exists = false;
            Iterator<Color> iterator = colorList.iterator();
            while (iterator.hasNext()){
                Color color = iterator.next();
                if (color.equals(newColor)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                colorList.add(newColor);
                colorQueue.enqueue(newColor);
            }
        }
    }
}
