package gr.ste.domain.models;

public class Board {
    private final Tile[] tiles;
    private final int width;
    private final int height;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width * height];
    }
}
