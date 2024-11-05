package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.misc.PopOverBuilder;
import io.jacobking.quickticket.gui.model.impl.EmployeeModel;
import io.jacobking.quickticket.gui.model.impl.InventoryModel;
import io.jacobking.quickticket.gui.utility.FALoader;
import io.jacobking.quickticket.tables.pojos.Inventory;
import io.jacobking.quickticket.tables.pojos.InventoryLog;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.glyphfont.FontAwesome;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class InventoryController extends Controller {

    @FXML private TableView<InventoryModel>            inventoryTable;
    @FXML private TableColumn<InventoryModel, Void>    actionsColumn;
    @FXML private TableColumn<InventoryModel, String>  nameColumn;
    @FXML private TableColumn<InventoryModel, Integer> totalColumn;
    @FXML private TableColumn<InventoryModel, Integer> issuedColumn;
    @FXML private TableColumn<InventoryModel, String>  issuedDateColumn;

    @FXML private PieChart pieChart;

    @FXML private Button newButton;
    @FXML private Button deleteButton;
    @FXML private Button viewLogButton;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTable();
        configurePieChart();
        deleteButton.disableProperty().bind(inventoryTable.getSelectionModel()
                .selectedItemProperty().isNull());
        viewLogButton.disableProperty().bind(inventoryTable.getSelectionModel()
                .selectedItemProperty().isNull());
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

                if (s == -1) {
                    setText("Unknown");
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
                data.getValue().getLastIssuedDate() == null
                ? "Never"
                : DateUtil.formatDate(DateUtil.DateFormat.DATE_ONE, data.getValue().getLastIssuedDate())
        ));

    }

    private void incrementAsset(final InventoryModel model) {
        final int currentCount = model.getTotalCount();
        final int newCount = currentCount + 1;

        model.setTotalCount(newCount);
        if (!inventory.update(model)) {
            Announcements.get().showError("Failed", "Could not increase asset count.", "Please try again.");
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

            final String date = DateUtil.nowAsString(DateUtil.DateFormat.DATE_ONE);

            model.setTotalCount(newCount);
            model.setLastIssued(issuedId);
            model.setLastIssuedDate(date);
            if (!inventory.update(model)) {
                Announcements.get().showError("Failed", "Could not decrease asset count.", "Please try again.");
                return;
            }

            createInventoryLog(date, currentCount, model);
            inventoryTable.refresh();
            builder.hide();
        });

        submit.setGraphic(FALoader.createDefault(FontAwesome.Glyph.CHECK));
        hBox.getChildren().addAll(comboBox, submit);
        return hBox;
    }

    private void createInventoryLog(final String date, final int currentCount, final InventoryModel model) {
        final InventoryLogModel inventoryLogModel = inventoryLog.createModel(new InventoryLog()
                .setAssetId(model.getId())
                .setCountAtTime(currentCount)
                .setIssuedDate(date)
                .setEmployeeId(model.getLastIssued())
        );

        if (inventoryLogModel == null) {
            Announcements.get().showError("Failure", "Failed to create asset transaction.", "Could not log asset decrement usage.");
        }
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

    @FXML private void onNew() {
        final PopOverBuilder builder = new PopOverBuilder()
                .useDefaultSettings()
                .setTitle("Create New Asset")
                .setArrowOrientation(PopOver.ArrowLocation.BOTTOM_LEFT)
                .setOwner(newButton);

        builder.setContent(getNewContent(builder));
        builder.show();
    }

    private HBox getNewContent(final PopOverBuilder builder) {
        final HBox hBox = new HBox(5.0);
        hBox.setPadding(new Insets(12));
        hBox.setPrefWidth(250);

        final TextField assetNameField = new TextField();
        assetNameField.setPromptText("Asset Name");

        final TextField countField = new TextField();
        countField.setPromptText("Total Count");
        countField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                countField.setText(t1.replaceAll("\\D", ""));
            }
        });

        final Button button = new Button();
        button.setGraphic(FALoader.createDefault(FontAwesome.Glyph.CHECK));
        button.setOnAction(event -> createNewAsset(builder, assetNameField.getText(), countField.getText()));
        button.disableProperty().bind(assetNameField.textProperty().isEmpty());
        hBox.getChildren().addAll(assetNameField, countField, button);
        return hBox;
    }

    private void createNewAsset(final PopOverBuilder builder, final String assetName, final String assetCount) {
        final int parsedCount = Integer.parseInt(assetCount);
        if (parsedCount < 0) {
            Announcements.get().showError("Failure", "You cannot have a total count less than 0.", "Try again.");
            return;
        }

        final InventoryModel model = inventory.createModel(new Inventory()
                .setAssetName(assetName)
                .setTotalCount(parsedCount)
                .setLastIssued(null)
                .setLastIssued(null)
        );

        if (model == null) {
            Announcements.get().showError("Failure", "Could not add inventory item.", "Please try again.");
            return;
        }

        inventoryTable.refresh();
        Announcements.get().showInfo("Success", "Inventory asset created.");
        builder.hide();
    }


    @FXML private void onDelete() {
        final InventoryModel model = inventoryTable.getSelectionModel().getSelectedItem();
        Announcements.get().showConfirmation(() -> deleteInventoryItem(model),
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
        Announcements.get().showInfo("Success", "Inventory Asset Deleted");
    }

    @FXML private void onViewLog() {
        final InventoryModel model = inventoryTable.getSelectionModel().getSelectedItem();
        final int assetId = model.getId();

        final ObservableList<InventoryLogModel> inventoryLogModels = inventoryLog.getObservableListByFilter(
                filter -> filter.getAssetId() == assetId);

        if (inventoryLogModels.isEmpty()) {
            Announcements.get().showError("Failure", "There are no transactions with this asset.", "Please distribute this item.");
            return;
        }

        final PopOverBuilder builder = new PopOverBuilder()
                .setTitle("Asset Transaction Log")
                .useDefaultSettings();

        builder.setContent(getLogContent(builder, inventoryLogModels));
        builder.show();
    }

    private VBox getLogContent(final PopOverBuilder builder, final ObservableList<InventoryLogModel> inventoryLogModels) {
        final VBox vBox = new VBox(5.0);
        vBox.setAlignment(Pos.TOP_CENTER);

        AtomicReference<Double> maxFlowWidth = new AtomicReference<>(0.0);
        for (final InventoryLogModel log : inventoryLogModels) {
            final int assetId = log.getAssetId();
            final InventoryModel item = inventory.getModel(assetId);
            final String assetName = (item == null) ? "Unknown" : item.getAssetName();

            final int employeeId = log.getEmployeeId();
            final EmployeeModel employeeModel = employee.getModel(employeeId);
            final String employeeName = (employeeModel == null) ? "Unknown" : employeeModel.getFullName();

            final TextFlow flow = new TextFlow();
            final Text assetText = new Text(assetName);
            assetText.setFill(Color.valueOf("#5DADD5"));

            final Text firstInfoText = new Text(String.format(" (Count: %s) ", log.getCountAtTime()));
            firstInfoText.setFill(Color.WHITE);

            final Text secondInfoText = new Text(" was given to ");
            secondInfoText.setFill(Color.WHITE);

            final Text thirdInfoText = new Text(employeeName);
            thirdInfoText.setFill(Color.valueOf("#5DADD5"));

            final Text fourthInfoText = new Text(" on ");
            fourthInfoText.setFill(Color.WHITE);

            final Text fifthInfoText = new Text(log.getIssuedDate());
            fifthInfoText.setFill(Color.valueOf("#5DADD5"));


            flow.setTextAlignment(TextAlignment.LEFT);
            flow.setPadding(new Insets(0, 10, 0, 10));
            flow.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.getWidth() > maxFlowWidth.get()) {
                    maxFlowWidth.set(newValue.getWidth());
                    vBox.setPrefWidth(maxFlowWidth.get());
                }
            });
            flow.getChildren().addAll(assetText, firstInfoText, secondInfoText, thirdInfoText, fourthInfoText, fifthInfoText);
            vBox.getChildren().add(flow);

        }

        return vBox;
    }
}


