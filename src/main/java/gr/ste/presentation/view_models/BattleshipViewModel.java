package gr.ste.presentation.view_models;

import gr.ste.domain.BattleshipGame;
import gr.ste.domain.entities.NPCPlayer;
import gr.ste.domain.exceptions.InvalidMoveException;
import gr.ste.domain.exceptions.InvalidScenarioException;
import gr.ste.domain.repositories.GameRepository;
import gr.ste.presentation.events.BattleshipGameEvent;
import gr.ste.presentation.events.LoadGameEvent;
import gr.ste.presentation.events.MoveEnteredEvent;
import gr.ste.presentation.events.StartGameEvent;

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
            BattleshipGame loadedGame = gameRepository.loadScenarioFromId(loadGameEvent.getScenarioId());
            this.game = loadedGame;
            this.initialGameState.update(loadedGame);
            this.initialGameState.hasStartedGame.setValue(false);
        } else if(event instanceof MoveEnteredEvent) {
            MoveEnteredEvent moveEnteredEvent = (MoveEnteredEvent) event;
            try {
                this.game = gameRepository.updateGame(this.game, moveEnteredEvent.getTargetPlayerId(), moveEnteredEvent.getTargetPosition());
                this.initialGameState.update(this.game.getCurrentPlayer(), moveEnteredEvent.getTargetPlayerId());
                if(this.game.hasEnded()) {
                    initialGameState.showEndDialog.setValue(true);
                } else {
                    this.game.nextPlayer();
                    if (this.game.getCurrentPlayer().isNPC()) {
                        NPCPlayer ai = (NPCPlayer) this.game.getCurrentPlayer();
                        MoveEnteredEvent aiEvent = ai.chooseMove(game.getPlayers());
                        mapEventToState(aiEvent);
                    }
                }
            } catch (InvalidMoveException e) {
                this.initialGameState.invalidMove.setValue("You have already tried that location");
            }
        } else if(event instanceof StartGameEvent) {
            if(game != null) {
                MoveEnteredEvent possibleAiMove = game.start();
                this.initialGameState.hasStartedGame.setValue(true);
                mapEventToState(possibleAiMove);
            }
        }
    }
}
