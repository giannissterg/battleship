package gr.ste.data;

import gr.ste.domain.BattleshipGame;
import gr.ste.domain.base.ListMapper;
import gr.ste.domain.base.Result;
import gr.ste.domain.entities.*;
import gr.ste.domain.repositories.GameRepository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class GameRepositoryImpl implements GameRepository {
    private final CsvReader csvReader;
    private final ListMapper<Ship, String[]> shipListMapper;

    public GameRepositoryImpl(CsvReader csvReader, ListMapper<Ship, String[]> shipListMapper) {
        this.csvReader = csvReader;
        this.shipListMapper = shipListMapper;
    }

    @Override
    public Result<BattleshipGame> loadScenario(String filename, String enemyFilename) {
        try {
            final List<String[]> dataResponse = csvReader.read(filename);
            final List<Ship> ships = shipListMapper.toDomain(dataResponse);

            final List<String[]> enemyDataResponse = csvReader.read(enemyFilename);
            final List<Ship> enemyShips = shipListMapper.toDomain(enemyDataResponse);

            final Board playerBoard = new Board(UUID.randomUUID().toString(), Board.WIDTH, Board.HEIGHT, ships);
            final Board enemyBoard = new Board(UUID.randomUUID().toString(), Board.WIDTH, Board.HEIGHT, enemyShips);

//            if(playerBoard.shipsBoundsCheck() || enemyBoard.shipBoundsCheck()) {
//                return Result.error("Ship position is out of bounds");
//            }

//            if(playerBoard.checkForOverlappingTiles()) {
//                return Result.error("Two ships are overlapping");
//            }
//            if(playerBoard.checkForAdjacentTiles()) {
//                return Result.error("Two ships are adjacent");
//            }

            final Player player1 = new Player("Giannis", playerBoard, PlayerType.human);
            final Player player2 = new Player("Kostas", enemyBoard, PlayerType.npc);

            return Result.ok(new BattleshipGame(player1, player2));
        } catch (IOException exception) {
            exception.printStackTrace();
            return Result.error("File could not be read");
        }
    }

    @Override
    public Result<Boolean> saveScenario(String saveFileName) {
        return null;
    }
}
