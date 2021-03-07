package gr.ste.presentation.widgets;

import javafx.scene.layout.StackPane;

public class Tile extends StackPane {

    public double width;
    public double height;

    public Tile(double width, double height) {
        setMinSize(width, height);
        //setOpacity(0.9);
    }

    public void highlight() {
        // ensure the style is only once in the style list
        getStyleClass().remove("cell-highlight");

        // add style
        getStyleClass().add("cell-highlight");
    }

    public void unhighlight() {
        getStyleClass().remove("cell-highlight");
    }

    public void hoverHighlight() {
        // ensure the style is only once in the style list
        getStyleClass().remove("cell-hover-highlight");

        // add style
        getStyleClass().add("cell-hover-highlight");
    }

    public void hoverUnhighlight() {
        getStyleClass().remove("cell-hover-highlight");
    }

//    public String toString() {
//        return this.column + "/" + this.row;
//    }
}