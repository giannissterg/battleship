package gr.ste.presentation.widgets;

import gr.ste.domain.entities.Board;
import gr.ste.domain.entities.Ship;
import gr.ste.presentation.view_models.PlayerState;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.Map;

public class BoardWidget extends VBox {
    private final Label nameLabel;
    private final Label scoreLabel;
    private final Label percentageLabel;
    private final Label activeShipsLabel;
    public final BattleshipGridPane gridPane;
    private final HBox xCoordinates;
    private final VBox yCoordinates;
    public final Circle currentPlayer;

    public BoardWidget() {
        this.nameLabel = new Label();
        this.scoreLabel = new Label();
        this.percentageLabel = new Label();
        this.activeShipsLabel = new Label();
        this.currentPlayer = new Circle(5.0, Color.BLACK);

        nameLabel.setFont(Font.font("Blackadder ITC", FontWeight.BOLD, 28));
        scoreLabel.setFont(Font.font("Blackadder ITC", FontWeight.NORMAL, 26));
        percentageLabel.setFont(Font.font("Blackadder ITC", FontWeight.NORMAL, 24));
        activeShipsLabel.setFont(Font.font("Blackadder ITC", FontWeight.NORMAL, 28));

        this.gridPane = new BattleshipGridPane(Board.WIDTH, Board.HEIGHT, Board.WIDTH * 40, Board.HEIGHT * 40);
        gridPane.setOpacity(0.8);
        gridPane.setBackgroundImage("images/sea.png");

        Tile emptyTile = new Tile(40, 40);


        xCoordinates = new HBox(emptyTile);
        for(int x = 0; x < Board.WIDTH; x++) {
            Tile tile = new Tile(40, 40);
            Label xCoordinate = new Label(Integer.toString(x));
            xCoordinate.setFont(Font.font("Blackadder ITC", FontWeight.BOLD, FontPosture.REGULAR, 24));
            tile.getChildren().add(xCoordinate);
            xCoordinates.getChildren().add(tile);
        }

        yCoordinates = new VBox();
        for(int y = 0; y < Board.HEIGHT; y++) {
            Tile tile = new Tile(40, 40);
            Label yCoordinate = new Label(Integer.toString(y));
            yCoordinate.setFont(Font.font("Blackadder ITC", FontWeight.BOLD, FontPosture.REGULAR, 24));
            tile.getChildren().add(yCoordinate);
            yCoordinates.getChildren().add(tile);
        }
        HBox gridWithCoords = new HBox(yCoordinates, gridPane);

        setPadding(new Insets(32, 0,0,0));
        getChildren().addAll(nameLabel, scoreLabel, percentageLabel, xCoordinates, gridWithCoords);
    }

    public BoardWidget(PlayerState playerState) {
        this.nameLabel = new Label();
        this.scoreLabel = new Label();
        this.percentageLabel = new Label();
        this.activeShipsLabel = new Label();
        this.currentPlayer = new Circle(5.0, Color.BLACK);
        this.currentPlayer.setVisible(false);

        nameLabel.setFont(Font.font("Blackadder ITC", FontWeight.BOLD, 28));
        scoreLabel.setFont(Font.font("Blackadder ITC", FontWeight.NORMAL, 26));
        percentageLabel.setFont(Font.font("Blackadder ITC", FontWeight.NORMAL, 24));
        activeShipsLabel.setFont(Font.font("Blackadder ITC", FontWeight.NORMAL, 24));

        nameLabel.textProperty().bind(playerState.name);
        scoreLabel.textProperty().bind(Bindings.createStringBinding(() -> "Score: " + playerState.score.getValue(), playerState.score));
        for (Map.Entry<Integer, DoubleProperty> entry : playerState.percentages.entrySet()) {
            DoubleProperty percentage = entry.getValue();
            percentageLabel.textProperty().bind(Bindings.createStringBinding(() -> "Percentage: " + String.format("%.2f", percentage.getValue()), percentage));
        }
        activeShipsLabel.textProperty().bind(Bindings.createStringBinding(() -> "Active Ships: " + playerState.activeShips.getValue(), playerState.activeShips));

        this.gridPane = new BattleshipGridPane(Board.WIDTH, Board.HEIGHT, Board.WIDTH * 40, Board.HEIGHT * 40);
        gridPane.setOpacity(0.8);
        gridPane.setBackgroundImage("images/sea.png");

        Tile emptyTile = new Tile(40, 40);
        xCoordinates = new HBox(emptyTile);
        for(int x = 0; x < Board.WIDTH; x++) {
            Tile tile = new Tile(40, 40);
            Label xCoordinate = new Label(Integer.toString(x));
            xCoordinate.setFont(Font.font("Blackadder ITC", FontWeight.BOLD, FontPosture.REGULAR, 24));
            tile.getChildren().add(xCoordinate);
            xCoordinates.getChildren().add(tile);
        }

        yCoordinates = new VBox();
        for(int y = 0; y < Board.HEIGHT; y++) {
            Tile tile = new Tile(40, 40);
            Label yCoordinate = new Label(Integer.toString(y));
            yCoordinate.setFont(Font.font("Blackadder ITC", FontWeight.BOLD, FontPosture.REGULAR, 24));
            tile.getChildren().add(yCoordinate);
            yCoordinates.getChildren().add(tile);
        }
        HBox gridWithCoords = new HBox(yCoordinates, gridPane);

        HBox nameWithCircle;
        if(playerState.id.getValue() % 2 == 0) {
            setAlignment(Pos.CENTER_LEFT);
            nameWithCircle = new HBox(nameLabel, currentPlayer);
            nameWithCircle.setAlignment(Pos.BASELINE_LEFT);
        } else {
            setAlignment(Pos.CENTER_RIGHT);
            nameWithCircle = new HBox(currentPlayer, nameLabel);
            nameWithCircle.setAlignment(Pos.BASELINE_RIGHT);
        }
        nameWithCircle.setSpacing(15.0);

        if(playerState.id.getValue() == 0) {
            playerState.boardState.ships.addListener((ListChangeListener<Ship>) c -> {
                gridPane.clear();
                c.getList().forEach(gridPane::add);
            });
        } else {
            playerState.boardState.ships.addListener((ListChangeListener<Ship>) c -> gridPane.clear());
        }

        setPadding(new Insets(32, 0,0,0));


        getChildren().addAll(nameWithCircle, scoreLabel, percentageLabel, activeShipsLabel, xCoordinates, gridWithCoords);
    }

    public void update(PlayerState playerState) {
        nameLabel.textProperty().bind(playerState.name);
        scoreLabel.textProperty().bind(Bindings.createStringBinding(() -> "Score: " + playerState.score.getValue(), playerState.score));
        for (Map.Entry<Integer, DoubleProperty> entry : playerState.percentages.entrySet()) {
            DoubleProperty percentage = entry.getValue();
            percentageLabel.textProperty().bind(Bindings.createStringBinding(() -> "Percentage: " + String.format("%.2f", percentage.getValue()), percentage));
        }
    }
}
