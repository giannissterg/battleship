package gr.ste.presentation.controllers;

import gr.ste.domain.BattleshipGame;
import gr.ste.domain.base.Result;
import gr.ste.domain.entities.Player;
import gr.ste.domain.entities.Position;
import gr.ste.domain.entities.Ship;
import gr.ste.domain.entities.ShipPosition;
import gr.ste.domain.repositories.GameRepository;
import gr.ste.presentation.events.BattleshipGameEvent;
import gr.ste.presentation.events.MoveEnteredEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class PlayerController implements Initializable {

    @FXML
    private GridPane myGridPane;

    @FXML
    private GridPane enemyGridPane;

    @FXML
    private Button fireButton;

    @FXML
    private TextField xCoordinateTextField;

    @FXML
    private TextField yCoordinateTextField;

    private final GameRepository gameRepository;
    private BattleshipGame gameState;

    public PlayerController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Result<BattleshipGame> domainResponse = gameRepository.loadScenario(
                Objects.requireNonNull(getClass().getClassLoader().getResource("player_default.txt")).getFile(),
                Objects.requireNonNull(getClass().getClassLoader().getResource("enemy_default.txt")).getFile()
        );
        fireButton.setOnAction(e -> fire());
        if (!domainResponse.hasError()) {
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

            for (Player player : gameState.players) {
                player.getBoard().ships.forEach(ship -> placeShip(ship, player == gameState.findPlayerById(0) ? myGridPane : enemyGridPane));
            }
        } else {
            showDialog();
        }
        gameState.playRound();
    }

    @FXML
    void fire() {
        final String x = xCoordinateTextField.getText();
        final String y = yCoordinateTextField.getText();
        final Position targetPosition = new Position(Integer.parseInt(x), Integer.parseInt(y));
        MoveEnteredEvent moveEnteredEvent = new MoveEnteredEvent(targetPosition, 1);
        updateBoard(moveEnteredEvent);
        System.out.println(x);
        System.out.println(y);
    }

    private void updateBoard(BattleshipGameEvent event) {
        if (event instanceof MoveEnteredEvent) {
            MoveEnteredEvent moveEnteredEvent = (MoveEnteredEvent) event;

            Position targetPosition = moveEnteredEvent.getTargetPosition();
            Player targetPlayer = gameState.findPlayerById(moveEnteredEvent.getTargetPlayerId());
            for (Ship enemyShip : targetPlayer.getBoard().ships) {
                for (ShipPosition shipPart : enemyShip.getPositions()) {
                    if (moveEnteredEvent.getTargetPosition().getX() == shipPart.getX() && moveEnteredEvent.getTargetPosition().getY() == shipPart.getY()) {
                        Rectangle r = new Rectangle(50,50);
                        r.setFill(Color.BLACK);
                        enemyGridPane.add(r, targetPosition.getY(), targetPosition.getX());
                        //TODO: update tile
                        break;
                    } else {
                        Rectangle r = new Rectangle(50,50);
                        r.setFill(Color.GRAY);
                        enemyGridPane.add(r, targetPosition.getY(), targetPosition.getX());
//                        gameState.findPlayerById(moveEnteredEvent.getTargetPlayerId()).getBoard().misses.add(targetPosition);
                        break;
                    }
                }
            }
            gameState.nextPlayer();
        }
    }

    private void placeShip(Ship ship, GridPane pane) {
        for (ShipPosition shipPosition : ship.getPositions()) {
            Rectangle r = new Rectangle(50, 50);
            r.setFill(Color.RED);
            pane.add(r, shipPosition.getY(), shipPosition.getX());
        }
    }

    private void showDialog() {

    }

}
