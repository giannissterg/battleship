package gr.ste;

import gr.ste.presentation.view_handlers.BattleshipApplicationViewHandler;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class BattleshipApplication extends Application {
    BattleshipApplicationViewHandler battleshipApplicationViewHandler;

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
     * @throws IOException if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        battleshipApplicationViewHandler = new BattleshipApplicationViewHandler(primaryStage);
        battleshipApplicationViewHandler.launchStartWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
