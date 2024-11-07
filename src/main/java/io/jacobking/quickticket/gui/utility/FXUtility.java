package io.jacobking.quickticket.gui.utility;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.core.utility.Logs;
import io.jacobking.quickticket.gui.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class FXUtility {

    private FXUtility() {
    }

    public static Parent getParent(final String path, final Controller controller) {
        final FXMLLoader loader = new FXMLLoader(App.class.getResource(path));
        loader.setController(controller);
        try {
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(Logs.exception(e));
        }
    }
}
