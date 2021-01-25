package gr.ste.domain;

import gr.ste.domain.entities.Board;
import gr.ste.domain.entities.Player;

import java.io.Serializable;

public class BattleshipGame implements Serializable {
//    private final Player me, enemy;
    public final Board board;

    public BattleshipGame(Board board) {
        this.board = board;
    }
}
