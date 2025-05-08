package app.BackEnd.Algorithms.preprocessing.utilization;

import app.BackEnd.Algorithms.preprocessing.abstraction.FeatureExtraction;
import app.BackEnd.Algorithms.preprocessing.processing.ProcessingImage;
import javafx.scene.image.Image;

/**
 * This class defines a patch and its features
 */
public class Patch  extends FeatureExtraction{
    private final Image patch;
    private double[] featureVector;

    /**
     * This constructor creates a patch
     * @param patch a patch from an image
     */
    public Patch(Image patch){
        this.patch = patch;
        this.featureVector = computeFeatureVector();
    }

    /**
     * This method computes the average color of this patch
     * @return returns an array of the RGB channel averages
     */
    public double[] colorFeature(){
        return averageColor(this.patch);
    }

    /**
     * This method computes a texture analysis on this patch
     * @return returns an array of the 4 common features describing texture : contrast, energy, entropy and homogeneity
     */
    public double[] textureFeature(){
        ProcessingImage processor =  new ProcessingImage();
        return averageGLCMFeatures(processor.grayScaleImage(this.patch));
    }

    /**
     * This method first computes a canny edge detection on this patch, the counts the number of edge pixels
     * @return returns the number of edge pixels of the patch
     */
    public double edgeDensity(){
        ProcessingImage processor = new ProcessingImage();
        return processor.computeEdgeDensity(processor.cannyEdgeImage(processor.gaussianBlurImage(this.patch)));
    }

    /**
     * @return returns a vector consisting of all the features of this patch
     */
    public double[] featureVector(){
        return this.featureVector;
    }

    /**
     * This method updates the underlying feature vector with the new feature vector
     * @param featureVector new feature vector
     */
    public void setFeatureVector(double[] featureVector){
        this.featureVector = featureVector;
    }

    /**
     * This method builds a full feature vector for the patch
     * [Red, Green, Blue, Contrast, Energy, Entropy, Homogeneity, Edges]
     * @return returns a vector consisting of all the features of this patch
     */
    private double[] computeFeatureVector(){
        double[] color = colorFeature(), texture = textureFeature(), edges = new double[]{edgeDensity()};
        double[] vector = new double[color.length + texture.length + edges.length];
        System.arraycopy(color, 0, vector, 0, color.length);
        System.arraycopy(texture, 0, vector, color.length, texture.length);;
        System.arraycopy(edges, 0, vector, texture.length, edges.length);
        return vector;
    }

    /**
     * @return returns the underlying patch
     */
    public Image getPatch() {
        return patch;
    }
}
