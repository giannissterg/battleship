package gr.ste.domain.entities;

public class Player {
    private final String name;
    private final PlayerType type;
    private int score;
    private int successfulHits;
    private Board board;

    public Player(String name, Board board, PlayerType playerType) {
        this.name = name;
        this.score = 0;
        this.successfulHits = 0;
        this.board = board;
        this.type = playerType;
    }

    public Player(String name, int score, int successfulHits, PlayerType playerType) {
        this.name = name;
        this.score = score;
        this.successfulHits = successfulHits;
        this.type = playerType;
    }

    public boolean isNPC() { return type == PlayerType.npc; }

    public Board getBoard() {
        return board;
    }
}
