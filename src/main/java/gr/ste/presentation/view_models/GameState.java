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
    public final BooleanProperty hasLoadedGame;

    public final StringProperty xTargetCoordinate;
    public final StringProperty yTargetCoordinate;

    GameState(int initialPlayers) {
        this.playerStates = new ArrayList<>();
        for(int i = 0; i < initialPlayers; i++) {
            PlayerState playerState = new PlayerState(i);
            playerStates.add(playerState);
        }

        this.rounds = new SimpleIntegerProperty(0);
        this.currentPlayer = new SimpleIntegerProperty();
        this.numberOfPlayers = new SimpleIntegerProperty(initialPlayers);

        this.invalidMove = new SimpleStringProperty();
        this.showInvalidMoveLabel = new ReadOnlyBooleanWrapper(false);
        this.showInvalidMoveLabel.bind(invalidMove.isEmpty());

        this.hasLoadedGame = new SimpleBooleanProperty(false);
        this.xTargetCoordinate = new SimpleStringProperty();
        this.yTargetCoordinate = new SimpleStringProperty();
    }

    GameState(BattleshipGame game) {
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
        this.showInvalidMoveLabel.bind(invalidMove.isEmpty());

        this.hasLoadedGame = new SimpleBooleanProperty(false);
        this.xTargetCoordinate = new SimpleStringProperty();
        this.yTargetCoordinate = new SimpleStringProperty();

        this.xTargetCoordinate.addListener(this::validateXCoordinate);
        this.yTargetCoordinate.addListener(this::validateYCoordinate);
    }

    public void update(Player current, int enemyId) {
        invalidMove.setValue(null);

        currentPlayer.setValue(current.getId());
        PlayerState currentPlayerState = playerStates.get(currentPlayer.getValue());

        Move lastMove = current.getPastMoves(enemyId).lastElement();
        currentPlayerState.moves.get(enemyId).add(lastMove);

        currentPlayerState.score.setValue(current.getScore());

        Double updatedPercentage = current.computePercentages().get(enemyId);
        currentPlayerState.percentages.get(enemyId).setValue(updatedPercentage);
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
