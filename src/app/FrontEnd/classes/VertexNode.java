package app.FrontEnd.classes;

import app.BackEnd.DataStructures.Classes.ArrayListDS;
import app.BackEnd.DataStructures.Interfaces.ArrayList;
import app.BackEnd.GraphStructure.Interfaces.Vertex;
import app.FrontEnd.interfaces.VertexUI;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class VertexNode<V,E> extends Circle implements VertexUI<V,E> {
    private final Vertex<V,E> vertex;
    private final DoubleProperty xPosition;
    private final DoubleProperty yPosition;
    private final DoubleProperty radius;
    private final ArrayList<VertexNode<V,E>> adjacencyList;
    private final Color color;

    /**
     * This constructor creates a new node for the vertex in the UI
     * @param vertex actual vertex node from the graph data structure to be represented on the GUi
     * @param x x coordinate for the node on the UI
     * @param y y coordinate for the node on the UI
     * @param radius radius for the circle shape which represents the vertex
     */
    public VertexNode(Vertex<V,E> vertex, double x, double y, double radius, Color color){
       super(x, y, radius, color);
       this.color = color;
       this.vertex = vertex;
       this.xPosition = new SimpleDoubleProperty();
       this.yPosition = new SimpleDoubleProperty();
       this.radius = new SimpleDoubleProperty();
       this.adjacencyList = new ArrayListDS<>();
       bindProperties();
    }

    /**
     * This method bind the n properties of the vertex's dimensions with shape of the vertex's dimensions
     */
    private void bindProperties(){
        xPosition.bindBidirectional(this.centerXProperty());
        yPosition.bindBidirectional(this.centerYProperty());
        radius.bindBidirectional(this.radiusProperty());
    }

    /**
     * This method sets the position of the vertex on the UI
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    @Override
    public void setPosition(double x, double y) {
        this.setCenterX(x);
        this.setCenterY(y);
    }

    /**
     * This method sets the radius of the circle representing the vertex
     *
     * @param r radius of the circle
     */
    @Override
    public void setVertexRadius(double r) {
        this.setRadius(r);
    }

    /**
     * @return returns the x coordinate of the vertex
     */
    @Override
    public double getXPosition() {
        return this.getCenterX();
    }

    /**
     * @return returns the y coordinate of the vertex
     */
    @Override
    public double getYPosition() {
        return this.getCenterY();
    }

    /**
     * @return returns the property representing the x-position  of the vertex
     */
    @Override
    public DoubleProperty getXProperty() {
        return xPosition;
    }

    /**
     * @return returns the property representing the y-position  of the vertex
     */
    @Override
    public DoubleProperty getYProperty() {
        return yPosition;
    }

    /**
     * @return returns the property representing the radius of the circle representing the vertex
     */
    @Override
    public DoubleProperty getRadiusProperty() {
        return radius;
    }

    /**
     * @return returns the underlying vertex from the graph Data structure
     */
    @Override
    public Vertex<V, E> getUnderlyingVertex() {
        return vertex;
    }

    /**
     * This method add a css styling to the vertex
     *
     * @param css css style
     */
    @Override
    public void setStyling(String css) {
        this.setStyle(css);
    }

    /**
     * This method add the adjacent vertex on to this one to the list
     *
     * @param vertex the adjacent vertex
     */
    @Override
    public void addAdjacentVertex(VertexNode<V, E> vertex) {
        this.adjacencyList.add(vertex);
    }

    /**
     * @return returns the color of the circle
     */
    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }

    public Circle events(){
        return this;
    }
}
