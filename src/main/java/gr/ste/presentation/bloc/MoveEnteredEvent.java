package gr.ste.presentation.bloc;

import gr.ste.domain.entities.Position;

public class MoveEnteredEvent extends BattleshipGameEvent {
    private Position targetPosition;
    private int targetPlayerId;

    MoveEnteredEvent(Position targetPosition, int targetPlayerId) {
        this.targetPosition = targetPosition;
        this.targetPlayerId = targetPlayerId;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public int getTargetPlayerId() {
        return targetPlayerId;
    }
}
