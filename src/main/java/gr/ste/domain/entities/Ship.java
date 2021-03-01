package gr.ste.domain.entities;

import javafx.geometry.Orientation;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private final String id;
    private final int space;
    private final Orientation orientation;
    private final List<ShipPosition> positions;
    private final int damage;
    private final int sankScore;

    public Ship(String id, Position position, Orientation orientation, int space, int damage, int sankScore) {
        this.id = id;
        this.space = space;
        this.orientation = orientation;
        this.positions = new ArrayList<>(this.space);
        this.damage = damage;
        this.sankScore = sankScore;

        for (int i = 0; i < space; i++) {
            final ShipPosition shipPosition;
            if (orientation == Orientation.HORIZONTAL) {
                shipPosition = new ShipPosition(position.getX(), position.getY() + i);
            } else if (orientation == Orientation.VERTICAL) {
                shipPosition = new ShipPosition(position.getX() + i, position.getY());
            } else {
                shipPosition = null;
            }
            this.positions.add(shipPosition);
        }
    }


    public String getId() {
        return id;
    }

    public int getSpace() {
        return space;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public int getDamage() {
        return damage;
    }

    public int getSankScore() {
        return sankScore;
    }

    public List<ShipPosition> getPositions() {
        return positions;
    }

    public boolean isSunk() {
        return positions.get(0).getShipStatus() == ShipStatus.sunk;
    }
}
