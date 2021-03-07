package gr.ste.presentation.utilities;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;

public class PresentationUtilities {
    public static Image loadImage(String imagePath) {
        try (InputStream is = PresentationUtilities.class.getClassLoader().getResourceAsStream(imagePath)) {
            if (is != null) {
                return new Image(is);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
