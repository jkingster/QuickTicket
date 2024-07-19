package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.utility.ImmutablePair;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.AlertSettingsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class NewSettingsController extends Controller {

    private       ObservableList<ImmutablePair<AlertSettingsModel, String>> alertSettingsList = FXCollections.observableArrayList();
    @FXML private TableView<AlertSettingsModel>                             alertsTable;
    @FXML private TableColumn<AlertSettingsModel, CheckBox>                 checkColumn;
    @FXML private TableColumn<AlertSettingsModel, String>                   typeColumn;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureAlertsTable();
    }

    private void configureAlertsTable() {

    }

}
