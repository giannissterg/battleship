package gr.ste.presentation.controllers;

import gr.ste.presentation.view_handlers.BattleshipApplicationViewHandler;
import gr.ste.presentation.view_handlers.ViewHandler;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartMenuController implements Initializable {

    @FXML
    private StackPane root;

    ViewHandler viewHandler;

    public StartMenuController() {
        this.viewHandler = new BattleshipApplicationViewHandler(new Stage());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initView();
    }

    private void initView() {

    }

    @FXML
    private void startButtonClickedEvent() {
        fadeOut();
    }

    private void fadeOut() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(actionEvent -> loadGameScene());
        fadeTransition.play();
    }

    private void loadGameScene() {
        try {
            viewHandler.launchGameWindow();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}

