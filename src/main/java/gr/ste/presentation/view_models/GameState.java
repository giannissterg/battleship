package gr.ste.presentation.view_models;

import gr.ste.domain.BattleshipGame;
import gr.ste.domain.entities.Board;
import gr.ste.domain.entities.Move;
import gr.ste.domain.entities.Player;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    public final IntegerProperty numberOfPlayers;
    public final IntegerProperty currentPlayer;
    public final IntegerProperty rounds;
    public final List<PlayerState> playerStates;

    public final StringProperty invalidMove;
    public final ReadOnlyBooleanWrapper showInvalidMoveLabel;
    public final BooleanProperty hasStartedGame;

    public final StringProperty xTargetCoordinate;
    public final StringProperty yTargetCoordinate;

    public final BooleanProperty showEndDialog;

    public GameState(int initialPlayers) {
        this.playerStates = new ArrayList<>();
        for(int i = 0; i < initialPlayers; i++) {
            PlayerState playerState = new PlayerState(i);
            playerStates.add(playerState);
        }

        for(int i = 0; i < initialPlayers; i++) {
            for(int j = 0; j < initialPlayers; j++) {
                if(i != j) {
                    playerStates.get(i).getPercentage(j);
                    playerStates.get(i).getMoves(j);
                }
            }
        }

        this.rounds = new SimpleIntegerProperty(0);
        this.currentPlayer = new SimpleIntegerProperty(0);
        this.numberOfPlayers = new SimpleIntegerProperty(initialPlayers);

        this.invalidMove = new SimpleStringProperty();
        this.showInvalidMoveLabel = new ReadOnlyBooleanWrapper(false);
        this.showInvalidMoveLabel.bind(invalidMove.isNotEmpty());
        this.showEndDialog = new SimpleBooleanProperty(false);

        this.hasStartedGame = new SimpleBooleanProperty(false);
        this.xTargetCoordinate = new SimpleStringProperty();
        this.yTargetCoordinate = new SimpleStringProperty();

        this.xTargetCoordinate.addListener(this::validateXCoordinate);
        this.yTargetCoordinate.addListener(this::validateYCoordinate);
    }

    public GameState(BattleshipGame game) {
        this.playerStates = new ArrayList<>();
        for(Player player : game.getPlayers()) {
            PlayerState playerState = new PlayerState(player);
            playerStates.add(playerState);
        }

        this.rounds = new SimpleIntegerProperty(game.getRound());
        this.currentPlayer = new SimpleIntegerProperty(game.getCurrentPlayer().getId());
        this.numberOfPlayers = new SimpleIntegerProperty(game.getNumberOfPlayers());

        this.invalidMove = new SimpleStringProperty();
        this.showInvalidMoveLabel = new ReadOnlyBooleanWrapper(false);
        this.showInvalidMoveLabel.bind(invalidMove.isNotEmpty());
        this.showEndDialog = new SimpleBooleanProperty(false);

        this.hasStartedGame = new SimpleBooleanProperty(false);
        this.xTargetCoordinate = new SimpleStringProperty();
        this.yTargetCoordinate = new SimpleStringProperty();

        this.xTargetCoordinate.addListener(this::validateXCoordinate);
        this.yTargetCoordinate.addListener(this::validateYCoordinate);
    }

    public void update(BattleshipGame game) {
        this.rounds.setValue(0);
        this.showEndDialog.setValue(false);
        for(Player player : game.getPlayers()) {
            playerStates.get(player.getId()).update(player);
        }
    }

    // Update round
    // Update moves
    // Update score
    // Update percentage
    // Check for errors
    // Next  player
    public void update(Player current, int enemyId) {
        invalidMove.setValue(null);

        currentPlayer.setValue(current.getId());
        PlayerState currentPlayerState = playerStates.get(current.getId());

        Move lastMove = current.getPastMoves(enemyId).lastElement();
        currentPlayerState.getMoves(enemyId).add(lastMove);

        currentPlayerState.score.setValue(current.getScore());
        if(current.getId() % numberOfPlayers.getValue() == 0) {
            rounds.setValue(rounds.getValue() + 1);
        }

        currentPlayerState.activeShips.setValue(current.getBoard().getActiveShips());
        Double updatedPercentage = current.computePercentages().get(enemyId);
        currentPlayerState.getPercentage(enemyId).setValue(updatedPercentage);
    }

    public void validateXCoordinate(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if(newValue.isEmpty()) {
            invalidMove.setValue(null);
        } else {
            try {
                int coordinate = Integer.parseInt(newValue);
                if (coordinate < 0 || coordinate > Board.WIDTH - 1) {
                    invalidMove.setValue("Coordinate must be in the range 0-9");
                } else {
                    invalidMove.setValue(null);
                }
            } catch (NumberFormatException exception) {
                invalidMove.setValue("Coordinate must have an integer value");
            }
        }
    }

    public void validateYCoordinate(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if(newValue.isEmpty()) {
            invalidMove.setValue(null);
        } else {
            try {
                int coordinate = Integer.parseInt(newValue);
                if (coordinate < 0 || coordinate > Board.HEIGHT - 1) {
                    invalidMove.setValue("Coordinate must be in the range 0-9");
                } else {
                    invalidMove.setValue(null);
                }
            } catch (NumberFormatException exception) {
                invalidMove.setValue("Coordinate must have an integer value");
            }
        }
    }

}
