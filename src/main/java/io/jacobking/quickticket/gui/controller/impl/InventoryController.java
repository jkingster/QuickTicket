package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.alert.Alerts;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.misc.PopOverBuilder;
import io.jacobking.quickticket.gui.model.impl.EmployeeModel;
import io.jacobking.quickticket.gui.model.impl.InventoryModel;
import io.jacobking.quickticket.gui.utility.FALoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTable();
        configurePieChart();
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
            Alerts.showError("Failed", "Could not increase asset count.", "Please try again.");
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

        builder.setContent(getContent(builder, inventoryModel));
        builder.show();
    }

    private HBox getContent(final PopOverBuilder builder, final InventoryModel model) {
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
                Alerts.showError("Failed", "Could not decrease asset count.", "Please try again.");
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
    }
}
