package gr.ste.presentation.controllers;

import gr.ste.BattleshipApplication;
import gr.ste.presentation.utilities.PresentationUtilities;
import gr.ste.presentation.view_handlers.BattleshipApplicationViewHandler;
import gr.ste.presentation.view_handlers.ViewHandler;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class StartMenuController implements Initializable {

    @FXML
    private VBox root;

    private Button button;

    private BattleshipApplication applicationInstance;

    public StartMenuController(BattleshipApplication applicationInstance) {
        this.applicationInstance = applicationInstance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initView();
    }

    private void initView() {
        Pane pane = new Pane();
        pane.setPrefSize(1280, 720);

        Image menuImage = PresentationUtilities.loadImage("images/menu.jpg");
        assert menuImage != null;
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true , false, true);
        BackgroundImage startMenuBackgroundImage = new BackgroundImage(menuImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background startMenuBackground = new Background(startMenuBackgroundImage);
        pane.setBackground(startMenuBackground);

        button = new Button("New game");
        button.setFont(Font.font("Blackadder ITC", FontWeight.BOLD, 38));
        button.setTextFill(Color.WHEAT);
        button.setLayoutX(700);
        button.setLayoutY(500);
        button.setOnMousePressed(this::startButtonClickedEvent);
        button.setOnMouseEntered(this::hover);
        button.setOnMouseExited(this::unhover);
        Background buttonBackground = new Background(new BackgroundFill(new Color(0.1, 0.05, 0.0, 0.4f), null, Insets.EMPTY));
        button.setBackground(buttonBackground);

        pane.getChildren().add(button);
        root.getChildren().add(pane);
    }

    private void startButtonClickedEvent(MouseEvent mouseEvent) {
        fadeOut();
    }

    private void hover(MouseEvent mouseEvent) {
        button.setTextFill(Color.BLACK);
    }

    private void unhover(MouseEvent mouseEvent) {
        button.setTextFill(Color.WHEAT);
    }

    private void fadeOut() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), root);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(actionEvent -> loadGameScene());
        fadeTransition.play();
    }

    private void loadGameScene() {
        try {
            applicationInstance.getViewHandler().launchGameWindow();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}

