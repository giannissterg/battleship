package gr.ste.presentation;

import gr.ste.data.CsvReader;
import gr.ste.data.GameRepositoryImpl;
import gr.ste.domain.BattleshipGame;
import gr.ste.domain.base.Result;
import gr.ste.domain.entities.Ship;
import gr.ste.domain.repositories.GameRepository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PlayerController implements Initializable {

    @FXML
    private GridPane myGridPane;

    @FXML
    private GridPane enemyGridPane;

    private final GameRepository gameRepository;

    public PlayerController() {
        this.gameRepository = new GameRepositoryImpl(new CsvReader());
    }
    public PlayerController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Result<BattleshipGame> domainResponse = gameRepository.loadScenario(Objects.requireNonNull(getClass().getClassLoader().getResource("player_default.txt")).getFile());
        if(!domainResponse.hasError()) {
            BattleshipGame battleshipGame = domainResponse.getValue();
            myGridPane.setBorder(
                    new Border(
                    new BorderStroke(
                            Color.BLACK,
                            BorderStrokeStyle.SOLID,
                            null,
                            BorderStroke.DEFAULT_WIDTHS
                    )
                    )
            );
            battleshipGame.board.ships.forEach(this::placeShip);
        }
    }

    private void placeShip(Ship ship) {
        for(int i = 0; i < ship.getSpace(); i++) {
            Rectangle r = new Rectangle(75, 75);
            r.setFill(Color.RED);
            if(ship.getOrientation() == Orientation.HORIZONTAL) {
                myGridPane.add(r, ship.getPosition().getY() + i, ship.getPosition().getX());
            } else {
                myGridPane.add(r, ship.getPosition().getY(), ship.getPosition().getX() + i);
            }
        }
    }

}
