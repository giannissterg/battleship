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
                shipPosition = new ShipPosition(position.getX() + i, position.getY());
            } else if (orientation == Orientation.VERTICAL) {
                shipPosition = new ShipPosition(position.getX(), position.getY() + i);
            } else {
                shipPosition = null;
            }
            this.positions.add(shipPosition);
        }
    }

    public String getId() { return id; }
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
        boolean isSunk = false;
        for(ShipPosition shipPosition : positions) {
            if(shipPosition.getShipStatus() != ShipStatus.damaged && shipPosition.getShipStatus() != ShipStatus.sunk) {
                isSunk = false;
                break;
            } else {
                isSunk = true;
            }
        }

        if(isSunk) {
            positions.forEach(shipPosition -> shipPosition.setShipStatus(ShipStatus.sunk));
        }
        return isSunk;
    }

    public ShipStatus getState() {
        int countDamaged = 0;
        for(ShipPosition shipPosition : positions) {
            if(shipPosition.getShipStatus() == ShipStatus.damaged || shipPosition.getShipStatus() == ShipStatus.sunk) {
                countDamaged++;
            }
        }
        if(countDamaged == positions.size()) {
            return ShipStatus.sunk;
        }
        if(countDamaged > 0) {
            return ShipStatus.damaged;
        } else {
            return ShipStatus.untouched;
        }
    }
}
