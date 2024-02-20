package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.Version;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.gui.alert.Notify;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends Controller {

    @FXML
    private Label versionLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        versionLabel.setText(Version.current());
    }

    @FXML
    private void onAbout() {
        Display.show(Route.ABOUT, null);
    }

    @FXML
    private void onExit() {
        Notify.showConfirmation("Are you sure you want to exit?", "All data is saved.").ifPresent(type -> {
            if (type == ButtonType.YES) {
                Database.getInstance().close();
                Platform.exit();
            }
        });
    }


}
