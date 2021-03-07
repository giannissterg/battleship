package gr.ste.presentation.widgets;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

public class BattleshipMenuItem extends MenuItem {
    public BattleshipMenuItem(String itemName) {
        super(itemName);
    }

    public BattleshipMenuItem(String itemName, EventHandler<ActionEvent> actionEventHandler) {
        super(itemName);
        setOnAction(actionEventHandler);
    }

    public BattleshipMenuItem(String itemName, EventHandler<ActionEvent> actionEventHandler, KeyCombination keyCombination) {
        super(itemName);
        setOnAction(actionEventHandler);
        setAccelerator(keyCombination);
    }
}
