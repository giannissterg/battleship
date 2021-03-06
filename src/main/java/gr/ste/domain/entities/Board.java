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

    public boolean checkForOverlappingTiles() throws IllegalStateException {
        int[] frequencyTable = new int[WIDTH * HEIGHT];
        for(Ship ship : ships) {
            for(Position pos : ship.getPositions())
                frequencyTable[pos.getX() + pos.getY() * WIDTH]++;
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
        for(Ship ship : ships) {
        }
        return true;
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
