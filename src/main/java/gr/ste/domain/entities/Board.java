package gr.ste.domain.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
    private final String id;
    private final int width;
    private final int height;
    public final List<Ship> ships;
    public final Set<Position> misses;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;

    public Board(String id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.ships = new ArrayList<>();
        this.misses = new HashSet<>();
    }
    public Board(String id, int width, int height, List<Ship> ships) {
        this.id = id;
        this.width = width;
        this.ships = ships;
        this.height = height;
        this.misses = new HashSet<>();
    }

    public boolean shipsBoundsCheck() {
        for(Ship ship : ships) {
            Position firstPosition = ship.getPositions().get(0);
            if(firstPosition.getX() < 0 || firstPosition.getY() < 0) {
                return false;
            }
            Position lastPosition = ship.getPositions().get(ship.getPositions().size() - 1);
            if(lastPosition.getX() >= WIDTH || lastPosition.getY() >= HEIGHT) {
                return false;
            }
        }
        return true;
    }

    public boolean checkForOverlappingTiles() {
        for(Ship ship : ships) {
            for(Ship otherShip : ships) {
                Position otherFirstPosition = otherShip.getPositions().get(0);
                Position otherLastPosition = otherShip.getPositions().get(ship.getPositions().size() - 1);
            }
        }
        return true;
    }
}
