import BackEnd.Algorithms.preprocessing.utilization.Pair;
import BackEnd.Algorithms.preprocessing.utilization.Patch;
import BackEnd.DataStructures.Classes.ArrayListDS;
import BackEnd.DataStructures.Classes.MapDS;
import BackEnd.DataStructures.Interfaces.ArrayList;
import BackEnd.DataStructures.Interfaces.Map;
import BackEnd.DataStructures.Interfaces.Queue;
import BackEnd.GraphBuilder.KNNGraph;
import BackEnd.GraphNeuralNetwork.Network.GNNModel;
import FrontEnd.classes.GraphView;
import FrontEnd.classes.Window;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This class defines the main class the runs the whole program
 */
public class Main extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Image image = new Image(new FileInputStream(new File("data/testing/inadequate/1.jpg")));
        KNNGraph graph = new KNNGraph(image,10);
        GNNModel model = new GNNModel(4, 4);
        model.trainModel(loadImagesFromSubfolders("data/training"), 0.1, 3);
        Queue<Pair<Double, String>> results = model.test(graph.getKnnGraph());

        GraphView<Patch, Double> graphView = new GraphView<>(graph.getKnnGraph());
        Scene scene = new Scene(new Window(image, graphView, results));
        graphView.buildGraph(550, 420);
        graphView.setLayoutX(100);
        graphView.setLayoutY(100);

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
    public static ArrayList<Pair<Image, Integer>> loadImagesFromSubfolders(String datasetPath) {
        ArrayList<Pair<Image, Integer>> imagesWithLabels = new ArrayListDS<>();
        Map<String, Integer> labelMapping = new MapDS<>();

        File datasetFolder = new File(datasetPath);
        File[] listOfSubfolders = datasetFolder.listFiles();

        int currentLabel = 0;

        if (listOfSubfolders != null) {
            for (File subfolder : listOfSubfolders) {
                if (subfolder.isDirectory()) {
                    labelMapping.put(subfolder.getName(), currentLabel);

                    File[] imageFiles = subfolder.listFiles();
                    if (imageFiles != null) {
                        for (File file : imageFiles) {
                            if (file.isFile() && isImageFile(file)) {
                                try {
                                    Image image = new Image(new FileInputStream(file));
                                    imagesWithLabels.add(new Pair<>(image, currentLabel));
                                } catch (FileNotFoundException e) {
                                    System.out.println("File not found: " + file.getAbsolutePath());
                                }
                            }
                        }
                    }
                    currentLabel++;
                }
            }
        } else {
            System.out.println("No subfolders found in dataset: " + datasetPath);
        }

        return imagesWithLabels;
    }

    /**
     * This method checks if the given file is a valid image
     * @param file file
     * @return returns true if its am image else false
     */
    private static boolean isImageFile(File file) {
        String[] imageExtensions = {"jpg", "jpeg", "png", "bmp", "gif"};
        String fileName = file.getName().toLowerCase();

        for (String extension : imageExtensions) {
            if (fileName.endsWith("." + extension)) {
                return true;
            }
        }
        return false;
    }
}
