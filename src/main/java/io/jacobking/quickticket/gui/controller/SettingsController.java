package io.jacobking.quickticket.gui.controller;


import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.model.AlertModel;
import io.jacobking.quickticket.gui.model.ModuleModel;
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

    @FXML private TableView<ModuleModel>            moduleTable;
    @FXML private TableColumn<ModuleModel, Boolean> moduleCheckColumn;
    @FXML private TableColumn<ModuleModel, String>  moduleNameColumn;
    @FXML private TableColumn<ModuleModel, String>  moduleDescriptionColumn;
    @FXML private Button                            updateModuleButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureAlerts();
        configureModules();
    }

    private void configureAlerts() {
        alertNameColumn.setCellValueFactory(data -> data.getValue().alertNameProperty());
        checkColumn.setCellValueFactory(data -> data.getValue().alertStateProperty());
        checkColumn.setCellFactory(createCheckBoxCellFactory());
        alertTable.setItems(bridgeContext.getAlerts().getObservableList());
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

    private void configureModules() {
        moduleTable.setItems(bridgeContext.getModule().getObservableList());
        moduleCheckColumn.setCellValueFactory(data -> data.getValue().stateProperty());
        moduleNameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        moduleDescriptionColumn.setCellValueFactory(data -> data.getValue().descriptionProperty());

        moduleCheckColumn.setCellFactory(data -> {
            return new TableCell<>() {
                private final CheckBox checkBox = new CheckBox();

                {
                    checkBox.selectedProperty().addListener((observable, oldState, newState) -> {
                        if (newState == null) {
                            return;
                        }

                        final ModuleModel moduleModel = getTableRow().getItem();
                        if (moduleModel != null) {
                            moduleModel.stateProperty().setValue(newState);
                        }
                    });
                }

                @Override protected void updateItem(Boolean state, boolean empty) {
                    super.updateItem(state, empty);
                    if (state == null || empty) {
                        setGraphic(null);
                        return;
                    }

                    final ModuleModel model = getTableRow().getItem();
                    if (model != null) {
                        checkBox.setSelected(getTableRow().getItem().isEnabled());
                    }

                    setGraphic(checkBox);
                }
            };
        });

        updateModuleButton.setOnAction(event -> onUpdateModules());
    }

    private void onUpdateModules() {
        int moduleUpdateCounter = 0;
        for (final ModuleModel module : moduleTable.getItems()) {
            if (!bridgeContext.getModule().update(module)) {
                moduleUpdateCounter++;
            }
        }

        if (moduleUpdateCounter >= 1) {
            Announcements.get().showError("Error", "Failed to update module(s).", "Please try again.");
            return;
        }

        Announcements.get().showConfirm("Success", "Module(s) states updated.");
    }

}
