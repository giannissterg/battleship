package gr.ste.presentation.view_handlers;

import gr.ste.presentation.windows.BaseWindow;
import gr.ste.presentation.windows.BattleshipGameWindow;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class BattleshipApplicationViewHandler implements ViewHandler {

    private final Stage primaryStage;

    public BattleshipApplicationViewHandler(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void launchStartWindow() throws IOException {
        BattleshipGameWindow gameWindow = new BattleshipGameWindow();
        buildAndShowScene(primaryStage, gameWindow);
    }

    @Override
    public void launchAboutWindow() throws IOException {
        Stage aboutStage = new Stage();
        aboutStage.initModality(Modality.WINDOW_MODAL);
        buildAndShowScene(aboutStage, new BattleshipGameWindow());
    }

    private void buildAndShowScene(Stage stage, BaseWindow window) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(window.iconFilePath())) {
            if (is != null) {
                stage.getIcons().add(new Image(is));
            }
        }
        stage.setTitle(window.titleBundleKey());
        stage.setResizable(window.resizable());
        stage.setScene(new Scene(window.getView()));
        stage.show();
    }
}
