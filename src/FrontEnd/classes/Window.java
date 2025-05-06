package FrontEnd.classes;

import BackEnd.Algorithms.preprocessing.utilization.Pair;
import BackEnd.DataStructures.Interfaces.Queue;
import FrontEnd.interfaces.LabelUI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * This class defines a grid pane the structures the UI
 */
public class Window extends GridPane {

    /**
     * This constructor creates a  grid pane to organize the original Image, its KNN graph and results from the GNN
     * @param image original image
     * @param frame pane that draws the graph
     * @param results results from the GNN
     */
    public Window(Image image, Pane frame, Queue<Pair<Double, String>> results){
        layerImageWithGraph(image, frame, results);
    }

    /**
     * This method structures the UI to display the original image, its graph and the results for the GNN
     * @param image original image
     * @param graphView KNN graph
     * @param results results from the GNN
     */
    public void layerImageWithGraph(Image image, Pane graphView,  Queue<Pair<Double, String>> results){
        LabelUI imageLabel = new LabelNode("Original Image");
        LabelUI graphLabel = new LabelNode("KNN Graph For Image");

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(600);
        imageView.setFitHeight(400);

        VBox layerImage = new VBox();
        layerImage.getChildren().addAll((Node) imageLabel, imageView);
        layerImage.setPadding(new Insets(10,10,10,10));
        layerImage.setSpacing(10);

        VBox layerGraph = new VBox();
        layerGraph.getChildren().addAll((Node) graphLabel, graphView);
        layerGraph.setPadding(new Insets(10,10,10,10));
        layerGraph.setSpacing(10);

        Pair<Double, String> prediction = results.dequeue();

        HBox layerScore = new HBox();
        LabelUI ScoreLabel = new LabelNode("Score: ");
        LabelUI score = new LabelNode(String.format("%.2f", prediction.getFirst()));
        layerScore.getChildren().addAll((Node) ScoreLabel, (Node) score);
        layerScore.setSpacing(10);
        layerScore.setPadding(new Insets(10,10, 10, 10));

        HBox layerConclusion = new HBox();
        LabelUI conclusionLabel = new LabelNode("Model conclusion: ");
        LabelUI conclusion = new LabelNode(prediction.getSecond());
        layerConclusion.getChildren().addAll((Node) conclusionLabel, (Node) conclusion);
        layerConclusion.setPadding(new Insets(10,10,10,10));
        layerConclusion.setSpacing(10);


        this.add(layerImage, 0,0);
        this.add(layerGraph, 1, 0);
        this.add(layerScore,0, 1);
        this.add(layerConclusion, 1, 1);
        super.setMinSize(900, 600);
        super.setPadding(new Insets(10, 10, 10, 10));
        super.setVgap(10);
        super.setHgap(20);
        super.setAlignment(Pos.CENTER);
    }
}
