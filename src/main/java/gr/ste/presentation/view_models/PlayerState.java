package gr.ste.presentation.view_models;

import gr.ste.domain.entities.Move;
import gr.ste.domain.entities.Player;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class PlayerState {
    public final IntegerProperty id;
    public final StringProperty name;
    public final IntegerProperty score;

    public final Map<Integer, ObservableList<Move>> moves;
    public final Map<Integer, DoubleProperty> percentages;

    public final BoardState boardState;

    public PlayerState(int id) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty("Player " + id);
        this.score = new SimpleIntegerProperty();

        this.moves = new HashMap<>();
        this.percentages = new HashMap<>();

        this.boardState = new BoardState();
    }

    public PlayerState(Player player) {
        this.id = new SimpleIntegerProperty(player.getId());
        this.name = new SimpleStringProperty(player.getName());
        this.score = new SimpleIntegerProperty(player.getScore());

        this.moves = new HashMap<>();
        for (Map.Entry<Integer, Stack<Move>> entry : player.getMovesMap().entrySet()) {
            Integer integer = entry.getKey();
            Stack<Move> value = entry.getValue();

            ObservableList<Move> movesList = FXCollections.observableList(value);
            this.moves.put(integer, movesList);
        }

        this.percentages = new HashMap<>();
        for (Map.Entry<Integer, Double> entry : player.computePercentages().entrySet()) {
            Integer integer = entry.getKey();
            Double aDouble = entry.getValue();
            this.percentages.put(integer, new SimpleDoubleProperty(aDouble));
        }
        this.boardState = new BoardState(player.getBoard());
    }

    public void add(Move move) {
        this.moves.get(move.getTargetId()).add(move);
    }
}
