package app.BackEnd.GraphNeuralNetwork.Layers;

import app.BackEnd.Algorithms.preprocessing.utilization.Patch;
import app.BackEnd.DataStructures.Classes.ArrayListDS;
import app.BackEnd.DataStructures.Interfaces.ArrayList;
import app.BackEnd.GraphNeuralNetwork.Exceptions.GNNException;
import app.BackEnd.GraphStructure.Interfaces.Edge;
import app.BackEnd.GraphStructure.Interfaces.Vertex;
import java.util.Random;

public class Layer {
    private final double[][] weights;
    private final int weightMatrixRows;
    private final int weightMatrixCols;

    public Layer(int weightMatrixRows, int weightMatrixCols){
        this.weightMatrixRows = weightMatrixRows;
        this.weightMatrixCols = weightMatrixCols;
        this.weights = generateWeights(this.weightMatrixRows, this.weightMatrixCols);
    }

    /**
     * This method performs message passing between the nodes of a by doing weighted edge aggregation to update the vertices
     * @param vertices the list of vertices from the graph
     */
    public void MessagePassing(ArrayList<Vertex<Patch, Double>> vertices){
        ArrayList<double[]> updatedFeatures = new ArrayListDS<>(); // list to store updated features
        for(int i = 0; i < vertices.size(); i++){
            Vertex<Patch, Double> current = vertices.get(i); // get the current vertex
            double[] itsFeatureVector = current.element().featureVector();
            double[] aggregate = new double[this.weights[0].length];

            for(int j = 0; j < this.weightMatrixCols; j++){
                aggregate[j] += itsFeatureVector[j];
            }
            // iterate through its neighbourhood
            for(int j = 0; j < current.getEdges().size(); j++){
                Edge<Patch, Double> connectingEdge = current.getEdges().get(j);
                Vertex<Patch, Double> neighbour = connectingEdge.getEndVertex();
                double[] neighbourFeatureVector = neighbour.element().featureVector();
                double edgeWeight = connectingEdge.getWeight();
                for(int k = 0; k < this.weights[0].length; k++){
                    aggregate[k] += edgeWeight * neighbourFeatureVector[k]; // weight sum
                }
            }

            // conduct linear transformation
            double[] transformed = featureTransformation(aggregate);
            double[] activated = ReLU(transformed);
            updatedFeatures.add(activated); // store updated feature vectors
        }
        // now update each patch with the new features
        for(int i = 0; i < vertices.size(); i++){
            vertices.get(i).element().setFeatureVector(updatedFeatures.get(i));
        }
    }

    /**
     * This method generates random weight using the gaussian method
     * @param rows number of rows for the weight matrix
     * @param cols number of columns for the wight matrix
     * @return returns the weight matrix
     */
    private double[][] generateWeights(int rows, int cols){
        double[][] weightMatrix = new double[rows][cols];
        Random random = new Random(123);
        for(int r = 0;  r < rows; r++){
            for(int c = 0; c < cols; c++){
                weightMatrix[r][c] = random.nextGaussian() * 0.1;
            }
        }
        return weightMatrix;
    }

    /**
     * This method performs transformation of the feature vector by multiplying it with the weights : for interpreting the information
     * @param featureMatrix the feature vector
     * @return returns the transformed vector
     */
    private double[] featureTransformation(double[] featureMatrix){
        if(featureMatrix.length != weightMatrixCols){
            throw new GNNException("Feature : rows x cols -> Weight: rows x cols, the number of columns of the feature matrix doesn't match the columns of the weight matrix");
        }

        double[] transformed = new double[weightMatrixRows];
        for(int i = 0; i < weightMatrixRows; i++){
            for(int j = 0; j < weightMatrixCols; j++){
                transformed[i] += featureMatrix[j] * this.weights[i][j];
            }
        }
        return transformed;
    }

    /**
     * This method applies the ReLU method on the transformed vector
     * @param vector transformed vector
     * @return returns the activated vector
     */
    private double[] ReLU(double[] vector){
        double[] activated = new double[vector.length];
        for(int i = 0; i < vector.length; i++){
            activated[i] = Math.max(0, vector[i]);
        }
        return activated;
    }
}
