package FrontEnd.classes;

import BackEnd.GraphStructure.Interfaces.Edge;
import FrontEnd.interfaces.EdgeUI;
import FrontEnd.interfaces.LabelUI;
import FrontEnd.interfaces.VertexUI;
import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class EdgeNode<V,E> extends Group implements EdgeUI<V,E> {
    private final Edge<V,E> edge;
    private final VertexUI<V,E> first;
    private final VertexUI<V,E> last;
    private final Line line;

    /**
     * This constructor creates an edge between two vertices on the GUI
     * @param edge the underlying edge connecting the two vertices
     * @param first the first vertex
     * @param last the second vertex
     */
    public EdgeNode(Edge<V,E> edge, VertexUI<V,E> first, VertexUI<V,E> last){
        this.edge = edge;
        this.first = first;
        this.last = last;
        this.line = new Line();
        bindEdgeWithVertices();
        this.getChildren().addAll(line); // Add both Line and Label to Group
    }

    /**
     * This method binds the line properties joining the two vertices
     */
    @Override
    public void bindEdgeWithVertices() {
        line.startXProperty().bind(first.getXProperty());
        line.startYProperty().bind(first.getYProperty());
        line.endXProperty().bind(last.getXProperty());
        line.endYProperty().bind(last.getYProperty());
    }

    /**
     * @return returns the underlying edge from the original graph data structure
     */
    @Override
    public Edge<V, E> underlyingEdge() {
        return edge;
    }

    /**
     * Styles the edge using the given color
     * @param color css style
     */
    @Override
    public void StyleEdge(Color color) {
        line.setStroke(color);
        line.setStrokeWidth(2);
    }

    @Override
    public Node getStyleableNode() {
        return this;
    }
}
