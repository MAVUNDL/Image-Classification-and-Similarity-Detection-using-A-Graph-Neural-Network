package FrontEnd.interfaces;

import BackEnd.GraphStructure.Interfaces.Edge;
import javafx.scene.paint.Color;

public interface EdgeUI<V,E> {
    /**
     * This method binds the line properties joining the two vertices
     */
    void bindEdgeWithVertices();

    /**
     * @return returns the underlying edge from the original graph data structure
     */
    Edge<V,E> underlyingEdge();

    /**
     * This method style the edge using the given color
     * @param color css style
     */
    void StyleEdge(Color color);

}
