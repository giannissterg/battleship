package gr.ste.presentation.controllers;

import gr.ste.BattleshipApplication;
import gr.ste.domain.entities.Move;
import gr.ste.domain.entities.Position;
import gr.ste.domain.exceptions.InvalidScenarioException;
import gr.ste.presentation.events.LoadGameEvent;
import gr.ste.presentation.events.MoveEnteredEvent;
import gr.ste.presentation.events.ShowEnemyShipsEvent;
import gr.ste.presentation.events.StartGameEvent;
import gr.ste.presentation.utilities.PresentationUtilities;
import gr.ste.presentation.view_models.BattleshipViewModel;
import gr.ste.presentation.view_models.GameState;
import gr.ste.presentation.view_models.PlayerState;
import gr.ste.presentation.widgets.BattleshipMenuItem;
import gr.ste.presentation.widgets.BoardWidget;
import gr.ste.presentation.widgets.BottomSection;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class BattleshipController implements Initializable {

    @FXML
    private VBox root;

    private List<BoardWidget> boardWidgets;
    private BottomSection bottomSection;
    private Label roundLabel;

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
        initView(battleshipViewModel.initialGameState);
//        battleshipViewModel.gameStateList.addListener((ListChangeListener<GameState>) c -> {
//            if(c.next()) {
//                for (GameState gameState : c.getAddedSubList()) {
//                    updateView(gameState);
//                }
//            }
//        });
    }

    private void initView(GameState gameState) {
        this.boardWidgets = new ArrayList<>();
        // MenuBar
        MenuBar gameMenuBar = createMenuBar();

        Image image = PresentationUtilities.loadImage("images/map.jpg");
        assert image != null;
        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, true, false);
        BackgroundImage gameBackgroundImage = new BackgroundImage(image, BackgroundRepeat.ROUND, BackgroundRepeat.SPACE, BackgroundPosition.CENTER, backgroundSize);
        Background gameBackground = new Background(gameBackgroundImage);
        root.setBackground(gameBackground);

        this.roundLabel = new Label();
        this.roundLabel.textProperty().bind(Bindings.createStringBinding(() -> "Round: " + gameState.rounds.getValue(), gameState.rounds));
        this.roundLabel.setFont(Font.font("Blackadder ITC", FontWeight.BOLD, 32));
        this.roundLabel.setPadding(new Insets(40, 0, 0, 0));

        HBox hbox = new HBox();
        for(PlayerState playerState : gameState.playerStates) {
            BoardWidget boardWidget = new BoardWidget(playerState);
            this.boardWidgets.add(boardWidget);
            hbox.getChildren().add(boardWidget);
        }

        gameState.playerStates.forEach(playerState -> playerState.moves.entrySet().forEach(entry -> {
            Integer i = entry.getKey();
            ObservableList<Move> moves = entry.getValue();
            moves.addListener((ListChangeListener<Move>) c -> {
                if(c.next()) {
                    c.getAddedSubList().forEach(boardWidgets.get(i).gridPane::add);
                }
            });
        }));

        gameState.showEndDialog.addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                showEnding();
            }
        });

        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(100.0);

        BottomSection bottomSection = new BottomSection(gameState);
        this.bottomSection = bottomSection;
        this.bottomSection.fireButton.setOnAction(this::fire);

        StackPane stackPane = new StackPane(hbox, roundLabel);
        stackPane.setAlignment(Pos.TOP_CENTER);

        root.getChildren().setAll(gameMenuBar, stackPane, bottomSection);
    }

    private void updateView(GameState gameState) {
        this.roundLabel.textProperty().bind(Bindings.createStringBinding(() -> "Round: " + gameState.rounds.getValue(), gameState.rounds));
        for (PlayerState playerState : gameState.playerStates) {
            this.boardWidgets.get(playerState.id.getValue()).update(playerState);
        }
        bottomSection.update(gameState);
    }

    private void fadeIn() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), root);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    private void fire(ActionEvent event) {
        final String x = bottomSection.xCoordinateTextField.getText();
        final String y = bottomSection.yCoordinateTextField.getText();
        final Position targetPosition = new Position(Integer.parseInt(x), Integer.parseInt(y));
        MoveEnteredEvent moveEnteredEvent = new MoveEnteredEvent(targetPosition, 1);
        try {
            battleshipViewModel.mapEventToState(moveEnteredEvent);
        } catch (InvalidScenarioException e) {
            showAlert(e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    private void showEnding() {
        int winnerId = 0;
        int maxScore = 0;
        int secondMaxScore = 0;
        for (PlayerState playerState : battleshipViewModel.initialGameState.playerStates) {
            if(playerState.score.getValue() > maxScore) {
                secondMaxScore = maxScore;
                maxScore = playerState.score.getValue();
                winnerId = playerState.id.getValue();
            } else if(playerState.score.getValue() > secondMaxScore) {
                secondMaxScore = playerState.score.getValue();
            }
        }
        if(maxScore == secondMaxScore) {
            showEndBanner("Game has ended", "It is a draw", Alert.AlertType.INFORMATION);
        } else {
            if(winnerId == 0) {
                showEndBanner("Game has ended", "Congratulations! You are the winner", Alert.AlertType.INFORMATION);
            } else {
                showEndBanner("Game has ended","Unfortunately, you lost!", Alert.AlertType.INFORMATION);
            }
        }
    }

    private void handleStartClickedEvent(ActionEvent actionEvent) {
        StartGameEvent event = new StartGameEvent();
        try {
            battleshipViewModel.mapEventToState(event);
        } catch (InvalidScenarioException e) {
            showAlert(e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    private void handleLoadClickedEvent(ActionEvent actionEvent) {
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
                LoadGameEvent event = new LoadGameEvent(result.get());
                battleshipViewModel.mapEventToState(event);
            } catch (InvalidScenarioException e) {
                showAlert(e.getMessage(), Alert.AlertType.WARNING);
            }
        }
    }

    private void handleShowEnemyShips(ActionEvent event) {
        ShowEnemyShipsEvent enemyShipsEvent = new ShowEnemyShipsEvent();
        try {
            battleshipViewModel.mapEventToState(enemyShipsEvent);
        } catch (InvalidScenarioException e) {
            showAlert(e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    private void handleLastPlayerShots(ActionEvent event) {

    }

    private void handleLastEnemyShots(ActionEvent event) {

    }

    private void showEndBanner(String title, String headerText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.show();
    }

    private void showAlert(String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
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
                new BattleshipMenuItem("Enemy ships", this::handleShowEnemyShips, new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN)),
                new BattleshipMenuItem("Player shots", this::handleLastPlayerShots, new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN)),
                new BattleshipMenuItem("Enemy shots", this::handleLastEnemyShots, new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN))
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
}
