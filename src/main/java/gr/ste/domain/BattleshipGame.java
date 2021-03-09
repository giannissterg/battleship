package gr.ste.domain;

import gr.ste.domain.entities.*;
import gr.ste.presentation.events.MoveEnteredEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleshipGame {
    private final List<Player> players;
    private int currentPlayerId;
    private int round;
    private boolean hasStarted;

    public BattleshipGame(Player player1, Player player2) {
        this.players = new ArrayList<>(2);
        this.players.add(player1);
        this.players.add(player2);
        this.currentPlayerId = (new Random()).nextInt(players.size());
        this.hasStarted = false;
    }

    public BattleshipGame(List<Player> players) {
        this.players = players;
        this.currentPlayerId = (new Random()).nextInt(players.size());
        this.hasStarted = false;
    }

    public Player findPlayerById(int playerId) {
        assert(playerId < players.size());
        return players.get(playerId);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerId);
    }
    public List<Player> getPlayers() { return players; }
    public int getNumberOfPlayers() { return players.size(); }
    public int getRound() { return round; }

    public boolean isPlayerEliminated(int playerId) {
        for(Ship ship : findPlayerById(playerId).getBoard().ships) {
            if(!ship.isSunk()) {
               return false;
            }
        }
        players.remove(findPlayerById(playerId));
        return true;
    }

    public MoveEnteredEvent start() {
        hasStarted = true;
        Player startingPlayer = findPlayerById(currentPlayerId);
        if (startingPlayer.isNPC()) {
            NPCPlayer ai = (NPCPlayer) startingPlayer;
            MoveEnteredEvent aiMove = ai.chooseMove(players);
            return aiMove;
        } else {
            return null;
        }
    }

    public boolean play(int targetPlayerId, Position target) {
        Player currentPlayer = players.get(currentPlayerId);
        Player targetPlayer = players.get(targetPlayerId);

        // Check if move is already played
        for(Move move : currentPlayer.getPastMoves(targetPlayerId)) {
            if(move.getX() == target.getX() && move.getY() == target.getY()) {
                return false;
            }
        }

        boolean hitShip = false;
        int reward = 0;
        for (Ship enemyShip : targetPlayer.getBoard().ships) {
            for (ShipPosition shipPart : enemyShip.getPositions()) {
                if (target.equals(shipPart)) {
                    hitShip = true;
                    reward += enemyShip.getDamage();
                    shipPart.setShipStatus(ShipStatus.damaged);
                    if (currentPlayer.isNPC()) {
                        NPCPlayer ai = (NPCPlayer) currentPlayer;
                        EnemyShipInformation shipInformation = ai.getEnemyShipInformation(targetPlayerId);
                        if(shipInformation != null) {
                            shipInformation.addShipLocation(shipPart);
                        } else {
                            ai.updateEnemyShipInformation(targetPlayerId, new EnemyShipInformation(shipPart));
                        }
                    }

                    if(enemyShip.isSunk()) {
                        reward += enemyShip.getSankScore();
//                        targetPlayer.getBoard().ships.remove(enemyShip);
                        if(currentPlayer.isNPC()) {
                            NPCPlayer ai = (NPCPlayer) currentPlayer;
                            ai.updateEnemyShipInformation(targetPlayerId, null);
                        }
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

        round++;
        return true;
    }

    public void nextPlayer() {
        currentPlayerId = (currentPlayerId + 1) % players.size();
    }

    public boolean hasEnded() {
        int eliminatedPlayers = 0;
        for (Player player : players) {
            if(player.isEliminated()) {
               eliminatedPlayers++;
            }
        }
        return eliminatedPlayers == getNumberOfPlayers() - 1 || round >= 2 * 40;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }
}
