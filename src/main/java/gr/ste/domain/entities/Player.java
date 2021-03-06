package gr.ste.domain.entities;

import java.util.HashMap;
import java.util.Stack;

public class Player {
    private final int id;
    private final String name;
    private final PlayerType type;
    private int score;
    private Stack<Move> pastMoves;
    private Board board;

    public Player(int id, String name, Board board, PlayerType playerType) {
        this.id = id;
        this.name = name;
        this.score = 0;
        this.pastMoves = new Stack<Move>();
        this.board = board;
        this.type = playerType;
    }

    public boolean isNPC() { return type == PlayerType.npc; }

    public Board getBoard() {
        return board;
    }

//    public void updateBoard(Move moveMade) {
//        if(moveMade.isHit()) {
//            board.updateShip(moveMade);
//        } else {
//            board.addMissedShot(moveMade);
//        }
//    }

    public Stack<Move> getPastMoves() {
        return pastMoves;
    }

    public void addMove(Move move) {
        pastMoves.add(move);
    }

    public void reward(int reward) {
        this.score += reward;
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
