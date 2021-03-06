package gr.ste.presentation.view_handlers;

import gr.ste.presentation.windows.BaseWindow;

import java.io.IOException;

public interface ViewHandler {
    void launchStartMenuWindow() throws IOException;
    void launchGameWindow() throws IOException;
    void launchAboutWindow() throws IOException;
    void launch(BaseWindow window) throws IOException;
}