package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.config.SystemConfig;
import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.custom.DashboardTab;
import io.jacobking.quickticket.gui.utility.FXUtility;
import io.jacobking.quickticket.gui.utility.IconLoader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends Controller {

    @FXML private HBox ticketBox;
    @FXML private Label ticketIconLabel;
    @FXML private Label ticketDisplayLabel;

    @FXML private HBox employeeBox;
    @FXML private Label employeeIconLabel;
    @FXML private Label employeeDisplayLabel;

    @FXML private HBox settingsBox;
    @FXML private Label settingsIconLabel;
    @FXML private Label settingsDisplayLabel;


    @FXML private AnchorPane ticketContent;
    @FXML private AnchorPane employeeContent;
    @FXML private AnchorPane settingsContent;

    private DashboardTab activeTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAndHandleEmbeddedControllers();
        configureFirstLaunch();

        final DashboardTab ticketTab = new DashboardTab("Tickets", ticketBox, ticketIconLabel, ticketDisplayLabel, ticketContent);
        ticketTab.setIcon(IconLoader.getMaterialIcon(MaterialDesign.MDI_TICKET_CONFIRMATION));

        final DashboardTab employeeTab = new DashboardTab("Employee", employeeBox, employeeIconLabel, employeeDisplayLabel, employeeContent);
        employeeTab.setIcon(IconLoader.getMaterialIcon(Material2MZ.PERSON));

        final DashboardTab settingsTab = new DashboardTab("Settings", settingsBox, settingsIconLabel, settingsDisplayLabel, settingsContent);
        settingsTab.setIcon(IconLoader.getMaterialIcon(Material2MZ.SETTINGS));

        this.activeTab = ticketTab;
        activeTab.activate();
        //contentTitle.setText(inventoryTab.getTitle());

        ticketBox.setOnMousePressed(event -> handleTabChange(ticketTab));
        employeeBox.setOnMousePressed(event -> handleTabChange(employeeTab));
        settingsBox.setOnMousePressed(event -> handleTabChange(settingsTab));
    }

    private void handleTabChange(final DashboardTab tab) {
        if (activeTab == tab)
            return;

        activeTab.deactivate();
        tab.activate();
        this.activeTab = tab;

        //contentTitle.setText(tab.getTitle());
    }

    private void configureFirstLaunch() {
        final SystemConfig systemConfig = QuickTicket.getInstance().getSystemConfig();
        if (systemConfig.parseBoolean("first_launch")) {
            final Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(3), (__) -> display.show(Route.WELCOME))
            );
            timeline.play();
        }
    }

    @FXML
    private void onAbout() {
        display.show(Route.ABOUT);
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
        ticketContent.getChildren().add(ticketParent);

        final EmployeeController employeeController = new EmployeeController();
        employeeController.controllerInitialization();

        final Parent employeeParent = FXUtility.getParent("fxml/employee.fxml", employeeController);
        employeeContent.getChildren().add(employeeParent);

        final SettingsController settingsController = new SettingsController();
        settingsController.controllerInitialization();

        final Parent settingsParent = FXUtility.getParent("fxml/settings.fxml", settingsController);
        settingsContent.getChildren().add(settingsParent);
    }
}
