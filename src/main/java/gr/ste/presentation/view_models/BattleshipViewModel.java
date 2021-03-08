package gr.ste.presentation.view_models;

import gr.ste.domain.BattleshipGame;
import gr.ste.domain.entities.Board;
import gr.ste.domain.entities.Player;
import gr.ste.domain.entities.Position;
import gr.ste.domain.entities.Ship;
import gr.ste.domain.exceptions.InvalidScenarioException;
import gr.ste.domain.exceptions.ShipException;
import gr.ste.domain.repositories.GameRepository;
import gr.ste.presentation.events.BattleshipGameEvent;
import gr.ste.presentation.events.MoveEnteredEvent;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleshipViewModel {
    private final GameRepository gameRepository;

    private final StringProperty xTargetCoordinate;
    private final StringProperty yTargetCoordinate;

    private final IntegerProperty numberOfPlayers;
    private final IntegerProperty currentPlayer;
    private final List<StringProperty> playerNameProperties;
    private final List<StringProperty> playerScoreProperties;

    private final List<ObservableList<Position>> moves;
    private final List<ObservableList<Ship>> playerShips;

    private final StringProperty invalidMove;
    private final ReadOnlyBooleanWrapper showInvalidMoveLabel;
    private final BooleanProperty hasLoadedGame;

    private BattleshipGame game;

    public BattleshipViewModel(GameRepository gameRepository) {
        this.gameRepository = gameRepository;

        xTargetCoordinate = new SimpleStringProperty();
        yTargetCoordinate = new SimpleStringProperty();

//        TODO: Initial state should be independent of repository data
        int initialPlayers = 2;
        numberOfPlayers = new SimpleIntegerProperty(initialPlayers);
        playerNameProperties = new ArrayList<>(initialPlayers);
        playerScoreProperties = new ArrayList<>(initialPlayers);
        currentPlayer = new SimpleIntegerProperty((new Random().nextInt(2)));
        playerShips = new ArrayList<>();
        moves = new ArrayList<>();

        for(int i = 0; i < initialPlayers; i++) {
            StringProperty nameProperty = new SimpleStringProperty("Player " + i);
            playerNameProperties.add(nameProperty);

            StringProperty scoreProperty = new SimpleStringProperty("Score: 0");
            playerScoreProperties.add(scoreProperty);

            playerShips.add(FXCollections.observableArrayList());
            moves.add(FXCollections.observableArrayList());
        }

        invalidMove = new SimpleStringProperty();
        showInvalidMoveLabel = new ReadOnlyBooleanWrapper();
        showInvalidMoveLabel.bind(invalidMove.isEmpty());

        hasLoadedGame = new SimpleBooleanProperty();

        xTargetCoordinate.addListener(this::validateXCoordinate);
        yTargetCoordinate.addListener(this::validateYCoordinate);
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

    public void loadGameState(String scenarioId) throws InvalidScenarioException {
        URL playerScenarioUrl = getClass().getClassLoader().getResource("medialab/player_" + scenarioId + ".txt");
        URL enemyScenarioUrl = getClass().getClassLoader().getResource("medialab/enemy_" + scenarioId + ".txt");
        if(playerScenarioUrl != null && enemyScenarioUrl != null) {
            try {
                this.game = gameRepository.loadScenario(playerScenarioUrl.getFile(), enemyScenarioUrl.getFile());

                numberOfPlayers.setValue(game.getNumberOfPlayers());
                currentPlayer.setValue((new Random()).nextInt(game.getNumberOfPlayers()));

                for (Player player : game.getPlayers()) {
                    playerNameProperties.get(player.getId()).setValue(player.getName());
                    playerScoreProperties.get(player.getId()).setValue("Score: " + player.getScore());
                    playerShips.get(player.getId()).setAll(player.getBoard().ships);
                    moves.get(player.getId()).setAll(player.getBoard().getMissedShots());
                }
            } catch (ShipException | IOException e) {
                e.printStackTrace();
                throw new InvalidScenarioException(e.getMessage());
            }
        } else {
            throw new InvalidScenarioException("An invalid scenario id has been provided");
        }
    }

    public void playMove(BattleshipGameEvent event) {
        if (event instanceof MoveEnteredEvent) {
            MoveEnteredEvent moveEnteredEvent = (MoveEnteredEvent) event;

            boolean couldPlayMove = game.play(moveEnteredEvent.getTargetPlayerId(), moveEnteredEvent.getTargetPosition());
            if(couldPlayMove) {
                moves.get(moveEnteredEvent.getTargetPlayerId()).add(moveEnteredEvent.getTargetPosition());
                playerScoreProperties.get(game.getCurrentPlayer().getId()).setValue("Score: " + game.getCurrentPlayer().getScore());

                MoveEnteredEvent nextMoveTarget = game.playRound();
                playMove(nextMoveTarget);
                invalidMove.setValue(null);
            } else {
                invalidMove.setValue("You have already tried that location");
            }
        }
    }

    public StringProperty xTargetCoordinateProperty() {
        return xTargetCoordinate;
    }

    public StringProperty yTargetCoordinateProperty() {
        return yTargetCoordinate;
    }

    public StringProperty getPlayerNameProperty(int playerId) {
        return playerNameProperties.get(playerId);
    }

    public StringProperty getPlayerScoreProperty(int playerId) {
        return playerScoreProperties.get(playerId);
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers.get();
    }

    public IntegerProperty getCurrentPlayerProperty() {
        return currentPlayer;
    }

    public StringProperty getInvalidMoveProperty() {
        return invalidMove;
    }

    public ReadOnlyBooleanWrapper getShowInvalidMoveLabel() { return showInvalidMoveLabel; }

    public ObservableList<Ship> getPlayerShips(int playerId) {
        return playerShips.get(playerId);
    }

    public ObservableList<Position> getMoves(int playerId) {
        return moves.get(playerId);
    }

    public BooleanProperty hasLoadedGameProperty() {
        return hasLoadedGame;
    }
}
