package FrontEnd.classes;

import FrontEnd.interfaces.LabelUI;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class LabelNode extends Text implements LabelUI {
    private DoubleProperty width;
    private DoubleProperty height;

    public LabelNode(String text){
        super(5, 5, text);
        this.height = new SimpleDoubleProperty();
        this.width = new SimpleDoubleProperty();
        super.setFont(Font.font(20));
        super.setTextAlignment(TextAlignment.CENTER);
        layoutUpdater();
    }

    /**
     * This method defines an event lister to recalculate the layout bounds of the label, when ever they change
     */
    private void layoutUpdater(){
        layoutBoundsProperty().addListener(((_, _, value) -> {
            if(value != null){
                // check if the current with is the same as the new one, update if it is not
                if(Double.compare(this.width.doubleValue(), value.getWidth()) != 0){
                    this.width.setValue(value.getWidth());
                }
                // check if the current height is the same as the new one, update if it is not
                if(Double.compare(this.height.doubleValue(), value.getHeight()) != 0){
                    this.height.setValue(value.getHeight());
                }
            }
        }));
    }

    /**
     * @param text this method sets the text of a label
     */
    @Override
    public void addTextToLabel(String text) {
        this.setText(text);
    }

    /**
     * @return returns a read only property of the width of the label
     */
    @Override
    public ReadOnlyDoubleProperty WidthProperty() {
        return width;
    }

    /**
     * @return returns a read only property of the height of the label
     */
    @Override
    public ReadOnlyDoubleProperty HeightProperty() {
        return height;
    }

    /**
     * @return returns the x property of the label
     */
    @Override
    public DoubleProperty xPosition() {
        return this.xProperty();
    }

    /**
     * @return returns the y property of the labl
     */
    @Override
    public DoubleProperty yPosition() {
        return this.yProperty();
    }

    /**
     * This method adds a css styling to the label
     *
     * @param css css style for the label
     */
    @Override
    public void styleLabel(String css) {
        this.setStyle(css);
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
