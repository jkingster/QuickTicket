package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.Version;
import io.jacobking.quickticket.gui.alert.Notify;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.utility.FALoader;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends Controller {

    @FXML
    private Label versionLabel;

    @FXML

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        versionLabel.setText(Version.current());
    }

    @FXML
    private void onDatabase() {
        Display.show(Route.DATABASE);
    }
    @FXML
    private void onSMTP() {
        Display.show(Route.SMTP);
    }

    @FXML
    private void onAbout() {
        Display.show(Route.ABOUT);
    }

    @FXML
    private void onExit() {
        Notify.showConfirmation("Are you sure you want to exit?", "All data is saved.").ifPresent(type -> {
            if (type == ButtonType.YES) {
                QuickTicket.shutdown();
            }
        });
    }
}
