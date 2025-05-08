package app.BackEnd.GraphNeuralNetwork.Network;

import app.BackEnd.Algorithms.preprocessing.utilization.Pair;
import app.BackEnd.Algorithms.preprocessing.utilization.Patch;
import app.BackEnd.DataStructures.Classes.QueueDS;
import app.BackEnd.DataStructures.Interfaces.ArrayList;
import app.BackEnd.DataStructures.Interfaces.Queue;
import app.BackEnd.GraphBuilder.KNNGraph;
import app.BackEnd.GraphNeuralNetwork.Layers.Layer;
import app.BackEnd.GraphStructure.Interfaces.Graph;
import javafx.scene.image.Image;

import java.util.Random;

public class GNNModel extends Layer {
    private double[] linearWeights;
    Random random = new Random(123);

    public GNNModel(int weightMatrixRows, int weightMatrixCols){
        super(weightMatrixRows, weightMatrixCols);
        this.linearWeights = new double[weightMatrixCols];
        generateLinearWeights();
    }

    /**
     * This method tests the model using a new graph
     * @param KNNgraph the new graph
     * @return returns the score and the conclusion
     */
    public Queue<Pair<Double, String>> test(Graph<Patch, Double> KNNgraph){
        // message passing
        LayerMessagePassing(KNNgraph);
        //  predict
        double[] predictedScores = predictScore(KNNgraph);
        double score = averageScore(predictedScores);
        Queue<Pair<Double, String>> results = new QueueDS<>();
        // get results
        if(score >= 0.58){
            results.enqueue(new Pair<>(score, "good image"));
        } else {
           results.enqueue(new Pair<>(score, "Bad Image"));
        }
        return results;
    }

    /**
     * This method is used to train the model
     * @param imagesWithLabel training images with labels
     * @param learningRate learning rate of the model
     * @param numberOFLayers the number of layer to perform message passing
     */
    public void trainModel(ArrayList<Pair<Image, Integer>> imagesWithLabel , double learningRate, int numberOFLayers){
        for(int i = 0; i < imagesWithLabel.size(); i++){
            Image currentImage = imagesWithLabel.get(i).getFirst();
            int currentImageLabel = imagesWithLabel.get(i).getSecond();
            // Build KNN graph for the currentImage with a neighbourhood if 10
            KNNGraph knnGraph = new KNNGraph(currentImage, 10);
            double totalLoss = 0.0;
            // for each layer
            for(int l = 0; l < numberOFLayers; l++){
                // message passing -> Update feature vectors
                LayerMessagePassing(knnGraph.getKnnGraph());
                // Pooling -> aggregate all feature on the nodes
                double[] pooledFeatures = poolFeatures(knnGraph.getKnnGraph());
                // perform readout with Linear classification
                double predictedScore = averageScore(predictScore(knnGraph.getKnnGraph()));
                // compute loss
                double loss = calculateLoss(predictedScore, currentImageLabel);
                totalLoss += loss;
                // update the weights of the model
                updateLinearWeights(pooledFeatures, predictedScore, currentImageLabel, learningRate);
            }
            System.out.println("Image " + i + " - Total Loss: " + totalLoss);
        }
    }

    /**
     * This method performs message passing on the given graph
     * @param KNNGraph graph of the image
     */
    public void LayerMessagePassing(Graph<Patch, Double> KNNGraph){
        MessagePassing(KNNGraph.getVertices());
    }

    /**
     * This method predicts a score for each path using its updated feature vector after message passing
     * @return returns an array of all the scores
     */
    public double[] predictScore(Graph<Patch, Double> graph){
        double[] scores = new double[graph.getVertices().size()];
        for(int i = 0; i < graph.getVertices().size(); i++){
            double[] updatedFeatureVector = graph.getVertices().get(i).element().featureVector();
            scores[i] = linearTransform(updatedFeatureVector);
        }
        return scores;
    }

    /**
     * This method computes the average score
     * @param predictedScores an array of scores
     * @return returns the average score
     */
    public double averageScore(double[] predictedScores){
        double sum = 0.0;
        for(int i = 0; i < predictedScores.length; i++){
            sum += predictedScores[i];
        }
        return  sum / predictedScores.length;
    }

    /**
     * This method generates random weights
     */
    private void generateLinearWeights(){
        for(int i = 0; i < this.linearWeights.length; i++){
            this.linearWeights[i] = random.nextGaussian() * 0.1;
        }
    }

    /**
     * This method computes an aggregate transformation of the updated feature vector after message passing and computes a probability based
     * @param updatedFeatureVector the updated feature vector after message passing
     * @return returns a probability of each feature vector
     */
    private double linearTransform(double[] updatedFeatureVector){
        double sum = 0.0;
        for(int i = 0; i < updatedFeatureVector.length; i++){
            sum += updatedFeatureVector[i] * this.linearWeights[i];
        }
        return  sigmoid(sum); // computes probability
    }

    /**
     * This method calculates the loss using the Binary Cross Entropy
     * @param predictedScore the score predicted my the GNN
     * @param trueLabel the true label for the image (1 for good, 0 for bad)
     * @return return the computed loss
     */
    private double calculateLoss(double predictedScore, int trueLabel){
        predictedScore = Math.max(1e-7, Math.min(1 - 1e-7, predictedScore)); // to ensure value is between 0 and 1 (to avoid log(0) )
        return  -(trueLabel * Math.log(predictedScore) + (1 - trueLabel) * Math.log(1 - predictedScore));
    }

    /**
     * This method defines the sigmoid function
     * @param value the parameter
     * @return returns output
     */
    private double sigmoid(double value){
        return 1.0 / (1.0 + Math.exp(-value));
    }


    private double derivativeOFLossWithRespectToWeights(double predicted, int trueLabel){
        return predicted - trueLabel;
    }

    /**
     * This method pools the updated feature vectors from all the node and aggregates them and averages them to standardize
     * @param imageGraph input graph
     * @return returns the full feature vector
     */
    private double[] poolFeatures(Graph<Patch, Double> imageGraph){
        assert !imageGraph.getVertices().isEmpty();
        int length = imageGraph.getVertices().get(0).element().featureVector().length;
        double[] pooled = new double[length];

        for(int i = 0; i < imageGraph.getVertices().size(); i++){
            double[] featureUpdatedFeatureVector = imageGraph.getVertices().get(i).element().featureVector();
            for(int j = 0; j < length; j++){
                pooled[j] += featureUpdatedFeatureVector[j]; // sum
            }
        }

        int size = imageGraph.getVertices().size();
        for(int i = 0; i < length; i++){
            pooled[i] /= size; // average
        }
        return pooled;
    }

    /**
     * This method updates the linear weights using the gradient decent algorithm
     * @param updatedFeatures the vector of updated features from message passing
     * @param predictedScore the predicted score
     * @param trueLabel the true label for the image
     * @param modelLearningRate the learning rate of the model
     */
    private void updateLinearWeights(double[] updatedFeatures, double predictedScore, int trueLabel, double modelLearningRate){
        double derivativeOFLossWithRespectPredicted = derivativeOFLossWithRespectToWeights(predictedScore, trueLabel);
        for(int i = 0; i < this.linearWeights.length; i++){
            this.linearWeights[i] -= modelLearningRate * derivativeOFLossWithRespectPredicted * updatedFeatures[i]; // compute the gradient descent
        }
    }

}
