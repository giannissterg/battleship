package gr.ste.presentation.widgets;

import javafx.scene.layout.*;

public class Tile extends StackPane {
    public Tile(double width, double height) {
        setMinSize(width, height);
        //setOpacity(0.9);
    }
}