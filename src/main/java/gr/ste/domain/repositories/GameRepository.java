package gr.ste.domain.repositories;

import gr.ste.domain.BattleshipGame;
import gr.ste.domain.base.Result;
import gr.ste.domain.entities.Position;
import gr.ste.domain.exceptions.InvalidMoveException;
import gr.ste.domain.exceptions.InvalidScenarioException;
import gr.ste.domain.exceptions.ShipException;

import java.io.IOException;

public interface GameRepository {
    BattleshipGame loadScenario(String filename, String enemyFilename) throws ShipException, IOException;
    BattleshipGame loadScenarioFromId(String scenarioId) throws InvalidScenarioException;
    BattleshipGame updateGame(BattleshipGame game, int enemyId, Position target) throws InvalidMoveException;
    Boolean saveScenario(String saveFileName);
}