package gr.ste.domain.entities;

import javafx.geometry.Orientation;

public class Cruiser extends Ship {
    public Cruiser(String id, Position position, Orientation orientation) {
        super(id, position, orientation, 3, 100, 250);
    }
}
