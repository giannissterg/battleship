package gr.ste.domain.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
    public final int id;
    private final int width;
    private final int height;
    public final List<Ship> ships;
    public final Set<Position> hits;
    public final Set<Position> misses;

    public Board(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.ships = new ArrayList<>();
        this.hits = new HashSet<>();
        this.misses = new HashSet<>();
    }
    public Board(int id, int width, int height, List<Ship> ships) {
        this.id = id;
        this.width = width;
        this.ships = ships;
        this.height = height;
        this.hits = new HashSet<>();
        this.misses = new HashSet<>();
    }

}
