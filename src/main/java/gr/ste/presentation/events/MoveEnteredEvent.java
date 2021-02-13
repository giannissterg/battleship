package gr.ste.presentation.events;

import gr.ste.domain.entities.Position;

public class MoveEnteredEvent extends BattleshipGameEvent {
    private Position targetPosition;
    private int targetPlayerId;

    public MoveEnteredEvent(Position targetPosition, int targetPlayerId) {
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
