package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.Version;
import io.jacobking.quickticket.core.config.SystemConfig;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.Display;
import io.jacobking.quickticket.gui.Route;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends Controller {

    @FXML private Label versionLabel;
    @FXML private TabPane tabPane;
    @FXML private Tab developerTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        versionLabel.setText(Version.current());


        final SystemConfig systemConfig = QuickTicket.getInstance().getSystemConfig();
        if (systemConfig.parseBoolean("first_launch")) {
            final Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(3), (__) -> Display.show(Route.WELCOME))
            );
            timeline.play();
        }

        if (systemConfig.parseBoolean("developer_mode")) {
            developerTab.setStyle("visibility: visible;");
            developerTab.setDisable(false);
        }
    }

    @FXML
    private void onAbout() {
        Display.show(Route.ABOUT);
    }

    @FXML
    private void onMetrics() {
       // Display.show(Route.METRICS);
    }

    @FXML
    private void onExit() {
        Announcements.get().showConfirmation(() -> QuickTicket.getInstance().shutdown(), "Are you sure you want to exit?", "All data is saved.").ifPresent(type -> {
            if (type == ButtonType.YES) {
                QuickTicket.getInstance().shutdown();
            }
        });
    }
}
