package gr.ste.presentation.view_models;

import gr.ste.domain.BattleshipGame;
import gr.ste.domain.entities.NPCPlayer;
import gr.ste.domain.entities.Position;
import gr.ste.domain.exceptions.InvalidScenarioException;
import gr.ste.domain.exceptions.ShipException;
import gr.ste.domain.repositories.GameRepository;
import gr.ste.presentation.events.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;

public class BattleshipViewModel {
    private final GameRepository gameRepository;

    private BattleshipGame game;

    public final ObservableList<GameState> gameStateList;

    public final GameState initialGameState;

    public BattleshipViewModel(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        this.gameStateList = FXCollections.observableArrayList();

        int initialPlayers = 2;
        initialGameState = new GameState(initialPlayers);
        gameStateList.add(initialGameState);
    }

    public void mapEventToState(BattleshipGameEvent event) throws InvalidScenarioException {
//        GameState newGameState = gameStateList.get(gameStateList.size() - 1);
        if(event instanceof LoadGameEvent) {
            LoadGameEvent loadGameEvent = (LoadGameEvent)event;
            try {
                BattleshipGame loadedGame = loadGame(loadGameEvent.getScenarioId());
                this.game = loadedGame;
                this.initialGameState.update(loadedGame);
//                newGameState = new GameState(loadedGame);
//                this.gameStateList.add(newGameState);
            } catch (InvalidScenarioException e) {
                throw e;
            }
        } else if(event instanceof MoveEnteredEvent) {
            MoveEnteredEvent moveEnteredEvent = (MoveEnteredEvent) event;
            enterMove(moveEnteredEvent.getTargetPlayerId(), moveEnteredEvent.getTargetPosition(), initialGameState);
//            this.gameStateList.add(newGameState);
        } else if(event instanceof StartGameEvent) {
            StartGameEvent startGameEvent = (StartGameEvent) event;
            if(game != null) {
                MoveEnteredEvent possibleAiMove = game.start();
                this.initialGameState.hasStartedGame.setValue(true);
    //            newGameState.hasStartedGame.setValue(true);
                mapEventToState(possibleAiMove);
            }
        } else if(event instanceof ShowEnemyShipsEvent) {

        }
    }

    public BattleshipGame loadGame(String scenarioId) throws InvalidScenarioException {
        URL playerScenarioUrl = getClass().getClassLoader().getResource("medialab/player_" + scenarioId + ".txt");
        URL enemyScenarioUrl = getClass().getClassLoader().getResource("medialab/enemy_" + scenarioId + ".txt");
        if(playerScenarioUrl != null && enemyScenarioUrl != null) {
            try {
                return gameRepository.loadScenario(playerScenarioUrl.getFile(), enemyScenarioUrl.getFile());
            } catch (ShipException | IOException e) {
                throw new InvalidScenarioException(e.getMessage());
            }
        } else {
            throw new InvalidScenarioException("An invalid scenario id has been provided");
        }
    }

    public void enterMove(int enemyId, Position target, GameState gameState) {
            boolean couldPlayMove = game.play(enemyId, target);
            if(couldPlayMove) {
                // Update round
                // Update moves
                // Update score
                // Update percentage
                // Check for errors
                // Next  player
                gameState.update(game.getCurrentPlayer(), enemyId);
                if(!game.hasEnded()) {
                    game.nextPlayer();
                    if (game.getCurrentPlayer().isNPC()) {
                        NPCPlayer ai = (NPCPlayer) game.getCurrentPlayer();
                        MoveEnteredEvent event = ai.chooseMove(game.getPlayers());
                        try {
                            mapEventToState(event);
                        } catch (InvalidScenarioException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    gameState.showEndDialog.setValue(true);
                }
            } else {
                gameState.invalidMove.setValue("You have already tried that location");
            }
        }

}
