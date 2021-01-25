package gr.ste.domain.repositories;

import gr.ste.domain.BattleshipGame;
import gr.ste.domain.base.Result;

public interface GameRepository {
    Result<BattleshipGame> loadScenario(String filename);
    Result<Boolean> saveScenario(String saveFileName);
}