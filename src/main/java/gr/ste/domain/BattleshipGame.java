package gr.ste.domain;

import gr.ste.domain.entities.Board;
import gr.ste.domain.entities.Player;
import gr.ste.domain.entities.Position;
import gr.ste.domain.entities.Ship;
import gr.ste.presentation.events.MoveEnteredEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class BattleshipGame implements Serializable {
    public final List<Player> players;
    public int currentPlayerId;

    public BattleshipGame(Player player1, Player player2) {
        this.players = new ArrayList<>(2);
        this.players.add(player1);
        this.players.add(player2);
        this.currentPlayerId = (new Random()).nextInt(players.size());
    }

    public Player findPlayerById(int playerId) {
        assert(playerId <= players.size());
        return this.players.get(playerId);
    }

    public Player getCurrentPlayer() {
        return this.players.get(currentPlayerId);
    }

    boolean isPlayerEliminated(int playerId) {
        for(Ship ship : players.get(playerId).getBoard().ships) {
            if(!ship.isSunk()) {
               return false;
            }
        }
        players.remove(playerId);
        return true;
    }

    public MoveEnteredEvent playRound() {
        Player currentPlayer = findPlayerById(currentPlayerId);
        if(currentPlayer.isNPC()) {
            int enemyId = selectRandomEnemy(currentPlayerId);
            Position targetPosition = selectTargetPosition(currentPlayerId, enemyId);
            MoveEnteredEvent move = new MoveEnteredEvent(targetPosition, enemyId);
            return move;
        } else {
            return null;
        }
    }

    public int selectRandomEnemy(int currentPlayerId) {
        Random rand = new Random();
        int targetId = rand.nextInt(players.size());
        while(targetId == currentPlayerId) {
            targetId = rand.nextInt(players.size());
        }
        return targetId;
    }

    public Position selectTargetPosition(int playerId, int enemyId) {
        Random rand = new Random();

        Position targetPosition;

        Stack<Position> pastMoves = players.get(playerId).getSuccessfulHits();
        if(!pastMoves.isEmpty()) {
            Position lastSuccessfulMove = pastMoves.peek();
            int randomDirection = rand.nextInt(4);
            if(randomDirection == 0) {
                targetPosition = new Position(lastSuccessfulMove.getX(), lastSuccessfulMove.getY() - 1);
            } else if(randomDirection == 1) {
                targetPosition = new Position(lastSuccessfulMove.getX() + 1, lastSuccessfulMove.getY());
            } else if(randomDirection == 2) {
                targetPosition = new Position(lastSuccessfulMove.getX(), lastSuccessfulMove.getY() + 1);
            } else {
                targetPosition = new Position(lastSuccessfulMove.getX() - 1, lastSuccessfulMove.getY());
            }
        } else {
            Board enemyBoard = players.get(enemyId).getBoard();
            int randomX = rand.nextInt(enemyBoard.getWidth());
            int randomY = rand.nextInt(enemyBoard.getHeight());
            targetPosition = new Position(randomX, randomY);
        }
        return targetPosition;
    }

    public void nextPlayer() {
        currentPlayerId = (currentPlayerId + 1) % players.size();
    }

    public boolean hasGameEnded() {
        return players.size() == 1;
    }
}
