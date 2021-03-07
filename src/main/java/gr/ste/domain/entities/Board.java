package gr.ste.domain.entities;

import javafx.geometry.Orientation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
    private final String id;
    private final int width;
    private final int height;
    public final List<Ship> ships;
    private final Set<Position> misses;
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

    public boolean isInside(List<Ship> ships) {
        for(Ship ship : ships) {
            for(ShipPosition shipPosition : ship.getPositions()) {
                if(!isInside(shipPosition)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkForOverlappingTiles() throws IllegalStateException {
        int[] frequencyTable = new int[WIDTH * HEIGHT];
        for(Ship ship : ships) {
            for(Position pos : ship.getPositions()) {
                frequencyTable[pos.getX() + pos.getY() * WIDTH]++;
            }
        }

        for(int y = 0; y < HEIGHT; y++) {
            for(int x = 0; x < WIDTH; x++) {
                if(frequencyTable[x + y * WIDTH] > 1){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkForAdjacentTiles() {
        int[] frequencyTable = new int[WIDTH * HEIGHT];
        for(Ship ship : ships) {
            for(Position pos : ship.getPositions()) {
                frequencyTable[pos.getX() + pos.getY() * WIDTH]++;
            }
        }

        int[] adjacentFrequencyTable = new int[WIDTH * HEIGHT];
        for(Ship ship : ships) {
            for(Position pos : getAdjacentTiles(ship)) {
                //System.out.println("(" + pos.getX() + "," + pos.getY() + ")");
                adjacentFrequencyTable[pos.getX() + pos.getY() * WIDTH]++;
            }
        }

        for(int y = 0; y < HEIGHT; y++) {
            for(int x = 0; x < WIDTH; x++) {
                if(frequencyTable[x + y * WIDTH] == 1 && adjacentFrequencyTable[x + y * WIDTH] == 1){
                    return true;
                }
            }
        }
        return false;
    }

    private List<Position> getAdjacentTiles(Ship ship) {
        List<Position> adjacentTiles = new ArrayList<>(2 * ship.getSpace() + 2);

        ShipPosition firstPosition = ship.getPositions().get(0);
        ShipPosition lastPosition = ship.getPositions().get(ship.getSpace() - 1);

        Position[] neighboringTiles = {
            new Position(-1, 0),
            new Position(1, 0),
            new Position(0, -1),
            new Position(0, 1)
        };

        int i = ship.getOrientation() == Orientation.HORIZONTAL ? 0 : 2;
        Position rightPosition = firstPosition.add(neighboringTiles[i % neighboringTiles.length]);
        Position leftPosition = lastPosition.add(neighboringTiles[(i+1) % neighboringTiles.length]);

        if(isInside(rightPosition)) {
            adjacentTiles.add(rightPosition);
        }

        if(isInside(leftPosition)) {
            adjacentTiles.add(leftPosition);
        }

        for(ShipPosition shipPosition : ship.getPositions()) {
            Position northTile = shipPosition.add(neighboringTiles[(i+2) % neighboringTiles.length]);
            Position southTile = shipPosition.add(neighboringTiles[(i+3) % neighboringTiles.length]);
            if(isInside(northTile)) {
                adjacentTiles.add(northTile);
            }

            if(isInside(southTile)) {
                adjacentTiles.add(southTile);
            }
        }
        return adjacentTiles;
    }

    private boolean isInside(Position position) {
        return position.getX() >= 0 && position.getX() < WIDTH && position.getY() >= 0 && position.getY() < HEIGHT;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void addMissedShot(Position position) {
        misses.add(position);
    }

    public Set<Position> getMissedShots() {
        return misses;
    }
}
