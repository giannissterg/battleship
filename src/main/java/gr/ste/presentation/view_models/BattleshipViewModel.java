package gr.ste.presentation.view_models;

import gr.ste.domain.BattleshipGame;
import gr.ste.domain.entities.NPCPlayer;
import gr.ste.domain.entities.Position;
import gr.ste.domain.exceptions.InvalidScenarioException;
import gr.ste.domain.exceptions.ShipException;
import gr.ste.domain.repositories.GameRepository;
import gr.ste.presentation.events.BattleshipGameEvent;
import gr.ste.presentation.events.LoadGameEvent;
import gr.ste.presentation.events.MoveEnteredEvent;
import gr.ste.presentation.events.StartGameEvent;

import java.io.IOException;
import java.net.URL;

public class BattleshipViewModel {
    private final GameRepository gameRepository;
    public BattleshipGame game;
    public final GameState initialGameState;

    public BattleshipViewModel(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        int initialPlayers = 2;
        initialGameState = new GameState(initialPlayers);
    }

    public void mapEventToState(BattleshipGameEvent event) throws InvalidScenarioException {
        if(event instanceof LoadGameEvent) {
            LoadGameEvent loadGameEvent = (LoadGameEvent)event;
            BattleshipGame loadedGame = loadGame(loadGameEvent.getScenarioId());
            this.game = loadedGame;
            this.initialGameState.update(loadedGame);
            this.initialGameState.hasStartedGame.setValue(false);
        } else if(event instanceof MoveEnteredEvent) {
            MoveEnteredEvent moveEnteredEvent = (MoveEnteredEvent) event;
            enterMove(moveEnteredEvent.getTargetPlayerId(), moveEnteredEvent.getTargetPosition());
        } else if(event instanceof StartGameEvent) {
            if(game != null) {
                MoveEnteredEvent possibleAiMove = game.start();
                this.initialGameState.hasStartedGame.setValue(true);
                mapEventToState(possibleAiMove);
            }
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

    public void enterMove(int enemyId, Position target) throws InvalidScenarioException {
            boolean couldPlayMove = game.play(enemyId, target);
            if(couldPlayMove) {
                initialGameState.update(game.getCurrentPlayer(), enemyId);
                if(!game.hasEnded()) {
                    game.nextPlayer();
                    if (game.getCurrentPlayer().isNPC()) {
                        NPCPlayer ai = (NPCPlayer) game.getCurrentPlayer();
                        MoveEnteredEvent event = ai.chooseMove(game.getPlayers());
                        mapEventToState(event);
                    }
                } else {
                    initialGameState.showEndDialog.setValue(true);
                }
            } else {
                initialGameState.invalidMove.setValue("You have already tried that location");
            }
        }

}
