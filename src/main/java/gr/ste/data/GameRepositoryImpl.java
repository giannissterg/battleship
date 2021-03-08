package gr.ste.data;

import gr.ste.domain.BattleshipGame;
import gr.ste.domain.base.ListMapper;
import gr.ste.domain.entities.*;
import gr.ste.domain.exceptions.*;
import gr.ste.domain.repositories.GameRepository;

import java.io.IOException;
import java.util.Collections;
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
    public BattleshipGame loadScenario(String filename, String enemyFilename) throws ShipException, IOException {
        final List<String[]> dataResponse = Collections.unmodifiableList(csvReader.read(filename));
        final List<Ship> ships = Collections.unmodifiableList(shipListMapper.toDomain(dataResponse));

        final List<String[]> enemyDataResponse = Collections.unmodifiableList(csvReader.read(enemyFilename));
        final List<Ship> enemyShips = Collections.unmodifiableList(shipListMapper.toDomain(enemyDataResponse));

        final Board playerBoard = new Board(UUID.randomUUID().toString(), Board.WIDTH, Board.HEIGHT, ships);
        final Board enemyBoard = new Board(UUID.randomUUID().toString(), Board.WIDTH, Board.HEIGHT, enemyShips);

        if(!playerBoard.oneOfAKind() && !enemyBoard.oneOfAKind()) {
            throw new InvalidCountException("Ships must be one of a kind");
        }

        if(!playerBoard.isInside(ships) || !enemyBoard.isInside(enemyShips)) {
            throw new OversizeException("Ship position is out of bounds");
        }

        if(playerBoard.checkForOverlappingTiles() || enemyBoard.checkForOverlappingTiles()) {
            throw new OverlapTilesException("Two ships are overlapping");
        }

        if(playerBoard.checkForAdjacentTiles() || enemyBoard.checkForOverlappingTiles()) {
            throw new AdjacentTilesException("Two ships are adjacent");
        }

        final Player player1 = new Player(0,"Giannis", playerBoard, PlayerType.human);
        final Player player2 = new NPCPlayer(1,"Kostas", enemyBoard);

        return new BattleshipGame(player1, player2);
    }

    @Override
    public Boolean saveScenario(String saveFileName) {
        return null;
    }
}
