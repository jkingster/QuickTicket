package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.Version;
import io.jacobking.quickticket.core.config.SystemConfig;
import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.utility.FXUtility;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends Controller {

    @FXML private Label   versionLabel;
    @FXML private TabPane tabPane;
    @FXML private Tab     developerTab;

    @FXML private AnchorPane ticketPane;
    @FXML private AnchorPane employeePane;
    @FXML private AnchorPane settingsPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAndHandleEmbeddedControllers();

        versionLabel.setText(Version.current());


        final SystemConfig systemConfig = QuickTicket.getInstance().getSystemConfig();
        if (systemConfig.parseBoolean("first_launch")) {
            final Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(3), (__) -> display.show(Route.WELCOME))
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
        display.show(Route.ABOUT);
    }

    @FXML
    private void onMetrics() {
        // display.show(Route.METRICS);
    }

    @FXML
    private void onExit() {
        Announcements.get().showConfirmation(() -> QuickTicket.getInstance().shutdown(), "Are you sure you want to exit?", "All data is saved.").ifPresent(type -> {
            if (type == ButtonType.YES) {
                QuickTicket.getInstance().shutdown();
            }
        });
    }

    private void loadAndHandleEmbeddedControllers() {

        final TicketController ticketController = new TicketController();
        ticketController.controllerInitialization();

        final Parent ticketParent = FXUtility.getParent("fxml/ticket.fxml", ticketController);
        ticketPane.getChildren().add(ticketParent);

        final EmployeeController employeeController = new EmployeeController();
        employeeController.controllerInitialization();

        final Parent employeeParent = FXUtility.getParent("fxml/employee.fxml", employeeController);
        employeePane.getChildren().add(employeeParent);

        final SettingsController settingsController = new SettingsController();
        settingsController.controllerInitialization();

        final Parent settingsParent = FXUtility.getParent("fxml/settings.fxml", settingsController);
        settingsPane.getChildren().add(settingsParent);
    }
}
