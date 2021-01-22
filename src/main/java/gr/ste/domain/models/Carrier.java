package gr.ste.domain.models;

public class Carrier extends Ship {
    public Carrier(int id, Position position) {
        super(id, position, 5, 350, 1000);
    }
}
