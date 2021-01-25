package gr.ste.domain.entities;

import javafx.geometry.Orientation;

public class Ship {
    private final String id;
    private final Position position;
    private final Orientation orientation;
    private final int space;
    private final int damage;
    private final int sankScore;

    public Ship(String id, Position position, Orientation orientation, int space, int damage, int sankScore) {
        this.id = id;
        this.position = position;
        this.orientation = orientation;
        this.space = space;
        this.damage = damage;
        this.sankScore = sankScore;
    }

    public String getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public int getSpace() {
        return space;
    }

    public int getDamage() {
        return damage;
    }

    public int getSankScore() {
        return sankScore;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
