package gr.ste.domain.entities;

import javafx.geometry.Orientation;

public class Submarine extends Ship {
    public Submarine(String id, Position position, Orientation orientation) {
        super(id, position, orientation, 3, 100, 0);
    }
}
