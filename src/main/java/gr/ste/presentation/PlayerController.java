package gr.ste.presentation;

import gr.ste.data.CsvReader;
import gr.ste.data.GameRepositoryImpl;
import gr.ste.domain.BattleshipGame;
import gr.ste.domain.base.ListMapper;
import gr.ste.domain.base.Result;
import gr.ste.domain.entities.Player;
import gr.ste.domain.entities.Ship;
import gr.ste.domain.entities.ShipPosition;
import gr.ste.domain.mappers.ShipMapper;
import gr.ste.domain.repositories.GameRepository;
import gr.ste.presentation.bloc.BattleshipGameEvent;
import gr.ste.presentation.bloc.MoveEnteredEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PlayerController implements Initializable {

    @FXML
    private HBox horizontalBox;

    @FXML
    private GridPane myGridPane;

    @FXML
    private GridPane enemyGridPane;

    private final GameRepository gameRepository;
    private BattleshipGame gameState;

    public PlayerController() {
        this.gameRepository = new GameRepositoryImpl(new CsvReader(), new ListMapper<>(new ShipMapper()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Result<BattleshipGame> domainResponse = gameRepository.loadScenario(
                Objects.requireNonNull(getClass().getClassLoader().getResource("player_default.txt")).getFile(),
                Objects.requireNonNull(getClass().getClassLoader().getResource("enemy_default.txt")).getFile()
        );
        if(!domainResponse.hasError()) {
            gameState = domainResponse.getValue();
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
            enemyGridPane.setBorder(
                    new Border(
                            new BorderStroke(
                                    Color.BLACK,
                                    BorderStrokeStyle.SOLID,
                                    null,
                                    BorderStroke.DEFAULT_WIDTHS
                            )
                    )
            );

            for(Player player : gameState.players) {
                player.getBoard().ships.forEach(ship -> { placeShip(ship, player == gameState.findPlayerById(0) ? myGridPane : enemyGridPane); });
            }
        } else {
            showDialog();
        }
        gameState.playRound();
    }

    private void updateBoard(BattleshipGameEvent event) {
        if (event instanceof MoveEnteredEvent) {
            MoveEnteredEvent moveEnteredEvent = (MoveEnteredEvent) event;

            for(Ship enemyShip : gameState.findPlayerById(moveEnteredEvent.getTargetPlayerId()).getBoard().ships) {
                for(ShipPosition shipPart : enemyShip.getPositions()) {
                    if(moveEnteredEvent.getTargetPosition() == shipPart) {
                    }
                }
            }
            gameState.nextPlayer();
        }
    }

    private void placeShip(Ship ship, GridPane pane) {
        for(ShipPosition shipPosition : ship.getPositions()) {
            Rectangle r = new Rectangle(50, 70);
            r.setFill(Color.RED);
            pane.add(r, shipPosition.getY(), shipPosition.getX());
        }
    }

    private void showDialog() {

    }

}
