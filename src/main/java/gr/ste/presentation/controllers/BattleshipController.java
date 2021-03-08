package gr.ste.presentation.controllers;

import gr.ste.BattleshipApplication;
import gr.ste.domain.entities.Board;
import gr.ste.domain.entities.Move;
import gr.ste.domain.entities.Position;
import gr.ste.domain.entities.Ship;
import gr.ste.domain.exceptions.InvalidScenarioException;
import gr.ste.presentation.events.MoveEnteredEvent;
import gr.ste.presentation.utilities.PresentationUtilities;
import gr.ste.presentation.view_models.BattleshipViewModel;
import gr.ste.presentation.widgets.BattleshipGridPane;
import gr.ste.presentation.widgets.BattleshipMenuItem;
import gr.ste.presentation.widgets.Tile;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

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

    private final BattleshipApplication applicationInstance;

    public BattleshipController(BattleshipViewModel battleshipViewModel, BattleshipApplication applicationInstance) {
        this.battleshipViewModel = battleshipViewModel;
        this.applicationInstance = applicationInstance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setOpacity(0.0f);
        fadeIn();
        initView();
        bindProperties();
    }

    private void initView() {
        // MenuBar
        MenuBar gameMenuBar = createMenuBar();
        root.getChildren().add(0, gameMenuBar);

        Image image = PresentationUtilities.loadImage("images/map.jpg");
        assert image != null;
        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, true, false);
        BackgroundImage gameBackgroundImage = new BackgroundImage(image, BackgroundRepeat.ROUND, BackgroundRepeat.SPACE, BackgroundPosition.CENTER, backgroundSize);
        Background gameBackground = new Background(gameBackgroundImage);
        root.setBackground(gameBackground);

        xCoordinateTextField.textProperty().bindBidirectional(battleshipViewModel.gameState.xTargetCoordinate);
        yCoordinateTextField.textProperty().bindBidirectional(battleshipViewModel.gameState.yTargetCoordinate);

        CornerRadii radius = new CornerRadii(64.0);
        Background textFieldBackground = new Background(new BackgroundFill(Color.TRANSPARENT, radius, Insets.EMPTY));
        Border textFieldBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, radius, BorderWidths.DEFAULT));
        xCoordinateTextField.setBackground(textFieldBackground);
        xCoordinateTextField.setBorder(textFieldBorder);

        yCoordinateTextField.setBackground(textFieldBackground);
        yCoordinateTextField.setBorder(textFieldBorder);

        Background buttonBackground = new Background(new BackgroundFill(Color.TRANSPARENT, radius, Insets.EMPTY));
        fireButton.setBackground(buttonBackground);
        fireButton.setBorder(textFieldBorder);
        fireButton.setTextFill(Color.BLACK);

        invalidMoveLabel.textProperty().bind(battleshipViewModel.gameState.invalidMove);
        invalidMoveLabel.visibleProperty().bind(battleshipViewModel.gameState.showInvalidMoveLabel.not());
        fireButton.disableProperty().bind(battleshipViewModel.gameState.showInvalidMoveLabel.not().or(battleshipViewModel.gameState.hasLoadedGame.not()));

        Label roundLabel = new Label();
        roundLabel.textProperty().bind(Bindings.createStringBinding(() -> "Score: " + battleshipViewModel.gameState.rounds, battleshipViewModel.gameState.rounds);
        roundLabel.setFont(Font.font("Blackadder ITC", FontWeight.BOLD, 32));
        roundLabel.setPadding(new Insets(40, 0, 0, 0));

        HBox hbox = new HBox();
        for(int i = 0; i < battleshipViewModel.gameState.numberOfPlayers.get(); i++) {
            Label nameLabel = new Label();
            Label scoreLabel = new Label();
            Label hitPercentageLabel = new Label();
            int finalI = i;
            hitPercentageLabel.textProperty().bind(Bindings.createStringBinding(() -> "Percentage: " + String.format("%.2f", battleshipViewModel.gameState.playerStates.get(i).percentages.get(finalI).getValue()), battleshipViewModel.percentage.get(i)));
            hitPercentageLabel.setFont(Font.font("Blackadder ITC", FontWeight.NORMAL, 24));

            nameLabel.textProperty().bindBidirectional(battleshipViewModel.gameState(i));
            scoreLabel.textProperty().bindBidirectional(battleshipViewModel.getPlayerScoreProperty(i));

            nameLabel.setFont(Font.font("Blackadder ITC", FontWeight.BOLD, 28));
            scoreLabel.setFont(Font.font("Blackadder ITC", FontWeight.NORMAL, 26));

            BattleshipGridPane playerGrid = createGrid();
            playerGrid.setOpacity(0.8);

            Pane spacer = new Pane();
            spacer.setPrefSize(0, 64);

            Tile emptyTile = new Tile(40, 40);


            HBox xCoordinates = new HBox(emptyTile);
            for(int x = 0; x < Board.WIDTH; x++) {
                Tile tile = new Tile(40, 40);
                Label xCoordinate = new Label(Integer.toString(x));
                xCoordinate.setFont(Font.font("Blackadder ITC", FontWeight.BOLD, FontPosture.REGULAR, 24));
                tile.getChildren().add(xCoordinate);
                xCoordinates.getChildren().add(tile);
            }

            VBox yCoordinates = new VBox();
            for(int y = 0; y < Board.HEIGHT; y++) {
                Tile tile = new Tile(40, 40);
                Label yCoordinate = new Label(Integer.toString(y));
                yCoordinate.setFont(Font.font("Blackadder ITC", FontWeight.BOLD, FontPosture.REGULAR, 24));
                tile.getChildren().add(yCoordinate);
                yCoordinates.getChildren().add(tile);
            }
            HBox gridWithCoords = new HBox(yCoordinates, playerGrid);

            VBox playerScoreBoard = new VBox(nameLabel, scoreLabel, hitPercentageLabel, xCoordinates, gridWithCoords);
            if(i % 2 == 0) {
                playerScoreBoard.setAlignment(Pos.CENTER_LEFT);
            } else {
                playerScoreBoard.setAlignment(Pos.CENTER_RIGHT);
            }

            playerScoreBoard.setPadding(new Insets(32, 0,0,0));
            hbox.getChildren().add(playerScoreBoard);

            battleshipViewModel.getPlayerShips(i).addListener((ListChangeListener<Ship>) c -> {
                playerGrid.clear();
                c.getList().forEach(playerGrid::add);
            });
            battleshipViewModel.getMoves(i).addListener((ListChangeListener<Move>) c -> {
                if(c.next()) {
                    c.getAddedSubList().forEach(playerGrid::add);
                } else {
                    c.getList().forEach(playerGrid::add);
                }
            });
        }

        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(100.0);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(hbox);
        stackPane.getChildren().add(roundLabel);
        stackPane.setAlignment(Pos.TOP_CENTER);

        root.getChildren().add(1, stackPane);
    }

    private void percent(Position p, int percent) {

    }

    private void bindProperties() {

    }

    private void fadeIn() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), root);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    @FXML
    private void fire() {
        final String x = xCoordinateTextField.getText();
        final String y = yCoordinateTextField.getText();
        final Position targetPosition = new Position(Integer.parseInt(x), Integer.parseInt(y));
        MoveEnteredEvent moveEnteredEvent = new MoveEnteredEvent(targetPosition, 1);
        battleshipViewModel.playMove(moveEnteredEvent);
    }

    private void handleStartClickedEvent(ActionEvent actionEvent) {
        battleshipViewModel.hasLoadedGameProperty().setValue(true);
    }

    private void handleLoadClickedEvent(ActionEvent actionEvent) {
        battleshipViewModel.hasLoadedGameProperty().setValue(false);
        ImageView graphic = null;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("app_icon.jpg")) {
            if (is != null) {
                graphic = new ImageView(new Image(is, 70, 70, true, false));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        TextInputDialog dialog = createDialog("0", "Battleship Scenario Loader", "Load new game", "Please, specify scenario id", graphic);
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) {
            try {
                battleshipViewModel.loadGame(result.get());
            } catch (InvalidScenarioException e) {
                showAlert(e.getMessage());
            }
        }
    }

    private void showAlert(String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(contentText);
        alert.show();
    }

    private TextInputDialog createDialog(String textDefaultValue, String title, String headerText, String contentText, Node graphic) {
        TextInputDialog dialog = new TextInputDialog(textDefaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        dialog.setGraphic(graphic);
        return dialog;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        BattleshipMenuItem[] applicationMenuItems = {
                new BattleshipMenuItem("Start", this::handleStartClickedEvent, new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN)),
                new BattleshipMenuItem("Load", this::handleLoadClickedEvent, new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN))
        };
        BattleshipMenuItem[] detailsMenuItems = {
                new BattleshipMenuItem("Enemy ships"),
                new BattleshipMenuItem("Player shots"),
                new BattleshipMenuItem("Enemy shots")
        };
        Menu applicationMenu = createMenu("Application", applicationMenuItems);
        Menu detailsMenu = createMenu("Details", detailsMenuItems);

        menuBar.getMenus().addAll(applicationMenu, detailsMenu);
        return menuBar;
    }

    private Menu createMenu(String menuName, MenuItem[] menuItems) {
        Menu menu = new Menu(menuName);
        for(MenuItem menuItem : menuItems) {
            menu.getItems().add(menuItem);
        }
        return menu;
    }

    private BattleshipGridPane createGrid() {
        BattleshipGridPane gridPane = new BattleshipGridPane(Board.WIDTH, Board.HEIGHT, Board.WIDTH * 40, Board.HEIGHT * 40);
        gridPane.setBackgroundImage("images/sea.png");
        return gridPane;
    }
}
