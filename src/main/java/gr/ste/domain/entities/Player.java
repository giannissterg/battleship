package gr.ste.domain.entities;

import java.util.List;

public class Player {
    private final int id;
    private final String name;
    private int score;
    private int successfulHits;
    private List<Ship> ships;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.score = 0;
        this.successfulHits = 0;
    }

    public Player(int id, String name, int score, int successfulHits) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.successfulHits = successfulHits;
    }

}
