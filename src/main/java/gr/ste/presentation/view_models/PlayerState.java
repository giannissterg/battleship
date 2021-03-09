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
    public final IntegerProperty activeShips;

    public PlayerState(int id) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty("Player " + id);
        this.score = new SimpleIntegerProperty();

        this.moves = new HashMap<>();
        this.percentages = new HashMap<>();

        this.boardState = new BoardState();
        this.activeShips = new SimpleIntegerProperty();
    }

    public PlayerState(Player player) {
        this.id = new SimpleIntegerProperty(player.getId());
        this.name = new SimpleStringProperty(player.getName());
        this.score = new SimpleIntegerProperty(player.getScore());
        this.activeShips = new SimpleIntegerProperty(player.getBoard().getActiveShips());

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

    public void update(Player player) {
        this.id.setValue(player.getId());
        this.name.setValue(player.getName());
        this.score.setValue(player.getScore());

        for (Map.Entry<Integer, Stack<Move>> entry : player.getMovesMap().entrySet()) {
            Integer integer = entry.getKey();
            Stack<Move> value = entry.getValue();
            this.getMoves(integer).add(value.lastElement());
        }

        for (Map.Entry<Integer, DoubleProperty> entry : percentages.entrySet()) {
            Integer integer = entry.getKey();
            DoubleProperty aDouble = entry.getValue();
            this.getPercentage(integer).setValue(0.0);
        }
        this.boardState.ships.setAll(player.getBoard().ships);
        this.activeShips.setValue(player.getBoard().getActiveShips());
    }

    public ObservableList<Move> getMoves(int enemyId) {
        moves.computeIfAbsent(enemyId, k -> FXCollections.observableArrayList());
        return moves.get(enemyId);
    }

    public DoubleProperty getPercentage(int enemyId) {
        percentages.computeIfAbsent(enemyId, k -> new SimpleDoubleProperty());
        return percentages.get(enemyId);
    }

    public void add(Move move) {
        this.moves.get(move.getTargetId()).add(move);
    }
}
