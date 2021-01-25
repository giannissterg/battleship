package gr.ste.domain.entities;

import javafx.geometry.Orientation;

public class Destroyer extends Ship {
    public Destroyer(String id, Position position, Orientation orientation) {
        super(id, position, orientation, 2, 50, 0);
    }
}
