package FrontEnd.interfaces;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.text.TextAlignment;

public interface LabelUI {
    /**
     * @param text this method sets the text of a label
     */
    void addTextToLabel(String text);

    /**
     * @return returns a read only property of the width of the label
     */
    ReadOnlyDoubleProperty WidthProperty();

    /**
     * @return returns a read only property of the height of the label
     */
    ReadOnlyDoubleProperty HeightProperty();

    /**
     * @return returns the x property of the label
     */
    DoubleProperty xPosition();

    /**
     * @return returns the y property of the label
     */
    DoubleProperty yPosition();

    /**
     * This method adds a css styling to the label
     * @param css css style for the label
     */
    void styleLabel(String css);
}
