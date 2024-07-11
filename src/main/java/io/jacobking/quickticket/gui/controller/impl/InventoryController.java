package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.alert.Alerts;
import io.jacobking.quickticket.gui.alert.Notifications;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.misc.PopOverBuilder;
import io.jacobking.quickticket.gui.model.impl.EmployeeModel;
import io.jacobking.quickticket.gui.model.impl.InventoryModel;
import io.jacobking.quickticket.gui.utility.FALoader;
import io.jacobking.quickticket.tables.pojos.Inventory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.glyphfont.FontAwesome;

import java.net.URL;
import java.util.ResourceBundle;

public class InventoryController extends Controller {

    @FXML private TableView<InventoryModel>            inventoryTable;
    @FXML private TableColumn<InventoryModel, Void>    actionsColumn;
    @FXML private TableColumn<InventoryModel, String>  nameColumn;
    @FXML private TableColumn<InventoryModel, Integer> totalColumn;
    @FXML private TableColumn<InventoryModel, Integer> issuedColumn;
    @FXML private TableColumn<InventoryModel, String>  issuedDateColumn;

    @FXML private PieChart pieChart;

    @FXML private Button deleteButton;
    @FXML private Button editButton;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTable();
        configurePieChart();
        configureButtonBindings();
    }

    private void configureTable() {
        inventoryTable.setItems(inventory.getObservableList());

        actionsColumn.setCellFactory(data -> new TableCell<>() {
            @Override protected void updateItem(Void unused, boolean b) {
                super.updateItem(unused, b);
                if (b) {
                    setGraphic(null);
                    return;
                }

                final HBox buttonBox = new HBox();
                buttonBox.setSpacing(2.5);
                buttonBox.setAlignment(Pos.CENTER);

                final Button increment = new Button();
                increment.setGraphic(FALoader.createDefault(FontAwesome.Glyph.PLUS).size(10));
                increment.setOnAction(event -> incrementAsset(getTableRow().getItem()));

                final Button decrement = new Button();
                decrement.setGraphic(FALoader.createDefault(FontAwesome.Glyph.MINUS).size(10));
                decrement.setOnAction(event -> decrementAsset(getTableRow().getItem(), decrement));

                buttonBox.getChildren().addAll(increment, decrement);
                setGraphic(buttonBox);
            }
        });

        nameColumn.setCellValueFactory(data -> data.getValue().assetNameProperty());
        totalColumn.setCellValueFactory(data -> data.getValue().totalCountProperty().asObject());
        issuedColumn.setCellValueFactory(data -> data.getValue().lastIssuedProperty().asObject());
        issuedColumn.setCellFactory(data -> new TableCell<>() {
            @Override protected void updateItem(Integer s, boolean b) {
                super.updateItem(s, b);
                if (s == null || b) {
                    setText(null);
                    return;
                }

                final EmployeeModel employeeModel = employee.getModel(s);
                if (employeeModel == null) {
                    setText("Unknown");
                    return;
                }

                setText(employeeModel.getFullName());
            }
        });

        issuedDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
                DateUtil.formatDate(DateUtil.DateFormat.DATE_ONE, data.getValue().getLastIssuedDate())
        ));
    }

    private void incrementAsset(final InventoryModel model) {
        final int currentCount = model.getTotalCount();
        final int newCount = currentCount + 1;

        model.setTotalCount(newCount);
        if (!inventory.update(model)) {
            Alerts.get().showError("Failed", "Could not increase asset count.", "Please try again.");
            return;
        }
        inventoryTable.refresh();
    }

    private void decrementAsset(final InventoryModel inventoryModel, final Button decrementButton) {
        final PopOverBuilder builder = new PopOverBuilder()
                .setOwner(decrementButton)
                .setArrowOrientation(PopOver.ArrowLocation.BOTTOM_LEFT)
                .setTitle("Issue Asset")
                .useDefaultSettings();

        builder.setContent(getDecrementContent(builder, inventoryModel));
        builder.show();
    }

    private HBox getDecrementContent(final PopOverBuilder builder, final InventoryModel model) {
        final HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(5.0);
        hBox.setPrefWidth(250);
        hBox.setPadding(new Insets(12));

        final SearchableComboBox<EmployeeModel> comboBox = new SearchableComboBox<>(employee.getObservableList());

        final Button submit = new Button();
        submit.disableProperty().bind(comboBox.selectionModelProperty().isNull());

        submit.setOnAction(event -> {
            final int issuedId = comboBox.getSelectionModel().getSelectedItem().getId();

            final int currentCount = model.getTotalCount();
            final int newCount = currentCount - 1;

            model.setTotalCount(newCount);
            model.setLastIssued(issuedId);
            model.setLastIssuedDate(DateUtil.nowAsString(DateUtil.DateFormat.DATE_ONE));
            if (!inventory.update(model)) {
                Alerts.get().showError("Failed", "Could not decrease asset count.", "Please try again.");
                return;
            }
            inventoryTable.refresh();
            builder.hide();
        });

        submit.setGraphic(FALoader.createDefault(FontAwesome.Glyph.CHECK));
        hBox.getChildren().addAll(comboBox, submit);
        return hBox;
    }

    private void configurePieChart() {
        final ObservableList<InventoryModel> inventoryList = inventory.getObservableList();
        final ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (final InventoryModel model : inventoryList) {
            addDataAndBind(model, pieChartData);
        }
        configureInventoryListener(pieChartData, inventoryList);
        pieChart.setData(pieChartData);
    }

    private void configureInventoryListener(final ObservableList<PieChart.Data> pieChartData, final ObservableList<InventoryModel> inventoryModels) {
        inventoryModels.addListener((ListChangeListener<? super InventoryModel>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (final InventoryModel model : change.getAddedSubList()) {
                        addDataAndBind(model, pieChartData);
                    }
                }

                if (change.wasRemoved()) {
                    for (final InventoryModel model : change.getRemoved()) {
                        pieChartData.removeIf(data -> data.getName().equals(model.getAssetName()));
                    }
                }
            }
        });
    }

    private void addDataAndBind(final InventoryModel inventoryModel, final ObservableList<PieChart.Data> pieChartData) {
        final PieChart.Data data = new PieChart.Data(inventoryModel.getAssetName(), inventoryModel.getTotalCount());
        data.pieValueProperty().bind(inventoryModel.totalCountProperty());
        pieChartData.add(data);
    }

    private void configureButtonBindings() {
        editButton.disableProperty().bind(inventoryTable.selectionModelProperty().isNull());
        deleteButton.disableProperty().bind(editButton.disabledProperty());
    }

    @FXML private void onNew() {

    }

    private HBox getNewContent() {
        final HBox hBox = new HBox(5.0);
        hBox.setPadding(new Insets(12));
        hBox.setPrefWidth(250);

        final TextField assetNameField = new TextField();
        assetNameField.setPromptText("Asset Name");

        final TextField countField = new TextField();
        countField.setPromptText("Total Count");
        configureCountField(countField);

        final Button button = new Button();
        button.setGraphic(FALoader.createDefault(FontAwesome.Glyph.CHECK));
        button.setOnAction(event -> createNewAsset(assetNameField.getText(), countField.getText()));
        button.disableProperty().bind(assetNameField.textProperty().isEmpty());
        hBox.getChildren().addAll(assetNameField, countField, button);
        return hBox;
    }

    private void configureCountField(final TextField countField) {

    }

    private void createNewAsset(final String assetName, final String assetCount) {
        final int parsedCount = Integer.parseInt(assetCount);
        if (parsedCount < 0) {
            Alerts.get().showError("Failure", "You cannot have a total count less than 0.", "Try again.");
            return;
        }

    }

    @FXML private void onEdit() {

    }

    @FXML private void onDelete() {
        final InventoryModel model = inventoryTable.getSelectionModel().getSelectedItem();
        Alerts.get().showConfirmation(() -> deleteInventoryItem(model),
                "Are you sure you want to delete this item?",
                "It cannot be recovered."
        ).ifPresent(type -> {
            if (type == ButtonType.YES) {
                deleteInventoryItem(model);
            }
        });
    }

    private void deleteInventoryItem(final InventoryModel model) {
        inventory.remove(model.getId());
        Notifications.showInfo("Success", "Inventory Asset Deleted");
    }
}


