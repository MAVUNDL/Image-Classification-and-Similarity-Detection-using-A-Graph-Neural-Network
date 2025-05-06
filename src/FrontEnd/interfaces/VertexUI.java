package FrontEnd.interfaces;

import BackEnd.GraphStructure.Interfaces.Vertex;
import FrontEnd.classes.VertexNode;
import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;

public interface VertexUI<V,E> {
    /**
     * This method sets the position of the vertex on the UI
     * @param x x coordinate
     * @param y y coordinate
     */
    void setPosition(double x, double y);

    /**
     * This method sets the radius of the circle representing the vertex
     * @param r radius of the circle
     */
    void setVertexRadius(double r);

    /**
     * @return returns the x coordinate of the vertex
     */
    double getXPosition();

    /**
     * @return returns the y coordinate of the vertex
     */
    double getYPosition();

    /**
     * @return returns the property representing the x-position  of the vertex
     */
    DoubleProperty getXProperty();

    /**
     * @return returns the property representing the y-position  of the vertex
     */
    DoubleProperty getYProperty();

    /**
     * @return returns the property representing the radius of the circle representing the vertex
     */
    DoubleProperty getRadiusProperty();

    /**
     * @return returns the underlying vertex from the graph Data structure
     */
    Vertex<V, E> getUnderlyingVertex();

    /**
     * This method add a css styling to the vertex
     * @param css css style
     */
    void setStyling(String css);

    /**
     * This method add the adjacent vertex on to this one to the list
     * @param vertex the adjacent vertex
     */
    void addAdjacentVertex(VertexNode<V,E> vertex);

    /**
     * @return returns the color of the circle
     */
    Color getColor();
}
