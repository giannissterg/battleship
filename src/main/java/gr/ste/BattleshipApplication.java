package gr.ste;

import gr.ste.data.CsvReader;
import gr.ste.data.GameRepositoryImpl;
import gr.ste.domain.base.ListMapper;
import gr.ste.domain.entities.Ship;
import gr.ste.domain.mappers.ShipMapper;
import gr.ste.domain.repositories.GameRepository;
import gr.ste.presentation.controllers.BattleshipController;
import gr.ste.presentation.controllers.StartMenuController;
import gr.ste.presentation.di.DependencyInjection;
import gr.ste.presentation.view_handlers.BattleshipApplicationViewHandler;
import gr.ste.presentation.view_handlers.ViewHandler;
import gr.ste.presentation.view_models.BattleshipViewModel;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class BattleshipApplication extends Application {
    private ViewHandler battleshipApplicationViewHandler;

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
        setupDependencyInjector();
        battleshipApplicationViewHandler = new BattleshipApplicationViewHandler(primaryStage);
        battleshipApplicationViewHandler.launchStartMenuWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setupDependencyInjector() {
        //set bundle
//        DependencyInjection.setBundle(ResourceBundle.getBundle("greetings", Locale.ENGLISH));

        //create factories - here we'll just create one!
        Callback<Class<?>, Object> startMenuControllerFactory = param -> new StartMenuController(this);

        Callback<Class<?>, Object> battleshipControllerFactory = param -> {
            final CsvReader csvReader = new CsvReader();
            final ListMapper<Ship, String[]> shipListMapper = new ListMapper<>(new ShipMapper());
            final GameRepository gameRepository = new GameRepositoryImpl(csvReader, shipListMapper);
            final BattleshipViewModel battleshipViewModel = new BattleshipViewModel(gameRepository);
            return new BattleshipController(battleshipViewModel, this);
        };
        //save the factory in the injector
        DependencyInjection.addInjectionMethod(
                StartMenuController.class, startMenuControllerFactory
        );

        DependencyInjection.addInjectionMethod(
                BattleshipController.class, battleshipControllerFactory
        );
    }

    public ViewHandler getViewHandler() {
        return battleshipApplicationViewHandler;
    }
}
