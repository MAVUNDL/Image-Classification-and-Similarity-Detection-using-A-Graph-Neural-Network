package BackEnd.Algorithms.preprocessing.abstraction;

import BackEnd.Algorithms.preprocessing.utilization.Patch;
import BackEnd.DataStructures.Classes.ArrayListDS;
import BackEnd.DataStructures.Interfaces.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * This class extracts patches from a given image
 */
public class Patches {
    /**
     * This method will extract patches from a given image
     * @param image the given image
     * @return returns a list of patches
     */
    public ArrayList<Patch> extractPatchesFromImage(Image image, int patchHeight, int patchWidth, int windowSize){
        ArrayList<Patch> patches = new ArrayListDS<>();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        PixelReader pixelReader = image.getPixelReader();

        for(int y = 0; y <= height - patchHeight; y += windowSize){
            for(int x = 0; x <= width - patchWidth; x += windowSize){
                WritableImage writableImage = new WritableImage(patchWidth, patchHeight);
                PixelWriter pixelWriter = writableImage.getPixelWriter();

                for(int i = 0; i < patchHeight; i++){
                    for(int j = 0; j < patchWidth; j++){
                        int argb = pixelReader.getArgb(x + j, y + i);
                        pixelWriter.setArgb(j, i, argb);
                    }
                }
                patches.add(new Patch(writableImage));
            }
        }
        return patches;
    }

}
