package gr.ste.presentation.view_models;

import gr.ste.domain.entities.Board;
import gr.ste.domain.entities.Ship;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BoardState {
    public final ObservableList<Ship> ships;

    public BoardState() {
        this.ships = FXCollections.emptyObservableList();
    }

    public BoardState(Board board) {
        this.ships = FXCollections.observableList(board.ships);
    }
}
