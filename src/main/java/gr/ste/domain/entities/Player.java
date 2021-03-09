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

    public Map<Integer, Double> computePercentages() {
        Map<Integer, Double> percentages = new HashMap<>();
        for (Map.Entry<Integer, Stack<Move>> entry : pastMovesMap.entrySet()) {
            Stack<Move> moves = entry.getValue();
            double percentage = 0.0;
            int hits = 0;
            int totalShots = 0;
            for (Move move : moves) {
                if (move.isHit()) {
                    hits++;
                }
                totalShots++;
            }
            if(totalShots != 0) {
                percentage = hits * 1.0 / totalShots;
            }
            percentages.put(entry.getKey(), percentage);
        }
        return percentages;
    }

    public Map<Integer, Stack<Move>> getMovesMap() {
        return pastMovesMap;
    }

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

    public boolean isEliminated() {
        boolean isEliminated = false;
        for (Ship ship : board.ships) {
            if(ship.isSunk()) {
                isEliminated = true;
            } else {
                isEliminated = false;
                break;
            }
        }
        return isEliminated;
    }
}
