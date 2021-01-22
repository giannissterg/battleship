package gr.ste.domain.models;

public class Ship {
    private final int id;
    private final Position position;
    private final int space;
    private final int damage;
    private final int sankScore;

    public Ship(int id, Position position, int space, int damage, int sankScore) {
        this.id = id;
        this.position = position;
        this.space = space;
        this.damage = damage;
        this.sankScore = sankScore;
    }

    public int getId() {
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
}
