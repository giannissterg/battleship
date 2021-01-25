package gr.ste.data;

import gr.ste.domain.BattleshipGame;
import gr.ste.domain.base.Result;
import gr.ste.domain.entities.*;
import gr.ste.domain.repositories.GameRepository;
import javafx.geometry.Orientation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameRepositoryImpl implements GameRepository {
    private final CsvReader csvReader;
//    private final CsvListMapper<String[], Ship> csvMapper;

    public GameRepositoryImpl(CsvReader csvReader) {
        this.csvReader = csvReader;
    }

    @Override
    public Result<BattleshipGame> loadScenario(String filename) {
        final List<Ship> ships = new ArrayList<>();

        List<String[]> dataResponse;
        try {
            dataResponse = csvReader.read(filename);
            for(String[] data : dataResponse) {
                assert(data.length == 4);
                final Position position = new Position(Integer.parseInt(data[1]), Integer.parseInt(data[2]));

                final Orientation orientation;
                switch (data[3]) {
                    case "1":
                        orientation = Orientation.HORIZONTAL;
                        break;
                    case "2":
                        orientation = Orientation.VERTICAL;
                        break;
                    default:
                        return Result.error("Orientation file encoding is not correct");
                }

                final Ship ship;
                switch (data[0]) {
                    case "1":
                        ship = new Carrier( UUID.randomUUID().toString(), position, orientation);
                        break;
                    case "2":
                        ship = new Battleship(UUID.randomUUID().toString(), position, orientation);
                        break;
                    case "3":
                        ship = new Cruiser(UUID.randomUUID().toString(), position, orientation);
                        break;
                    case "4":
                        ship = new Submarine(UUID.randomUUID().toString(), position, orientation);
                        break;
                    case "5":
                        ship = new Destroyer(UUID.randomUUID().toString(), position, orientation);
                        break;
                    default:
                        return Result.error("Ship type file encoding is not correct");
                }
                ships.add(ship);
            }
            return Result.ok(new BattleshipGame(new Board(0, 10, 10, ships)));
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
