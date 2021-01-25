package gr.ste.domain.mappers;

import gr.ste.domain.base.Mapper;
import gr.ste.domain.base.Result;
import gr.ste.domain.entities.*;
import javafx.geometry.Orientation;

import java.util.UUID;

public class ShipMapper implements Mapper<Ship, String[]> {
    @Override
    public Ship toDomain(String[] data) {
        assert(data.length == 4);

        final Position position = new Position(Integer.parseInt(data[1]), Integer.parseInt(data[2]));

        Orientation orientation = null;
        switch (data[3]) {
            case "1":
                orientation = Orientation.HORIZONTAL;
                break;
            case "2":
                orientation = Orientation.VERTICAL;
                break;
            default:
                break;
//                return Result.error("Orientation file encoding is not correct");
        }

        Ship ship = null;
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
                break;
//                return Result.error("Ship type file encoding is not correct");
        }
        return ship;
    }

    @Override
    public String[] toData(Ship ship) {
        return new String[0];
    }
}
