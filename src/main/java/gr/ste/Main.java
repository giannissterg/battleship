package gr.ste;

import gr.ste.data.CsvReader;
import gr.ste.data.GameRepositoryImpl;
import gr.ste.domain.base.ListMapper;
import gr.ste.domain.entities.Ship;
import gr.ste.domain.mappers.ShipMapper;
import gr.ste.domain.repositories.GameRepository;
import gr.ste.presentation.BattleshipViewModel;
import gr.ste.presentation.controllers.BattleshipController;
import gr.ste.presentation.controllers.StartMenuController;
import gr.ste.presentation.di.DependencyInjection;
import javafx.util.Callback;

public class Main {
    public static void main(String[] args) {
        setupDependencyInjector();
        BattleshipApplication.main(args);
    }

    private static void setupDependencyInjector() {
        //set bundle
//        DependencyInjection.setBundle(ResourceBundle.getBundle("greetings", Locale.ENGLISH));

        //create factories - here we'll just create one!
        Callback<Class<?>, Object> startMenuControllerFactory = param -> {
            return new StartMenuController();
        };

        Callback<Class<?>, Object> battleshipControllerFactory = param -> {
            final CsvReader csvReader = new CsvReader();
            final ListMapper<Ship, String[]> shipListMapper = new ListMapper<>(new ShipMapper());
            final GameRepository gameRepository = new GameRepositoryImpl(csvReader, shipListMapper);
            final BattleshipViewModel battleshipViewModel = new BattleshipViewModel(gameRepository);
            return new BattleshipController(battleshipViewModel);
        };
        //save the factory in the injector
        DependencyInjection.addInjectionMethod(
                StartMenuController.class, startMenuControllerFactory
        );

        DependencyInjection.addInjectionMethod(
                BattleshipController.class, battleshipControllerFactory
        );

    }
}
