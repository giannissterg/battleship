package gr.ste.domain.models;

public class Player {
    private int id;
    private int[] ships;
    private int score;
    private int successfulHits;

    public Player(int id, int[] ships, int score, int successfulHits) {
        this.id = id;
        this.ships = ships;
        this.score = score;
        this.successfulHits = successfulHits;
    }
}
