package gr.ste.presentation.windows;

import gr.ste.presentation.di.DependencyInjection;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public abstract class BaseWindow {
    public Parent getView() throws IOException {
        return DependencyInjection.load(fxmlFileName());
    }

    private URL url() {
        return getClass().getClassLoader().getResource(/*AppPaths.FXML_PATH +*/ fxmlFileName());
    }

    public String iconFilePath() {
        return /*AppPaths.IMG_PATH +*/ iconFileName();
    }

    public boolean resizable() {
        return false;
    }

    protected abstract String iconFileName();
    protected abstract String fxmlFileName();
    public abstract String styleSheetFileName();
    public abstract String titleBundleKey();
}
