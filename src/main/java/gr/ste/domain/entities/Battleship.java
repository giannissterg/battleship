package gr.ste.domain.entities;

import javafx.geometry.Orientation;

public class Battleship extends Ship {
    public Battleship(String id, Position position, Orientation orientation) {
        super(id, position, orientation, 4, 250, 500);
    }
}
