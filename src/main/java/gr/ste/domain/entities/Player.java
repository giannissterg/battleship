package gr.ste.domain.entities;

import java.util.*;

public class Player {
    private final int id;
    private final String name;
    private final PlayerType type;
    private int score;
    private final Map<Integer, Stack<Move>> pastMovesMap;
    private final Board board;

    public Player(int id, String name, Board board, PlayerType playerType) {
        this.id = id;
        this.name = name;
        this.score = 0;
        this.pastMovesMap = new HashMap<>();
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

    public Stack<Move> getPastMoves(int enemyId) {
        pastMovesMap.computeIfAbsent(enemyId, k -> new Stack<>());
        return pastMovesMap.get(enemyId);
    }

    public List<Position> getAvailableMoves(int enemyId) {
        List<Position> availableMoves = new ArrayList<>(Board.WIDTH * Board.HEIGHT);
        for(int y = 0; y < board.getHeight(); y++) {
            for(int x = 0; x < board.getWidth(); x++) {
                boolean isAvailable = true;
                for(Move move : pastMovesMap.get(enemyId)) {
                    if(move.getX() == x && move.getY() == y) {
                        isAvailable = false;
                        break;
                    }
                }
                if(isAvailable) {
                    availableMoves.add(new Position(x, y));
                }
            }
        }
        return availableMoves;
    }

    public void addMove(Move move) {
        pastMovesMap.get(move.getTargetId()).add(move);
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
