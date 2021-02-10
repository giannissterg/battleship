package gr.ste.domain;

import gr.ste.domain.entities.Player;
import gr.ste.domain.entities.Ship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public void playRound() {
        Player currentPlayer = findPlayerById(currentPlayerId);
        if(currentPlayer.isNPC()) {
            currentPlayer.play();
        }
    }

    public void nextPlayer() {
        currentPlayerId = (currentPlayerId + 1) % players.size();
    }

    public boolean hasGameEnded() {
        return players.size() == 1;
    }
}
