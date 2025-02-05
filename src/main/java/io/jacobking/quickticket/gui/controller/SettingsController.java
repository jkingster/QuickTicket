package io.jacobking.quickticket.gui.controller;


import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.Data;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.misc.PopOverBuilder;
import io.jacobking.quickticket.gui.model.AlertModel;
import io.jacobking.quickticket.gui.model.ModuleModel;
import io.jacobking.quickticket.gui.model.TicketCategoryModel;
import io.jacobking.quickticket.gui.utility.FXUtility;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.controlsfx.control.SearchableComboBox;

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
        configureCategories();
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

    @FXML private TitledPane                              categoryPane;
    @FXML private SearchableComboBox<TicketCategoryModel> categoryBox;
    @FXML private TextField                               categoryName;
    @FXML private Pane                                    colorPane;
    @FXML private Button                                  saveDefaultCategoryButton;
    @FXML private TextArea                                categoryDescription;
    @FXML private Button                                  deleteCategoryButton;
    @FXML private Button                                  updateCategoryButton;
    @FXML private Button                                  newCategoryButton;
    @FXML private Button                                  clearCategoryButton;
    private final StringProperty                          colorCode = new SimpleStringProperty();

    private void configureCategories() {
        configureCategoryBox();
        configureCategoryButtons();
        configureColorPane();
    }

    private void configureCategoryBox() {
        categoryBox.setItems(bridgeContext.getCategory().getObservableList());
        categoryBox.getSelectionModel().selectedItemProperty().addListener((obs, oldCategory, newCategory) -> {
            if (newCategory == null) {
                FXUtility.resetFields(categoryBox);
                colorCode.setValue("");
                return;
            }
            final String rgbColor = TicketCategoryModel.getColorAsRGB(newCategory.getColorProperty());

            categoryName.setText(newCategory.getNameProperty());
            categoryDescription.setText(newCategory.getDescriptionProperty());
            colorPane.setStyle(String.format("-fx-background-color: %s; -fx-background-border: 2px;", rgbColor));
            colorCode.setValue(rgbColor);
        });


    }

    private void configureCategoryButtons() {
        deleteCategoryButton.disableProperty().bind(categoryBox.getSelectionModel().selectedItemProperty().isNull());
        updateCategoryButton.disableProperty().bind(deleteCategoryButton.disabledProperty());
        newCategoryButton.disableProperty().bind(categoryBox.getSelectionModel().selectedItemProperty().isNotNull());
        newCategoryButton.setOnAction(event -> display.show(Route.CATEGORY_CREATOR, Data.of(categoryBox)));

        updateCategoryButton.setOnAction(event -> onUpdateCategory());
        deleteCategoryButton.setOnAction(event -> onDeleteCategory());
        clearCategoryButton.setOnAction(event -> onClearCategory());
    }

    private void onUpdateCategory() {
        final String newName = categoryName.getText();
        final String desc = categoryDescription.getText();
        final String color = colorCode.getValueSafe();

        final TicketCategoryModel model = categoryBox.getSelectionModel().getSelectedItem();
        if (model == null) {
            Announcements.get().showError("Error", "Could not update category.", "No selected category.");
            return;
        }

        final String rgbColor = TicketCategoryModel.getColorAsRGB(color);
        model.setColorProperty(rgbColor);
        model.setNameProperty(newName);
        model.setDescriptionProperty(desc);

        final boolean updated = bridgeContext.getCategory().update(model);
        if (!updated) {
            Announcements.get().showError("Error", "Could not update category.", "Update failed.");
            return;
        }

        colorPane.setStyle(String.format("-fx-background-color: %s", rgbColor));
        Announcements.get().showConfirm("Success", "Category Updated");
    }

    private void onDeleteCategory() {
        final TicketCategoryModel selected = categoryBox.getSelectionModel().getSelectedItem();
        if (selected.getId() == 0) {
            Announcements.get().showError("Error", "You cannot delete the default category.", "Ignoring request.");
            return;
        }

        Announcements.get().showConfirmation(() -> processCategoryDeletion(selected), "Deletion Confirmation", "Are you sure you want to delete this category?")
                .ifPresent(type -> {
                    if (type == ButtonType.YES) {
                        processCategoryDeletion(selected);
                    }
                });
    }

    private void processCategoryDeletion(final TicketCategoryModel model) {
        final boolean deleted = bridgeContext.getCategory().remove(model.getId());
        if (!deleted) {
            Announcements.get().showError("Error", "Could not delete category.", "Unknown.");
            return;
        }
        Announcements.get().showConfirm("Success", "Category deleted");

        bridgeContext.getTicket().getObservableList().forEach(ticket -> {
            final int categoryId = ticket.getCategory();
            if (categoryId == model.getId()) {
                ticket.categoryProperty().setValue(0);
                bridgeContext.getTicket().update(ticket);
            }
        });

        onClearCategory();
    }

    private void configureColorPane() {
        colorPane.setOnMousePressed(event -> {
            PopOverBuilder.build()
                    .setTitle("Category Color Picker")
                    .setDetached(true)
                    .setDetachable(true)
                    .setAnimated(true)
                    .setHideOnEscape(true)
                    .process(process -> {
                        final ColorPicker colorPicker = new ColorPicker();
                        colorPicker.setValue(Color.web(colorCode.getValueSafe()));

                        colorPicker.setOnAction(__ -> {
                            colorCode.setValue(String.valueOf(colorPicker.getValue()));
                        });
                        return colorPicker;
                    }).show(colorPane, 10);
        });
    }

    @FXML private void onClearCategory() {
        categoryBox.getSelectionModel().clearSelection();
        categoryName.clear();
        categoryDescription.clear();
        colorPane.setStyle("-fx-background-color: none;");
    }
}
