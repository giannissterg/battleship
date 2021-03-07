package gr.ste.presentation.widgets;

import gr.ste.domain.entities.Position;
import gr.ste.domain.entities.Ship;
import javafx.geometry.Orientation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.io.InputStream;

public class BattleshipGridPane extends GridPane {

    private final int rows;
    private final int columns;

    private final double width;
    private final double height;

    private final Tile[] tiles;

    public BattleshipGridPane(int columns, int rows, double width, double height) {
        this.columns = columns;
        this.rows = rows;
        this.width  = width;
        this.height = height;

        setGridLinesVisible(true);
        setMinSize(width, height);

        tiles = new Tile[rows * columns];
        for(int y = 0; y < columns; y++) {
            for(int x = 0; x < rows; x++) {
                double tileWidth = width / columns;
                double tileHeight = height / rows;
                Tile tile = new Tile(tileWidth, tileHeight);
                add(tile, y, x);
            }
        }
    }

    public void add(Ship ship) {
        String orientation = ship.getOrientation() == Orientation.HORIZONTAL ? "horiz" : "vert";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("images/ship_deploy_"+orientation+"_"+ship.getSpace()+".png")) {
            if (is != null) {
                Image shipImage = new Image(is);
                ImageView shipImageView = new ImageView(shipImage);
                if(ship.getOrientation() == Orientation.HORIZONTAL) {
                    shipImageView.setFitWidth(width / columns * ship.getSpace());
                    shipImageView.setFitHeight(height / rows);
                    add(shipImageView, ship.getPositions().get(0).getX(), ship.getPositions().get(0).getY(), ship.getSpace(), 1);
                } else {
                    shipImageView.setFitWidth(width / columns);
                    shipImageView.setFitHeight(height / rows * ship.getSpace());
                    add(shipImageView, ship.getPositions().get(0).getX(), ship.getPositions().get(0).getY(), 1, ship.getSpace());
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void add(Position targetPosition) {
        Rectangle r = new Rectangle(40.0,40.0);
        r.setFill(Color.GRAY);
        r.setOpacity(0.6);
        add(r, targetPosition.getX(), targetPosition.getY());
    }

    public void setBackgroundImage(String imageFilePath) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(imageFilePath)) {
            if (is != null) {
                BackgroundSize backgroundSize = new BackgroundSize(
                        1.0,
                        1.0,
                        true,
                        true,
                        true,
                        false
                );
                BackgroundImage gridBackgroundImage = new BackgroundImage(
                        new Image(is),
                        BackgroundRepeat.ROUND,
                        BackgroundRepeat.SPACE,
                        BackgroundPosition.CENTER,
                        backgroundSize
                );
                Background gridBackground = new Background(gridBackgroundImage);
                setBackground(gridBackground);
            }
        }
    }

    public Tile getTile(int x, int y) {
        return tiles[x + y * rows];
    }

//    /**
//     * Unhighlight all cells
//     */
////    public void unhighlight() {
////        for( int row=0; row < rows; row++) {
////            for( int col=0; col < columns; col++) {
////                tiles[row][col].unhighlight();
////            }
////        }
////    }
}

