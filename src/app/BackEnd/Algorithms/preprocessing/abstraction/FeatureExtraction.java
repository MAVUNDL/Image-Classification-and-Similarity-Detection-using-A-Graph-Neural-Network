package app.BackEnd.Algorithms.preprocessing.abstraction;

import app.BackEnd.DataStructures.Classes.ArrayListDS;
import app.BackEnd.DataStructures.Interfaces.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

/**
 * This abstract class defines the algorithms to extract feature from images
 */
public abstract class FeatureExtraction {
    /**
     * This method calculates the average color of each RGB channel on the given patch
     * @return returns an array of the 3 averages (RGB)
     */
    public double[] averageColor(Image patch){
        // get dimensions
        int width = (int) patch.getWidth();
        int height = (int) patch.getHeight();
        PixelReader reader = patch.getPixelReader();

        double sumRed = 0.0, sumGreen = 0.0, sumBlue = 0.0;

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                Color color = reader.getColor(x, y);
                sumRed += color.getRed();
                sumGreen += color.getGreen();
                sumBlue += color.getBlue();
            }
        }
        int total = width * height;
        return new double[]{ (sumRed / total), (sumGreen / total), (sumBlue / total)};
    }


    /**
     * This method takes an image patch and converts into a quantized grayscale matrix. Instead of using 256 gray levels (0–255), it reduces them into 8 levels (0–7).
     * @param patch the image patch
     * @return returns a quantized matrix of gray values
     */
    public int[][] quantizedGrayIntensities(Image patch){
        // get dimensions
        int width = (int) patch.getWidth();
        int height = (int) patch.getHeight();
        PixelReader reader = patch.getPixelReader();
        int level = 8;
        int[][] matrix = new int[width][height];

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int argb = reader.getArgb(x, y); // get argb pixel
                int red = (argb >> 16) & 0xff;
                int green = (argb >> 8) & 0xff;
                int blue = (argb) & 0xff;
                int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue); // compute gray intensity
                matrix[x][y] = (gray * level) / 256;
            }
        }
        return matrix;
    }

    /**
     * This method normalizes the GLCM matrix
     * @param glcmMatrix the GLCM matrix to be normalized
     * @return returns the normalized GLCM matrix
     */
    private double[][] normalizeMatrix(int[][] glcmMatrix){
        double sum = 0;
        double[][] normalized = new double[glcmMatrix.length][glcmMatrix[0].length];

        for(int r = 0; r < glcmMatrix.length; r++){ // compute the sum
            for(int c = 0; c < glcmMatrix[0].length; c++){
                sum += glcmMatrix[r][c];
            }
        }

        for(int r = 0; r < glcmMatrix.length; r++){ // normalize values
            for(int c = 0; c < glcmMatrix[0].length; c++){
                normalized[r][c] = (double) glcmMatrix[r][c] / sum;
            }
        }
        return normalized;
    }

    /**
     * This method computes a GLCM for each direction 0, 45, 90 and 135 degrees from the quantized gray levels matrix
     * @param quantizedGrayMatrix the quantized matrix
     * @return returns a list of all the computed GLCMs
     */
    private ArrayList<int[][]> glcmComputationPerDirection(int[][] quantizedGrayMatrix){
        int[][] glcm0 = new int[8][8], glcm45 = new int[8][8], glcm90 = new int[8][8], glcm135 = new int[8][8];

        for(int x = 0; x < quantizedGrayMatrix.length; x++){
            for(int y = 0; y < quantizedGrayMatrix[0].length; y++){
                if(x > 0 && x < quantizedGrayMatrix.length - 1 && y > 0 && y < quantizedGrayMatrix[0].length - 1){
                    int quantizedValue = quantizedGrayMatrix[x][y]; // get the current quantized gray value
                    // get quantized gray values per direction
                    int degrees0 = quantizedGrayMatrix[x][y + 1];
                    int degrees45 = quantizedGrayMatrix[x - 1][y + 1];
                    int degrees90 = quantizedGrayMatrix[x - 1][y];
                    int degrees135 = quantizedGrayMatrix[x - 1][y - 1];

                    // count the pairs per direction
                    glcm0[quantizedValue][degrees0]++;
                    glcm45[quantizedValue][degrees45]++;
                    glcm90[quantizedValue][degrees90]++;
                    glcm135[quantizedValue][degrees135]++;
                }
            }
        }
        ArrayList<int[][]> glcm = new ArrayListDS<>();
        glcm.add(glcm0);
        glcm.add(glcm45);
        glcm.add(glcm90);
        glcm.add(glcm135);

        return glcm;
    }

    /**
     * This method extracts the contrast, energy, entropy and homogeneity texture properties from the GLCM
     * @param normalizedGLCM the normalized GLCM
     * @return returns an array consisting of the properties
     */
    private double[] extractGLCMFeatures(double[][] normalizedGLCM){
        double contrast = 0.0, energy = 0.0, entropy = 0.0,  homogeneity = 0.0;

        for(int i = 0; i < normalizedGLCM.length; i++){
            for(int j = 0; j < normalizedGLCM[i].length; j++){
                contrast += ((i - j)^2) * normalizedGLCM[i][j];
            }
        }

        for(int i = 0; i < normalizedGLCM.length; i++){
            for(int j = 0; j < normalizedGLCM[i].length; j++){

                energy += normalizedGLCM[i][j] *  normalizedGLCM[i][j];
            }
        }

        for(int i = 0; i < normalizedGLCM.length; i++){
            for(int j = 0; j < normalizedGLCM[i].length; j++){
                if(normalizedGLCM[i][j] > 0){
                    entropy -= normalizedGLCM[i][j] * (Math.log(normalizedGLCM[i][j]) / Math.log(2)); // getting log2(GLCM[i][j]
                }
            }
        }

        for(int i = 0; i < normalizedGLCM.length; i++){
            for(int j = 0; j < normalizedGLCM[i].length; j++){
                // now calculate the homogeneity
                homogeneity += ((double) normalizedGLCM[i][j] / (1 + (Math.abs(i - j))));
            }
        }

        return new double[]{contrast, energy, entropy, homogeneity};
    }

    /**
     * This method computes the average of the features extracted on the GLCM matrices
     * @param patch the image to be used to compute the GLCM on all directions
     * @return returns an array of the averaged features
     */
    public double[] averageGLCMFeatures(Image patch){
        // quantized patch and compute glcm matrices
        int[][] quantizedMatrix = quantizedGrayIntensities(patch);
        ArrayList<int[][]> glcmList = glcmComputationPerDirection(quantizedMatrix);

        // normalize glcm matrices
        double[][] normalized0GLCM = normalizeMatrix(glcmList.get(0));
        double[][] normalized45GLCM = normalizeMatrix(glcmList.get(1));
        double[][] normalized90GLCM = normalizeMatrix(glcmList.get(2));
        double[][] normalized135GLCM = normalizeMatrix(glcmList.get(3));

        // extract features in each normalize glcm
        double[] glcm0Features = extractGLCMFeatures(normalized0GLCM);
        double[] glcm45Features = extractGLCMFeatures(normalized45GLCM);
        double[] glcm90Features = extractGLCMFeatures(normalized90GLCM);
        double[] glcm135Features = extractGLCMFeatures(normalized135GLCM);

        // compute averages
        double contrast = (glcm0Features[0] + glcm45Features[0] + glcm90Features[0] + glcm135Features[0]) / 4;
        double energy = (glcm0Features[1] + glcm45Features[1] + glcm90Features[1] + glcm135Features[1]) / 4;
        double entropy = (glcm0Features[2] + glcm45Features[2] + glcm90Features[2] + glcm135Features[2]) / 4;
        double homogeneity = (glcm0Features[3] + glcm45Features[3] + glcm90Features[3] + glcm135Features[3]) / 4;

        return new double[]{contrast, energy, entropy, homogeneity};
    }


}
