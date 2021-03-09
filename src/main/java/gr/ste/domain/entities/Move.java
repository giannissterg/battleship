package gr.ste.domain.entities;

public class Move extends Position {

    private final int targetId;
    private final ShipType shipType;

    public Move(int x, int y, ShipType shipType, int targetId) {
        super(x, y);
        this.shipType = shipType;
        this.targetId = targetId;
    }

    public Move(Position position, ShipType shipType, int targetId) {
        super(position.getX(), position.getY());
        this.shipType = shipType;
        this.targetId = targetId;
    }

    public boolean isHit() {
        return shipType != ShipType.NONE;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public int getTargetId() {
        return targetId;
    }
}
