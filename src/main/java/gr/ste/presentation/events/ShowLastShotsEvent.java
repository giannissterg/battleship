package gr.ste.presentation.events;

public class ShowLastShotsEvent extends BattleshipGameEvent {
    public final int playerId;

    public ShowLastShotsEvent(int id) {
        this.playerId = id;
    }
}
