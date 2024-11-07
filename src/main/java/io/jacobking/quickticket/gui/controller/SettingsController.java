package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.model.AlertModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends Controller {

    @FXML private TableView<AlertModel>           alertTable;
    @FXML private TableColumn<AlertModel, Void>   checkColumn;
    @FXML private TableColumn<AlertModel, String> alertNameColumn;
    @FXML private Button                          updateAlertButton;


    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureAlertTable();

        updateAlertButton.setOnAction(event -> onUpdateAlerts());
    }

    private void configureAlertTable() {
        checkColumn.setCellFactory(data -> new TableCell<>() {
            @Override protected void updateItem(Void unused, boolean b) {
                super.updateItem(unused, b);
                if (b) {
                    setGraphic(null);
                    return;
                }

                final AlertModel model = getTableRow().getItem();
                if (model == null) {
                    setGraphic(null);
                    return;
                }

                final CheckBox checkBox = new CheckBox();
                checkBox.setSelected(model.getAlertState());
                checkBox.disableProperty().bind(model.disabledStateProperty());

                final int modelId = model.getId();
                final boolean isParentModel = (modelId == 1) || (modelId == 2);
                if (isParentModel) {
                    handleParentState(modelId, checkBox, model);
                }

                checkBox.selectedProperty().bindBidirectional(model.alertStateProperty());

                setGraphic(checkBox);
            }
        });

        alertNameColumn.setCellValueFactory(data -> data.getValue().alertNameProperty());
        alertTable.setItems(bridgeContext.getAlerts().getObservableList());
    }

    @FXML private void onUpdateAlerts() {
        Announcements.get().showConfirmation(this::updateAlerts, "Confirmation", "Are you sure you want to update alerts and notifications?", "Original settings cannot be recovered.")
                .ifPresent(type -> {
                    if (type == ButtonType.YES) {
                        updateAlerts();
                    }
                });
    }

    private void updateAlerts() {
        final ObservableList<AlertModel> failedUpdates = FXCollections.observableArrayList();
        bridgeContext.getAlerts().getObservableList().forEach(model -> {
            if (!bridgeContext.getAlerts().update(model)) {
                failedUpdates.add(model);
            }
        });

        if (!failedUpdates.isEmpty()) {
            Announcements.get().showWarning("Could not update alerts.", "Total failed: " + failedUpdates.size());
            return;
        }

        Announcements.get().showInfo("Success", "Alerts & notifications updated.");
    }

    private void handleParentState(final int modelId, final CheckBox checkBox, final AlertModel model) {
        checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            bridgeContext.getAlerts().getObservableList().forEach(targetModel -> {
                final int parentId = targetModel.getAlertParentId();
                if (parentId == modelId) {
                    targetModel.setDisabledState(isSelected);
                    targetModel.setAlertState(false);
                }
            });

            alertTable.refresh(); // Refresh the table after processing all the models
        });
    }

}
