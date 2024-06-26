package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.Version;
import io.jacobking.quickticket.gui.alert.Alerts;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends Controller {

    @FXML
    private Label versionLabel;


    @FXML

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        versionLabel.setText(Version.current());

        if (QuickTicket.getInstance().getSystemConfig().parseBoolean("first_launch")) {
            final Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(3), (__) -> Display.show(Route.WELCOME))
            );
            timeline.play();
        }
    }

    @FXML
    private void onSettings() {
        Display.show(Route.SETTINGS);
    }

    @FXML
    private void onConfiguration() {
        Display.show(Route.CONFIGURATION);
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
        Alerts.showConfirmation(() -> QuickTicket.getInstance().shutdown(), "Are you sure you want to exit?", "All data is saved.").ifPresent(type -> {
            if (type == ButtonType.YES) {
                QuickTicket.getInstance().shutdown();
            }
        });
    }
}
