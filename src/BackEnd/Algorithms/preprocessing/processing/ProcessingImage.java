package BackEnd.Algorithms.preprocessing.processing;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * This class defines algorithms to process an image
 */
public class ProcessingImage {
    private final int[][] gaussian = {
            {1, 2, 1},
            {2, 4, 2},
            {1, 2, 1}
    };

    private final int[][] vertical = {
            {1, 2, 1},
            {0, 0, 0},
            {-1, -2, -1}
    };

    private final int[][] horizontal = {
            {1, 0, -1},
            {2, 0, -2},
            {1, 0 ,-1}
    };

    /**
     * This method performs gaussian blurring on the image to reduce noise
     * @param image the image to be blurred
     * @return returns the blurred image
     */
    public Image gaussianBlurImage(Image image){
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        PixelReader reader = image.getPixelReader();
        WritableImage blurredImage = new WritableImage(width, height); // build new image frame
        PixelWriter writer = blurredImage.getPixelWriter(); // writer to frame

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int blurred = applyWeights(reader, width, height, x, y, gaussian); // blur each pixel
                int argb = (0xff << 24) | (blurred << 16) | (blurred << 8) | blurred; // reconstruct a pixel
                writer.setArgb(x, y, argb);
            }
        }
        return blurredImage;
    }

    /**
     * This method performs a canny edge detection on the given image
     * @param image image to be  edge detected
     * @return returns an edge detected representation of the image
     */
    public Image cannyEdgeImage(Image image){
        int width = (int) image.getWidth(), height = (int) image.getHeight();
        PixelReader reader = image.getPixelReader();;
        // new image
        WritableImage edgeDetectedImage = new WritableImage(width, height);
        PixelWriter writer = edgeDetectedImage.getPixelWriter();

        // for gradient computations
        double[][] intensities = new double[width][height], directions = new double[width][height];
        gradientComputation(reader, intensities, directions); // compute
        // compute non maximum suppression
        double[][] nonMaximumSuppressionMatrix = computeNonMaximumSuppression(intensities, directions);
        // thresholding
        double highThreshold = findMaximumIntensity(intensities) * 0.09;
        double lowThreshold = highThreshold * 0.05;
        // compute edge map
        int[][] edgeMap = edgeTrackingByHysteresis(nonMaximumSuppressionMatrix, highThreshold, lowThreshold);

        for(int y = 0; y < height; y++){ // build  canny edge detected image
            for(int x = 0; x < width; x++){
                if(edgeMap[y][x] == 255){
                   writer.setArgb(x, y, 0xFFFFFFFF);
                } else {
                    writer.setArgb(x, y, 0xFF000000);
                }
            }
        }
        return edgeDetectedImage;
    }

    /**
     * This method converts the given image into gray scale image
     * @param image input image
     * @return returns gray scaled image
     */
    public Image grayScaleImage(Image image){
        int width = (int) image.getWidth(), height = (int) image.getHeight();
        PixelReader reader = image.getPixelReader();;
        // new image
        WritableImage grayedImage = new WritableImage(width, height);
        PixelWriter writer = grayedImage.getPixelWriter();

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int argb = reader.getArgb(x, y);
                int alpha = (argb >> 24) &0xff, red = (argb >> 16) & 0xff, green = (argb >> 8) & 0xff, blue = (argb) & 0xff;
                int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
                int pixel = (alpha << 24) | (gray << 16) | (gray << 8) | gray;
                writer.setArgb(x, y, pixel);
            }
        }
        return grayedImage;
    }

    /**
     * This method counts the number of edge pixels in the edge detected image
     * @param image  detected image
     * @return returns the number of edges pixels in the image
     */
    public double computeEdgeDensity(Image image){
        int width = (int) image.getWidth(), height = (int) image.getHeight();
        PixelReader reader = image.getPixelReader();
        int edgeCounter = 0;

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
               if(reader.getArgb(x,y) == 0xFFFFFFFF){
                   edgeCounter++;
               }
            }
        }
        return (double)  edgeCounter / (height * width);
    }

    /**
     * This method applies weights on the surrounding pixels of the given one and sums them
     * @param reader pixel reader to get the neighbouring pixels of a given one
     * @param width the width of the image
     * @param height the height of the image
     * @param xCoordinate the x position of the current pixel in the image
     * @param yCoordinate the y position of the current pixel in the image
     * @param weights the matrix consisting of the weights to be applied on neighbour pixels
     * @return returns the computed sum
     */
    private int applyWeights(PixelReader reader, int width, int height, int xCoordinate, int yCoordinate, int[][] weights){
        int computation = 0;
        for(int y = - 1; y <= 1; y++){
            for(int x = -1; x <= 1; x++){
                int neighbourX = xCoordinate + x, neighbourY = yCoordinate + y;
                if(neighbourX >= 0 && neighbourX < width && neighbourY >=0 && neighbourY < height){
                    int neighbourArgb = reader.getArgb(neighbourX, neighbourY);
                    int red = (neighbourArgb >> 16) & 0xff, green = (neighbourArgb >> 8) & 0xff, blue = (neighbourArgb) & 0xff, weight = weights[y + 1][x + 1];
                    computation += (red * weight + green * weight + blue * weight) / 3; // multiply each channel by the current weight, sum and average
                }
            }
        }
        return computation;
    }

    /**
     * This method computes the gradient calculation, computing the gradient and its direction
     * @param reader pixel reader to get the surrounding pixels to apply weights
     * @param intensities an array to store the computed gradient intensities
     * @param directions an array to store the computed gradient directions
     */
    private void gradientComputation(PixelReader reader, double[][] intensities, double[][] directions){
        for(int y = 0; y < intensities.length; y++){
            for(int x = 0; x < intensities[0].length; x++){
                int verticalEdge = applyWeights(reader, intensities.length, intensities[0].length, x, y, vertical);
                int horizontalEdge = applyWeights(reader, intensities.length, intensities[0].length, x, y, horizontal);
                double EuclideanDistance = Math.sqrt(((verticalEdge * verticalEdge) + (horizontalEdge * horizontalEdge)));
                double direction =  Math.atan((double) horizontalEdge / verticalEdge);
                intensities[y][x] = EuclideanDistance;
                directions[y][x] = direction;
            }
        }
    }

    /**
     * This method computes the Non-maximum suppression to thin out the edges by keeping only the strongest pixels contributing to an edge
     * @param intensities matrix of gradient pixel intensities
     * @param directions matrix of gradient pixel directions
     * @return returns a matrix with gradients contributing to the edges
     */
    private double[][] computeNonMaximumSuppression(double[][] intensities, double[][] directions){
        double[][] nonMaximumMatrix = new double[intensities.length][intensities[0].length];

        for(int x = 1; x < intensities.length - 1; x++){
            for(int y = 1; y < intensities[0].length - 1; y++){
                double angle = directions[x][y] * (180 / Math.PI);
                if(angle < 0) angle += 180; // normalize negative angles
                double nextPixel = 255, beforePixel = 255;

                if((angle >= 0 && angle < 22.5) || (angle >= 157.5 && angle < 180)) { // 0 degrees
                    nextPixel = intensities[x][y + 1];
                    beforePixel = intensities[x][y - 1];
                }

                else if((angle >= 22.5 && angle < 67.5)){ // 45 degrees
                    nextPixel = intensities[x + 1][y - 1];
                    beforePixel = intensities[x - 1][y + 1];
                }

                else if((angle >= 67.5 && angle < 112.5)){ // 90 degrees
                    nextPixel = intensities[x + 1][y];
                    beforePixel = intensities[x - 1][y];
                }

                else if((angle >= 112.5 && angle < 157.5)){ // 135 degrees
                    nextPixel = intensities[x - 1][y - 1];
                    beforePixel = intensities[x + 1] [y + 1];
                }

                // now if the current pixel's intensity if greater or equal to the next pixel in the same direction and before
                if(intensities[x][y] >= nextPixel && intensities[x][y] >= beforePixel) {
                    nonMaximumMatrix[x][y] = intensities[x][y]; // keep it
                } else {
                    nonMaximumMatrix[x][y] = 0.0; // else remove it
                }
            }
        }
        return nonMaximumMatrix;
    }

    /**
     * This method iterates through all the intensities and finds the maximum one
     * @param intensities matrix of intensities
     * @return returns the maximum intensity
     */
    private double findMaximumIntensity(double[][] intensities){
        double max = 0;
        for(int x = 0; x < intensities.length; x++){
            for(int y = 0; y < intensities[0].length; y++){
                if(intensities[x][y] > max){
                    max = intensities[x][y];
                }
            }
        }
        return max;
    }

    /**
     * This method performs the edge tracking using hysteresis to identify weak, strong and non-relevant pixels, the check if a weak one is surrounded by a strong pixel to transform it
     * @param nonMaximumSuppressionMatrix  matrix with suppressed intensities
     * @param highThreshold high threshold for strong pixels
     * @param lowThreshold low threshold for weak pixels
     * @return returns an edge map
     */
    private int[][] edgeTrackingByHysteresis(double[][] nonMaximumSuppressionMatrix, double highThreshold, double lowThreshold){
        int cols = nonMaximumSuppressionMatrix[0].length, rows = nonMaximumSuppressionMatrix.length;
        int strongPixel = 255, weakPixel = 50, nonRelevant = 0;
        int[][] doubleThresholdMatrix = new int[rows][cols];

        for(int x = 0; x < rows; x++){ // identity strong, weak and non relevant pixels
            for(int y = 0; y < cols; y++){
                double suppressed = nonMaximumSuppressionMatrix[x][y];
                if(suppressed >= highThreshold){
                    doubleThresholdMatrix[x][y] = strongPixel;
                } else  if(suppressed <= lowThreshold){
                    doubleThresholdMatrix[x][y] = weakPixel;
                } else {
                    doubleThresholdMatrix[x][y] = nonRelevant;
                }
            }
        }
        // for each weak pixel, check if it surrounded by strong pixels, if so transform it to a strong pixel, else its non-relevant
        int[][] resultingEdgeMap = new int[rows][cols];
        for(int x = 0; x < rows; x++){
            for(int y = 0; y < cols; y++){
                if(doubleThresholdMatrix[x][y] == weakPixel){
                    boolean isSurrounded = false;
                    for(int r = -1; r <= 1; r++){
                        for(int c = -1; c <= 1; c++){
                            int xNew = x +  r;
                            int yNew = y + c;
                            if(xNew >=0 && xNew < rows && yNew >= 0 && yNew < cols){
                                if(doubleThresholdMatrix[x +  r][y + c] == strongPixel){
                                    isSurrounded = true;
                                    break;
                                }
                            }
                        }
                        if(isSurrounded) break;;
                    }
                    resultingEdgeMap[x][y] = isSurrounded ? strongPixel : nonRelevant; // transform if surrounded else make it non relevant
                } else if(doubleThresholdMatrix[x][y] == strongPixel){
                    resultingEdgeMap[x][y] = strongPixel;
                } else {
                    resultingEdgeMap[x][y] = nonRelevant;
                }
            }
        }
        return resultingEdgeMap;
    }
}
