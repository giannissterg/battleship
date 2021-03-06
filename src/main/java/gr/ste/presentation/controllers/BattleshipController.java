package gr.ste.presentation.controllers;

import gr.ste.domain.entities.Board;
import gr.ste.domain.entities.Position;
import gr.ste.domain.entities.Ship;
import gr.ste.domain.exceptions.InvalidScenarioException;
import gr.ste.presentation.BattleshipViewModel;
import gr.ste.presentation.events.MoveEnteredEvent;
import gr.ste.presentation.widgets.BattleshipGridPane;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class BattleshipController implements Initializable {

    @FXML
    private VBox root;

    @FXML
    private Label invalidMoveLabel;

    @FXML
    private Button fireButton;

    @FXML
    private TextField xCoordinateTextField;

    @FXML
    private TextField yCoordinateTextField;

    private final BattleshipViewModel battleshipViewModel;

    public BattleshipController(BattleshipViewModel battleshipViewModel) {
        this.battleshipViewModel = battleshipViewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
    }

    private void initView() {
        // MenuBar gameMenuBar = createMenuBar();
        // root.getChildren().add(0, gameMenuBar);

        xCoordinateTextField.textProperty().bindBidirectional(battleshipViewModel.xTargetCoordinateProperty());
        yCoordinateTextField.textProperty().bindBidirectional(battleshipViewModel.yTargetCoordinateProperty());

        invalidMoveLabel.textProperty().bind(battleshipViewModel.getInvalidMoveProperty());
        invalidMoveLabel.visibleProperty().bind(battleshipViewModel.getShowInvalidMoveLabel().not());
        fireButton.disableProperty().bind(battleshipViewModel.getShowInvalidMoveLabel().not().or(battleshipViewModel.hasLoadedGameProperty().not()));

        HBox hbox = new HBox();

        for(int i = 0; i < battleshipViewModel.getNumberOfPlayers(); i++) {
            Label nameLabel = new Label();
            Label scoreLabel = new Label();

            nameLabel.textProperty().bindBidirectional(battleshipViewModel.getPlayerNameProperty(i));
            scoreLabel.textProperty().bindBidirectional(battleshipViewModel.getPlayerScoreProperty(i));

            BattleshipGridPane playerGrid = createGrid();
            VBox playerScoreBoard = new VBox(nameLabel, scoreLabel, playerGrid);
            if(i % 2 == 0) {
                playerScoreBoard.setAlignment(Pos.CENTER_LEFT);
            } else {
                playerScoreBoard.setAlignment(Pos.CENTER_RIGHT);
            }

            hbox.getChildren().add(playerScoreBoard);

            battleshipViewModel.getPlayerShips(i).addListener((ListChangeListener<Ship>) c -> c.getList().forEach(playerGrid::add));
            battleshipViewModel.getMoves(i).addListener((ListChangeListener<Position>) c -> {
                if(c.next()) {
                    c.getAddedSubList().forEach(playerGrid::add);
                } else {
                    c.getList().forEach(playerGrid::add);
                }
            });
        }
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(100.0);

        root.getChildren().add(1, hbox);
    }

    private BattleshipGridPane createGrid() {
        BattleshipGridPane gridPane = new BattleshipGridPane(Board.WIDTH, Board.HEIGHT, Board.WIDTH * 50, Board.HEIGHT * 50);
        try {
            gridPane.setBackgroundImage("images/sea.png");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return gridPane;
    }

    @FXML
    private void handleStartClickedEvent() {
        battleshipViewModel.hasLoadedGameProperty().setValue(true);
    }

    @FXML
    private void handleLoadClickedEvent() {
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Battleship Scenario Loader.");
        dialog.setHeaderText("Load new game");
        dialog.setContentText("Please, specify scenario id");
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("app_icon.jpg")) {
            if (is != null) {
                dialog.setGraphic(new ImageView(new Image(is, 70, 70, true, false)));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) {
            try {
                battleshipViewModel.loadGameState(result.get());
            } catch (InvalidScenarioException e) {
                showAlert();
            }
        }
    }

    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        // bind content text
        alert.contentTextProperty().bind(battleshipViewModel.getInvalidScenarioProperty());
        // show the dialog
        alert.show();
    }

    @FXML
    private void fire() {
        final String x = xCoordinateTextField.getText();
        final String y = yCoordinateTextField.getText();
        final Position targetPosition = new Position(Integer.parseInt(x), Integer.parseInt(y));
        MoveEnteredEvent moveEnteredEvent = new MoveEnteredEvent(targetPosition, 1);
        battleshipViewModel.playMove(moveEnteredEvent);
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
