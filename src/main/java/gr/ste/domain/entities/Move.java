package gr.ste.domain.entities;

public class Move extends Position {

    private final boolean hit;
    private final int targetId;

    public Move(int x, int y, boolean hit, int targetId) {
        super(x, y);
        this.hit = hit;
        this.targetId = targetId;
    }

    public Move(Position position, boolean hit, int targetId) {
        super(position.getX(), position.getY());
        this.hit = hit;
        this.targetId = targetId;
    }

    public boolean isHit() {
        return hit;
    }

    public int getTargetId() {
        return targetId;
    }
}
