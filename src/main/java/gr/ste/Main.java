package gr.ste;

import gr.ste.data.CsvReader;
import gr.ste.data.GameRepositoryImpl;
import gr.ste.domain.base.ListMapper;
import gr.ste.domain.entities.Ship;
import gr.ste.domain.mappers.ShipMapper;
import gr.ste.domain.repositories.GameRepository;
import gr.ste.presentation.controllers.PlayerController;
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
        Callback<Class<?>, Object> playerControllerFactory = param -> {
            final CsvReader csvReader = new CsvReader();
            final ListMapper<Ship, String[]> shipListMapper = new ListMapper<>(new ShipMapper());
            final GameRepository gameRepository = new GameRepositoryImpl(csvReader, shipListMapper);
            return new PlayerController(gameRepository);
        };
        //save the factory in the injector
        DependencyInjection.addInjectionMethod(
                PlayerController.class, playerControllerFactory
        );
    }
}
