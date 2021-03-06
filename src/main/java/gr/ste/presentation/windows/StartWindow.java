package gr.ste.presentation.windows;

public class StartWindow extends BaseWindow {

    @Override
    protected String iconFileName() {
        return "app_icon.jpg";
    }

    @Override
    protected String fxmlFileName() {
        return "start_window.fxml";
    }

    @Override
    public String styleSheetFileName() {
        return "index.css";
    }

    @Override
    public String titleBundleKey() {
        return "MediaLab Battleship";
    }
}
