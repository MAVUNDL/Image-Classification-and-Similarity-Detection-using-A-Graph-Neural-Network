package app.FrontEnd.classes;

import app.BackEnd.Algorithms.preprocessing.utilization.Pair;
import app.BackEnd.DataStructures.Interfaces.Queue;
import app.FrontEnd.interfaces.LabelUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * This class defines a grid pane the structures the UI
 */
public class Window extends GridPane {
    private Button slider;
    private Image image;
    private ImageView imageView;
    private Pane pane;

    /**
     * This constructor creates a  grid pane to organize the original Image, its KNN graph and results from the GNN
     * @param image original image
     * @param frame pane that draws the graph
     * @param results results from the GNN
     */
    public Window(Image image, Pane frame, Queue<Pair<Double, String>> results){
        this.pane = frame;
        this.image = image;
        this.slider = new Button("Next Image");
        this.imageView = new ImageView(this.image);
        this.imageView.setFitWidth(600);
        this.imageView.setFitHeight(400);
        layerImageWithGraph(this.imageView, frame, results);
    }

    /**
     * This method structures the UI to display the original image, its graph and the results for the GNN
     * @param imageView  image view
     * @param graphView KNN graph
     * @param results results from the GNN
     */
    public void layerImageWithGraph(ImageView imageView, Pane graphView,  Queue<Pair<Double, String>> results){
        LabelUI imageLabel = new LabelNode("Original Image");
        LabelUI graphLabel = new LabelNode("KNN Graph For Image");

        VBox layerImage = new VBox();
        layerImage.getChildren().addAll((Node) imageLabel, imageView, this.slider);
        layerImage.setPadding(new Insets(10,10,10,10));
        layerImage.setSpacing(10);

        VBox layerGraph = new VBox();
        layerGraph.getChildren().addAll((Node) graphLabel, graphView);
        layerGraph.setPadding(new Insets(10,10,10,10));
        layerGraph.setSpacing(10);

        //Pair<Double, String> prediction = results.dequeue();

        HBox layerScore = new HBox();
        LabelUI ScoreLabel = new LabelNode("Score: ");
        LabelUI score = new LabelNode("");
        layerScore.getChildren().addAll((Node) ScoreLabel, (Node) score);
        layerScore.setSpacing(10);
        layerScore.setPadding(new Insets(10,10, 10, 10));
        layerScore.setVisible(false);

        HBox layerConclusion = new HBox();
        LabelUI conclusionLabel = new LabelNode("Model conclusion: ");
        LabelUI conclusion = new LabelNode("");
        layerConclusion.getChildren().addAll((Node) conclusionLabel, (Node) conclusion);
        layerConclusion.setPadding(new Insets(10,10,10,10));
        layerConclusion.setSpacing(10);
        layerConclusion.setVisible(false);


        this.add(layerImage, 0,0);
        this.add(layerGraph, 1, 0);
        this.add(layerScore,0, 1);
        this.add(layerConclusion, 1, 1);
        super.setMinSize(900, 600);
        super.setPadding(new Insets(10, 10, 10, 10));
        super.setVgap(10);
        super.setHgap(20);
        super.setAlignment(Pos.CENTER);

        graphSimulation(graphView, () -> {
            // After graph is done
            Pair<Double, String> prediction = results.dequeue();
            score.label().setText(String.format("%.2f", prediction.getFirst()));
            conclusion.label().setText(prediction.getSecond());

            layerScore.setVisible(true);
            layerConclusion.setVisible(true);
        });
    }

    /**
     * This method simulates the updating of the GUI with the model results after the graph is drawn
     * @param graphView the node that stores the graph
     * @param finished runnable instance to detect if the drawing of the graph was complete
     */
    private void graphSimulation(Pane graphView, Runnable finished){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(9), event -> {
            finished.run();
        }));
        timeline.play();
    }

    /**
     * @return returns a reference to the button to update the images in the GUI
     */
    public Button imageSlider(){
        return this.slider;
    }

    /**
     * This method updates the GUI with a new Image and graph
     * @param image new image
     * @param results results from the GNN after testing image
     */
    public void updateImage(Image image, Queue<Pair<Double, String>> results){
        this.getChildren().clear();
        this.image = image;
        this.imageView.setImage(this.image);
        layerImageWithGraph(this.imageView, this.pane, results);
    }
}
