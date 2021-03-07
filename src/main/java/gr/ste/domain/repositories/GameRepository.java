package gr.ste.domain.repositories;

import gr.ste.domain.BattleshipGame;
import gr.ste.domain.base.Result;
import gr.ste.domain.exceptions.ShipException;

import java.io.IOException;

public interface GameRepository {
    BattleshipGame loadScenario(String filename, String enemyFilename) throws ShipException, IOException;
    Boolean saveScenario(String saveFileName);
}