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
import gr.ste.presentation.widgets.BattleshipGridPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class PlayerController implements Initializable {

    @FXML
    private VBox root;

    @FXML
    private Button fireButton;

    @FXML
    private TextField xCoordinateTextField;

    @FXML
    private TextField yCoordinateTextField;

    private final GameRepository gameRepository;
    private BattleshipGame gameState;
    private final List<GridPane> boardPanes;

    public PlayerController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        this.boardPanes = new ArrayList<>();
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

            MenuBar gameMenuBar = createMenuBar();

            HBox hbox = new HBox();
            for (Player player : gameState.players) {
                Label nameLabel = new Label(player.getName());
                Label scoreLabel = new Label(Integer.toString(player.getScore()));

                BattleshipGridPane playerGrid = createGrid();
                VBox playerScoreBoard = new VBox(nameLabel, scoreLabel, playerGrid);
                if(player.getId() == 0) {
                    playerScoreBoard.setAlignment(Pos.CENTER_LEFT);
                } else {
                    playerScoreBoard.setAlignment(Pos.CENTER_RIGHT);
                }

                hbox.getChildren().add(playerScoreBoard);
                player.getBoard().ships.forEach(playerGrid::add);
                boardPanes.add(playerGrid);
            }

            hbox.setAlignment(Pos.CENTER);
            hbox.setSpacing(100.0);

            root.getChildren().add(0, gameMenuBar);
            root.getChildren().add(1, hbox);

        } else {
            showDialog();
        }
        gameState.playRound();
    }

    private BattleshipGridPane createGrid() {
        BattleshipGridPane gridPane = new BattleshipGridPane(10, 10, 500, 500);
        try {
            gridPane.setBackgroundImage("images/sea.png");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return gridPane;
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
            GridPane targetPane = boardPanes.get(moveEnteredEvent.getTargetPlayerId());

            boolean hitShip = false;
            for (Ship enemyShip : targetPlayer.getBoard().ships) {
                for (ShipPosition shipPart : enemyShip.getPositions()) {
                    if (moveEnteredEvent.getTargetPosition().getX() == shipPart.getX() && moveEnteredEvent.getTargetPosition().getY() == shipPart.getY()) {
                        hitShip = true;
                        break;
                    }
                }
            }
            showMove(targetPane, targetPosition, hitShip);
//            gameState.updateShip();
//            gameState.rewardPlayer(enemyShip, shipPosition);
//            gameState.update(moveEnteredEvent, hitShip);
//            gameState.findPlayerById(moveEnteredEvent.getTargetPlayerId()).getBoard().misses.add(targetPosition);

            gameState.nextPlayer();
            MoveEnteredEvent nextMoveTarget = gameState.playRound();
            updateBoard(nextMoveTarget);
        }
    }

    void showMove(GridPane targetPane, Position targetPosition, boolean success) {
        Rectangle r = new Rectangle(50.0,50.0);
        if(success) {
            r.setFill(Color.BLACK);
        } else {
            r.setFill(Color.GRAY);
        }
        targetPane.add(r, targetPosition.getY(), targetPosition.getX());
    }

    private void showDialog() {

    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu applicationMenu = createMenu("Application", new String[]{"Start", "Load"});
        Menu detailsMenu = createMenu("Details", new String[]{"Enemy ships", "Player shots", "Enemy shots"});
        Menu helpMenu = createMenu("Help", new String[]{"Help"});

        menuBar.getMenus().addAll(applicationMenu, detailsMenu, helpMenu);
        return menuBar;
    }

    private Menu createMenu(String menuName, String[] menuItemNames) {
        Menu menu = new Menu(menuName);
        for(String menuItemName : menuItemNames) {
            MenuItem menuItem = new MenuItem(menuItemName);
            menuItem.setOnAction(this::handleMenuClicked);
            menu.getItems().add(menuItem);
        }
        return menu;
    }

    private void handleMenuClicked(ActionEvent actionEvent) {
        System.out.println("A");
    }

}
