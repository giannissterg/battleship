package gr.ste;

import com.sun.javafx.fxml.builder.JavaFXSceneBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class BattleshipApplication extends Application {

    private Stage mainWindow;
    private Scene mainScene;
    private final String mainWindowTitle = "MediaLab Battleship";

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        mainWindow = primaryStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("root.fxml")));

        mainScene = new Scene(root);
//        mainScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        mainWindow.setTitle(mainWindowTitle);
        mainWindow.setScene(mainScene);
        mainWindow.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
