package gr.ste.presentation.events;

public class LoadGameEvent extends BattleshipGameEvent {
    private String scenarioId;

    public LoadGameEvent(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getScenarioId() {
        return scenarioId;
    }
}
