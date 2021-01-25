package gr.ste.domain.entities;

import javafx.geometry.Orientation;

public class Carrier extends Ship {
    public Carrier(String id, Position position, Orientation orientation) {
        super(id, position, orientation, 5, 350, 1000);
    }
}
