package gr.ste.domain.repositories;

import gr.ste.domain.models.Player;
import gr.ste.domain.models.Result;

interface ShipRepository {
    Result<Player> loadPlayer(String filename);
    Result<Player> loadEnemy(String filename);
}