package app.BackEnd.GraphBuilder;

import app.BackEnd.Algorithms.preprocessing.abstraction.Patches;
import app.BackEnd.Algorithms.preprocessing.utilization.Pair;
import app.BackEnd.Algorithms.preprocessing.utilization.Patch;
import app.BackEnd.Algorithms.vectors.Calculations;
import app.BackEnd.DataStructures.Classes.ArrayListDS;
import app.BackEnd.DataStructures.Interfaces.ArrayList;
import app.BackEnd.GraphStructure.Classes.GraphStructure;
import app.BackEnd.GraphStructure.Interfaces.Graph;
import app.BackEnd.GraphStructure.Interfaces.Vertex;
import javafx.scene.image.Image;

import java.util.Comparator;
import java.util.Iterator;

/**
 * This class defines the KNN graph for a given image
 */
public class KNNGraph {
    private final Graph<Patch, Double> knnGraph;
    private final ArrayList<Patch> patches;

    /**
     * Thsi constructor builds a knn graph for the given image
     * @param image input image
     * @param kthNeighbor the size of the neighbourhood
     */
    public KNNGraph(Image image, int kthNeighbor){
        this.knnGraph = new GraphStructure<>();
        Calculations calculator = new Calculations();
        Image scaledImage = calculator.resize(image, 800, 600);
        this.patches = new Patches().extractPatchesFromImage(scaledImage, 128, 128, 128);
        loadNodes();
        connectEdges(kthNeighbor);
    }

    /**
     * This method adds all patches as vertices to the graph
     */
    private void loadNodes(){
        Iterator<Patch> iterator = patches.iterator();
        while(iterator.hasNext()){
            Patch patch = iterator.next();
            knnGraph.addVertex(patch);
        }
    }

    /**
     * This method creates an edge between a patch and its nearest patches based on the distance between
     * @param kNeighbor the kth neighbor
     */
    private void connectEdges(int kNeighbor){
        int size = knnGraph.getVertices().size();
        Calculations calculator = new Calculations();
        for(int i = 0; i < size; i++){
            Vertex<Patch, Double> currentVertex = knnGraph.getVertices().get(i); // get the vertex from the graph
            ArrayList<Pair<Vertex<Patch, Double>, Double>> distances = new ArrayListDS<>(); // list of pairs
            for(int j = 0; j < size; j++){
                if(i == j) continue;
                Vertex<Patch, Double> nextVertex = knnGraph.getVertices().get(j); // get another vertex from the graph
                double distance = calculator.EuclideanDistance(currentVertex.element().featureVector(), nextVertex.element().featureVector());
                distances.add(new Pair<>(nextVertex, distance));
            }
            distances.sort(Comparator.comparingDouble(Pair::getSecond)); // sort the list by the distances
            for(int r = 0; r < kNeighbor && r < distances.size(); r++){
                Vertex<Patch, Double> neighbour = distances.get(r).getFirst();
                double weight = distances.get(r).getSecond();
                knnGraph.addEdge(currentVertex.element(), neighbour.element(), weight); //create edge between the current(vertex) patch and its neighbours(vertex) patch
                currentVertex.addEdge(neighbour, weight); // update vertex's edge list
            }
        }
    }

    /**
     * @return returns the knn graph
     */
    public Graph<Patch, Double> getKnnGraph(){
        return this.knnGraph;
    }
}
