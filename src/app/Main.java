package app;

import app.BackEnd.Algorithms.preprocessing.utilization.Pair;
import app.BackEnd.Algorithms.preprocessing.utilization.Patch;
import app.BackEnd.DataStructures.Classes.ArrayListDS;
import app.BackEnd.DataStructures.Classes.QueueDS;
import app.BackEnd.DataStructures.Interfaces.ArrayList;
import app.BackEnd.DataStructures.Interfaces.Queue;
import app.BackEnd.GraphBuilder.KNNGraph;
import app.BackEnd.GraphNeuralNetwork.Network.GNNModel;
import app.FrontEnd.classes.GraphView;
import app.FrontEnd.classes.Window;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * This class defines the main class the runs the whole program
 */
public class Main extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Queue<Image> imageQueue = queueImages(loadImagesFromSubfolders("/app/resources", "testing", "testing.txt"));
        Image image = imageQueue.dequeue();
        KNNGraph graph = new KNNGraph(image,15);
        GNNModel model = new GNNModel(8, 8);
        model.trainModel(loadImagesFromSubfolders("/app/resources", "training", "data.txt"), 0.001, 1);
        Queue<Pair<Double, String>> results = model.test(graph.getKnnGraph());

        GraphView<Patch, Double> graphView = new GraphView<>(graph.getKnnGraph());
        Window window = new Window(image, graphView, results);
        Scene scene = new Scene(window);
        graphView.buildGraph(550, 420);
        graphView.setLayoutX(100);
        graphView.setLayoutY(100);
        AtomicInteger counter = new AtomicInteger();
        int numberOfImages = imageQueue.size();
        window.imageSlider().setOnAction(actionEvent -> {
            System.out.println("Size: " + imageQueue.size());
            if (counter.get() < numberOfImages - 1) {
                Image currentImage = imageQueue.dequeue();
                KNNGraph newGraph = new KNNGraph(currentImage, 10);
                Queue<Pair<Double, String>> newResults = model.test(newGraph.getKnnGraph());
                counter.getAndIncrement();
                Platform.runLater(() -> {
                    window.updateImage(currentImage, newResults);
                    graphView.update(newGraph.getKnnGraph());
                });

                System.out.println("Counter: " + counter.get());
            }
            else{
                Platform.runLater(() -> {
                    Alert alert =  new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("End of Images");
                    alert.setHeaderText(null);
                    alert.setContentText("No more images to display.");
                    alert.showAndWait();
                });
            }
        });

        window.uploader().setOnAction(actionEvent -> {
            FileChooser file = new FileChooser();
            Image currentImage = null;
            try {
                currentImage = new Image(new FileInputStream(file.showOpenDialog(stage)));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            KNNGraph newGraph = new KNNGraph(currentImage, 10);
            Queue<Pair<Double, String>> newResults = model.test(newGraph.getKnnGraph());
            counter.getAndIncrement();
            Image finalCurrentImage = currentImage;
            Platform.runLater(() -> {
                window.updateImage(finalCurrentImage, newResults);
                graphView.update(newGraph.getKnnGraph());
            });

        });
        stage.setScene(scene);
        stage.setMinHeight(660);
        stage.setMinWidth(1300);
        stage.setResizable(true);
        stage.show();
    }

    /**
     * This method load the images from the folders
     * @param datasetPath path to the folder
     * @return returns a list of the images with their labels
     */
    public static ArrayList<Pair<Image, Integer>> loadImagesFromSubfolders(String datasetPath, String type, String dataFile) {
        ArrayList<Pair<Image, Integer>> imagesWithLabels = new ArrayListDS<>();

        try {;
            // Load the dataset.txt file
            InputStream inputStream = Main.class.getResourceAsStream( datasetPath + "/" + dataFile);
            if (inputStream == null) {
                System.out.println("data.txt not found at path: " + datasetPath);
                return imagesWithLabels;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int adequateLabel = 0;
            int inadequateLabel = 1;

            while ((line = reader.readLine()) != null) {
                String fullPath = datasetPath + "/" + type.toLowerCase() + "/" + line;  // Full path inside JAR

                InputStream imageStream = Main.class.getResourceAsStream(fullPath);
                if (imageStream != null) {
                    Image image = new Image(imageStream);

                    // Determine label based on path
                    int label = -1;
                    if (line.startsWith("adequate/")) {
                        label = adequateLabel;
                    } else if (line.startsWith("inadequate/")) {
                        label = inadequateLabel;
                    }

                    imagesWithLabels.add(new Pair<>(image, label));
                } else {
                    System.out.println("Cannot load image: " + fullPath);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imagesWithLabels;
    }

    /**
     * This method takes the list of images with their labels and creates a queue to store the images only without their label for testing purposes
     * @param imagesWithLabels list of images with their labels
     * @return returns the queue of images
     */
    private  Queue<Image> queueImages( ArrayList<Pair<Image, Integer>> imagesWithLabels){
        Queue<Image> imageQueue = new QueueDS<>();
        for(int i = 0; i < imagesWithLabels.size(); i++){
            Pair<Image, Integer> pair = imagesWithLabels.get(i);
            imageQueue.enqueue(pair.getFirst());
        }
        return imageQueue;
    }
}
