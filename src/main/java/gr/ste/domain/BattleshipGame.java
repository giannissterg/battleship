package gr.ste.domain;

import gr.ste.domain.entities.*;
import gr.ste.presentation.events.MoveEnteredEvent;
import javafx.application.Platform;

import java.util.*;

/**
 * Class that holds game logic
 */
public class BattleshipGame {
    private final List<Player> players;
    private int currentPlayerId;
    private int round;
    private boolean hasStarted;

    /**
     * Constructs a new 2-player battleship game
     * @param player1 First player of the game
     * @param player2 Second player of the game
     */
    public BattleshipGame(Player player1, Player player2) {
        this.players = new ArrayList<>(2);
        this.players.add(player1);
        this.players.add(player2);

        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                if(i != j) {
                    players.get(i).getPastMoves(j);
                }
            }

        }

        this.currentPlayerId = (new Random()).nextInt(players.size());
        this.hasStarted = false;
    }

    /**
     * Constructs a multiplayer game of arbirtary number of players
     * @param players List of game's players
     */
    public BattleshipGame(List<Player> players) {
        this.players = players;
        this.currentPlayerId = (new Random()).nextInt(players.size());
        this.hasStarted = false;
    }


    /**
     * Helper method to retrieve player instances from playerId
     * @param playerId Id of the player
     * @return An instance of player with id playerId
     */
    public Player findPlayerById(int playerId) {
        assert(playerId < players.size());
        return players.get(playerId);
    }


    /**
     * Getter for the current player instance
     * @return Instance of the currently active player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerId);
    }

    /**
     * Getter for the list of players
     * @return List of all the players of the game
     */
    public List<Player> getPlayers() { return players; }

    /**
     * Getter for the number of players
     * @return Number of players
     */
    public int getNumberOfPlayers() { return players.size(); }

    /**
     * Getter for the round
     * @return Current round number
     */
    public int getRound() { return round; }

    /**
     * First method that starts game
     * @return The first move if startingPlayer is an NPC, null otherwise
     */
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

    /**
     * Method that implements main logic of the application.
     * First checks if the move has not been played before.
     * After that it checks to find if position belongs to a targetPlayer's ship.
     * According to the result, it updates the game model
     * @param targetPlayerId PlayerId of the target
     * @param target Position of the target
     * @return True if move was played, otherwise false
     */
    public boolean play(int targetPlayerId, Position target) {
        Player currentPlayer = players.get(currentPlayerId);
        Player targetPlayer = players.get(targetPlayerId);

        // Check if move is already played
        for(Move move : currentPlayer.getPastMoves(targetPlayerId)) {
            if(move.getX() == target.getX() && move.getY() == target.getY()) {
                return false;
            }
        }

        ShipType shipType = ShipType.NONE;
        int reward = 0;
        for (Ship enemyShip : targetPlayer.getBoard().ships) {
            for (ShipPosition shipPart : enemyShip.getPositions()) {
                if (target.equals(shipPart)) {
                    shipType = findShipType(enemyShip);
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
                        // targetPlayer.getBoard().ships.remove(enemyShip);
                        if(currentPlayer.isNPC()) {
                            NPCPlayer ai = (NPCPlayer) currentPlayer;
                            ai.updateEnemyShipInformation(targetPlayerId, null);
                        }
                    }
                    break;
                }
            }
        }

        Move moveMade = new Move(target, shipType, targetPlayer.getId());
        if(shipType == ShipType.NONE) {
            targetPlayer.getBoard().addMissedShot(target);
        } else {
            currentPlayer.reward(reward);
        }
        currentPlayer.addMove(moveMade);

        round++;
        return true;
    }

    /**
     * Helper method to find ship's type
     * @param ship Instance of ship
     * @return Type of the given ship
     */
    private ShipType findShipType(Ship ship) {
        if(ship instanceof Carrier) {
            return ShipType.CARRIER;
        } else if(ship instanceof Battleship) {
            return ShipType.BATTLESHIP;
        } else if(ship instanceof Cruiser) {
            return ShipType.CRUISER;
        } else if(ship instanceof Submarine) {
            return ShipType.SUBMARINE;
        } else if (ship instanceof Destroyer){
            return ShipType.DESTROYER;
        } else {
            return ShipType.NONE;
        }
    }

    /**
     * Method that advances currentPlayerId to the next player
     */
    public void nextPlayer() {
        currentPlayerId = (currentPlayerId + 1) % players.size();
    }

    /**
     * Method that checks if the game has ended either by
     * reaching 40 * number of players rounds or either
     * because all but one players are all eliminated
     * @return True if the game has ended, otherwise false
     */
    public boolean hasEnded() {
        int eliminatedPlayers = 0;
        for (Player player : players) {
            if(player.isEliminated()) {
               eliminatedPlayers++;
            }
        }
        return eliminatedPlayers == getNumberOfPlayers() - 1 || round >= 2 * 40;
    }
}
