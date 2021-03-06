package gr.ste.domain.entities;

public class ShipPosition extends Position {
    private ShipStatus shipStatus;

    public ShipPosition(int x, int y) {
        super(x, y);
        this.shipStatus = ShipStatus.untouched;
    }

    public ShipPosition(int x, int y, ShipStatus shipStatus) {
        super(x, y);
        this.shipStatus = shipStatus;
    }

    public ShipStatus getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(ShipStatus newStatus) {
        this.shipStatus = newStatus;
    }
}
