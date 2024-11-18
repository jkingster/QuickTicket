package io.jacobking.quickticket.gui.utility;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.gui.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;

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
            throw new RuntimeException(e.fillInStackTrace());
        }
    }

    public static void resetFields(final Parent root, final String... ignoreNodeIds) {
        for (final Node node : root.getChildrenUnmodifiable()) {
            if (node instanceof TextField) {
                ((TextField) node).clear();
            } else if (node instanceof ComboBox<?>) {
                ((ComboBox<?>) node).getSelectionModel().clearSelection();
            } else if (node instanceof ToggleButton) {
                ((ToggleButton) node).setSelected(!((ToggleButton) node).isSelected());
            } else if (node instanceof CheckBox) {
                ((CheckBox) node).setSelected(false);
            } else if (node instanceof TextArea) {
                ((TextArea) node).clear();
            } else if (node instanceof Parent) {
                resetFields((Parent) node);
            }
        }
    }

}
