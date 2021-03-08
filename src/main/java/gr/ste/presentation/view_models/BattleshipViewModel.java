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

    private BattleshipGame game;

    public GameState gameState;

    public BattleshipViewModel(GameRepository gameRepository) {
        this.gameRepository = gameRepository;

        int initialPlayers = 2;
        this.gameState = new GameState(initialPlayers);
    }

//    public static NumberBinding divideSafe(ObservableValue<Number> dividend, ObservableValue<Number> divisor, ObservableValue<Number> defaultValue) {
//        return Bindings.createDoubleBinding(() -> {
//
//            if (divisor.getValue().doubleValue() == 0) {
//                return defaultValue.getValue().doubleValue();
//            } else {
//                return dividend.getValue().doubleValue() / divisor.getValue().doubleValue();
//            }
//
//        }, dividend, divisor);
//    }
//
    public void mapEventToState(BattleshipGameEvent event) throws InvalidScenarioException {
        if(event instanceof LoadGameEvent) {
            LoadGameEvent loadGameEvent = (LoadGameEvent)event;
            try {
                BattleshipGame loadedGame = loadGame(loadGameEvent.getScenarioId());
                this.game = loadedGame;
                this.gameState = new GameState(loadedGame);
            } catch (InvalidScenarioException e) {
                e.printStackTrace();
                throw e;
            }
        } else if(event instanceof MoveEnteredEvent) {
            MoveEnteredEvent moveEnteredEvent = (MoveEnteredEvent) event;
            enterMove(moveEnteredEvent.getTargetPlayerId(), moveEnteredEvent.getTargetPosition());
        } else if(event instanceof StartGameEvent) {
            StartGameEvent startGameEvent = (StartGameEvent) event;
            MoveEnteredEvent possibleAiMove = game.start();
            mapEventToState(possibleAiMove);
        }
    }

    public BattleshipGame loadGame(String scenarioId) throws InvalidScenarioException {
        URL playerScenarioUrl = getClass().getClassLoader().getResource("medialab/player_" + scenarioId + ".txt");
        URL enemyScenarioUrl = getClass().getClassLoader().getResource("medialab/enemy_" + scenarioId + ".txt");
        try {
            return gameRepository.loadScenario(playerScenarioUrl.getFile(), enemyScenarioUrl.getFile());
        } catch (ShipException | IOException e) {
            e.printStackTrace();
            throw new InvalidScenarioException(e.getMessage());
        }
    }

    public void enterMove(int enemyId, Position target) {
            boolean couldPlayMove = game.play(enemyId, target);
            if(couldPlayMove) {
                // Update round
                // Update moves
                // Update score
                // Update percentage
                // Check for errors
                // Next  player
                gameState.update(game.getCurrentPlayer(), enemyId);
                game.nextPlayer();
                if(game.getCurrentPlayer().isNPC()) {
                    NPCPlayer ai = (NPCPlayer) game.getCurrentPlayer();
                    MoveEnteredEvent event = ai.chooseMove(game.getPlayers());
                    try {
                        mapEventToState(event);
                    } catch (InvalidScenarioException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                gameState.invalidMove.setValue("You have already tried that location");
            }
        }

}
