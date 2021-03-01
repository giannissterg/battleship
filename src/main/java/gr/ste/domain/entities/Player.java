package gr.ste.domain.entities;

import java.util.Stack;

public class Player {
    private final int id;
    private final String name;
    private final PlayerType type;
    private int score;
    private Stack<Position> successfulHits;
    private Board board;

    public Player(int id, String name, Board board, PlayerType playerType) {
        this.id = id;
        this.name = name;
        this.score = 0;
        this.successfulHits = new Stack<>();
        this.board = board;
        this.type = playerType;
    }

    public boolean isNPC() { return type == PlayerType.npc; }

    public Board getBoard() {
        return board;
    }

    public Stack<Position> getSuccessfulHits() {
        return successfulHits;
    }

    public void addSuccessfulHit(Position p) {
        successfulHits.add(p);
    }
    public void setSuccessfulHits(Stack<Position> successfulHits) {
        this.successfulHits = successfulHits;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
