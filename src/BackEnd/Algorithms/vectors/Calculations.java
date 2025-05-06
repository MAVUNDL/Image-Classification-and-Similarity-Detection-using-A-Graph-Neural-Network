package BackEnd.Algorithms.vectors;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * This abstract class defines algorithms that are applied on vectors
 */
public class Calculations {
    /**
     * This method calculates the Euclidean distance between two vectors
     * @param vectorX first vector
     * @param vectorY second vector
     * @return returns the calculated Euclidean distance
     */
    public double EuclideanDistance(double[] vectorX, double[] vectorY) throws VectorException{
        if(vectorX.length != vectorY.length){
            throw new VectorException("The length of the vectors must be equal");
        }

        double sum = 0;
        for(int i = 0; i < vectorX.length; i++){
            sum+= Math.pow(vectorX[i] - vectorY[i], 2);
        }
        return Math.sqrt(sum);
    }

    /**
     * This method resizes an image based on the desired width and height
     * @param image image to be resized
     * @param width desired width
     * @param height desired height
     * @return returns the new resized image
     */
    public Image resize(Image image, int width, int height) throws  VectorException{
        if(image == null){
            throw new VectorException("Image must not be null for this operation");
        }

        assert width >= 0 && height >= 0;
        WritableImage writableImage = new WritableImage(width, height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        double xScale = image.getWidth() / (double) width;
        double yScale = image.getHeight() / (double) height;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int xCoordinate = (int) (x * xScale);
                int yCoordinate = (int) (y * yScale);

                pixelWriter.setArgb(x, y, pixelReader.getArgb(xCoordinate, yCoordinate));
            }
        }

        return writableImage;
    }

    /**
     * This method converts the given image to a 1D array of gray value pixels
     * @param image the given image
     * @return returns a 1D array of pixels from the image
     */
    public int[] imageTo1D(Image image) throws VectorException{
        if(image == null){
            throw new VectorException("Image must not be null for this operation");
        }

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        int[] oneDArray = new int[width * height];
        PixelReader pixelReader = image.getPixelReader();

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int argb = pixelReader.getArgb(x,y);
                int red = (argb >> 16) & 0xFF;
                int green = (argb >> 8) & 0xFF;
                int blue = argb & 0xFF;
                oneDArray[y * width + x] = (int) (0.299 * red + 0.587 * green + 0.144 * blue);
            }
        }
        return oneDArray;
    }

}
