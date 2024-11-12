package io.jacobking.quickticket.gui.controller;


import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.model.AlertModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends Controller {

    @FXML private TableView<AlertModel>            alertTable;
    @FXML private TableColumn<AlertModel, Boolean> checkColumn;
    @FXML private TableColumn<AlertModel, String>  alertNameColumn;
    @FXML private Button                           updateAlertButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up cell value factories
        alertNameColumn.setCellValueFactory(data -> data.getValue().alertNameProperty());
        checkColumn.setCellValueFactory(data -> data.getValue().alertStateProperty());

        // Set up CheckBox in the checkColumn
        checkColumn.setCellFactory(createCheckBoxCellFactory());

        // Load data into the TableView
        alertTable.setItems(bridgeContext.getAlerts().getObservableList());

        // Configure listener for parent alerts
        configureParentListener();

        updateAlertButton.setOnAction(event -> onUpdateAlerts());
    }

    private Callback<TableColumn<AlertModel, Boolean>, TableCell<AlertModel, Boolean>> createCheckBoxCellFactory() {
        return column -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                    AlertModel alertModel = getTableRow().getItem();
                    if (alertModel != null) {
                        alertModel.alertStateProperty().setValue(isSelected);
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }

                AlertModel alertModel = getTableRow().getItem();
                if (alertModel != null) {
                    // Set checkbox state
                    checkBox.setSelected(alertModel.alertStateProperty().get());

                    // Bind disable state for child checkboxes to the parent's alert state
                    if (alertModel.getAlertParentId() != 0) { // It's a child alert
                        alertTable.getItems().stream()
                                .filter(parent -> parent.getId() == alertModel.getAlertParentId())
                                .findFirst()
                                .ifPresent(parentAlert -> checkBox.disableProperty().bind(parentAlert.alertStateProperty()));
                    }

                    setGraphic(checkBox);
                }
            }
        };
    }


    private void configureParentListener() {
        alertTable.getItems().forEach(alert -> {
            if (alert.getAlertParentId() == 0) { // It's a parent alert
                alert.alertStateProperty().addListener((obs, wasEnabled, isEnabled) -> {
                    alertTable.getItems().stream()
                            .filter(childAlert -> childAlert.getAlertParentId() == alert.getId())
                            .forEach(childAlert -> {
                                childAlert.alertStateProperty().setValue(!isEnabled && childAlert.alertStateProperty().get());
                            });
                });
            }
        });
    }

    private void onUpdateAlerts() {
        int failureCounter = 0;
        for (final AlertModel alertModel : alertTable.getItems()) {
            if (!bridgeContext.getAlerts().update(alertModel)) {
                System.out.println("?");
                failureCounter++;
            }
        }

        if (failureCounter >= 1) {
            Announcements.get().showError("Error", "Could not update an alert model.");
            return;
        }

        Announcements.get().showConfirm("Success", "Alerts & notifications updated successfully.");
    }

}
