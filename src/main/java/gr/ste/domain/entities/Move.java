package gr.ste.domain.entities;

public class Move extends Position {

    private final int targetId;
    private final Ship hitShip;

    public Move(Position position, int targetId, Ship hitShip) {
        super(position.getX(), position.getY());
        this.targetId = targetId;
        this.hitShip = hitShip;
    }

    public boolean isHit() {
        return hitShip != null;
    }
    public int getTargetId() {
        return targetId;
    }
    public Ship getHitShip() {
        return hitShip;
    }
}
