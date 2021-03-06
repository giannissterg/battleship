package gr.ste.domain;

import gr.ste.domain.entities.*;
import gr.ste.presentation.events.MoveEnteredEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class BattleshipGame implements Serializable {
    private final List<Player> players;
    private int currentPlayerId;
    private int round;

    public BattleshipGame(Player player1, Player player2) {
        this.players = new ArrayList<>(2);
        this.players.add(player1);
        this.players.add(player2);
        this.currentPlayerId = (new Random()).nextInt(players.size());
    }

    public Player findPlayerById(int playerId) {
        assert(playerId < players.size());
        return this.players.get(playerId);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerId);
    }

    public List<Player> getPlayers() { return players; }
    public int getNumberOfPlayers() { return players.size(); }

    public boolean isPlayerEliminated(int playerId) {
        for(Ship ship : players.get(playerId).getBoard().ships) {
            if(!ship.isSunk()) {
               return false;
            }
        }
        players.remove(playerId);
        return true;
    }

    public MoveEnteredEvent playRound() {
        Player currentPlayer = getCurrentPlayer();
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
        Stack<Move> pastMoves = players.get(playerId).getPastMoves();
        if(!pastMoves.isEmpty()) {
            Move lastMove = pastMoves.peek();
//            while(lastMove.getTargetId() != enemyId || !lastMove.isHit()) {
//                pastMoves.pop();
//                lastMove = pastMoves.peek();
//            }
            int randomDirection = rand.nextInt(4);
            if(randomDirection == 0) {
                targetPosition = new Position(lastMove.getX(), lastMove.getY() - 1);
            } else if(randomDirection == 1) {
                targetPosition = new Position(lastMove.getX() + 1, lastMove.getY());
            } else if(randomDirection == 2) {
                targetPosition = new Position(lastMove.getX(), lastMove.getY() + 1);
            } else {
                targetPosition = new Position(lastMove.getX() - 1, lastMove.getY());
            }
            while(pastMoves.contains(targetPosition)) {
                targetPosition = selectTargetPosition(playerId, enemyId);
            }
        } else {
            Board enemyBoard = players.get(enemyId).getBoard();
            int randomX = rand.nextInt(enemyBoard.getWidth());
            int randomY = rand.nextInt(enemyBoard.getHeight());
            targetPosition = new Position(randomX, randomY);
        }
        return targetPosition;
    }

    public void play(int targetPlayerId, Position target) {
        Player currentPlayer = players.get(currentPlayerId);
        Player targetPlayer = players.get(targetPlayerId);

        boolean hitShip = false;
        int reward = 0;
        for (Ship enemyShip : targetPlayer.getBoard().ships) {
            for (ShipPosition shipPart : enemyShip.getPositions()) {
                if (target.equals(shipPart)) {
                    hitShip = true;
                    reward += enemyShip.getDamage();

                    shipPart.setShipStatus(ShipStatus.damaged);
                    if(enemyShip.isSunk()) {
                        reward += enemyShip.getSankScore();
                        targetPlayer.getBoard().ships.remove(enemyShip);
                    }
                    break;
                }
            }
        }

        Move moveMade = new Move(target, hitShip, targetPlayer.getId());
        if(!hitShip) {
            targetPlayer.getBoard().addMissedShot(target);
        } else {
            currentPlayer.reward(reward);
        }
        currentPlayer.addMove(moveMade);

        nextPlayer();
    }

    public void nextPlayer() {
        currentPlayerId = (currentPlayerId + 1) % players.size();
    }

    public boolean hasGameEnded() {
        return players.size() == 1 || round >= 40;
    }
}
