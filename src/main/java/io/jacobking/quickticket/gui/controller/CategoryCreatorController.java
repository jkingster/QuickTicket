package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.model.TicketCategoryModel;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.tables.pojos.TicketCategories;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoryCreatorController extends Controller {

    private TicketCategoryModel model;

    @FXML private TextField   nameField;
    @FXML private TextField   descriptionField;
    @FXML private ColorPicker colorPicker;
    @FXML private Button      createButton;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        data.mapFirst(TicketCategoryModel.class).ifPresent(target -> {
            this.model = target;
            initializeFields(model);
            createButton.setText("Update");

            createButton.setOnAction(event -> updateModel(model));
        });

        createButton.disableProperty().bind(nameField.textProperty().isEmpty());
    }

    @FXML
    private void onCreate() {
        final String description = descriptionField.getText().isEmpty() ? "" : descriptionField.getText();
        final TicketCategories ticketCategories = new TicketCategories()
                .setColor(TicketCategoryModel.getColorAsRGB(colorPicker.getValue().toString()))
                .setName(nameField.getText())
                .setDescription(description);

        final TicketCategoryModel newModel = bridgeContext.getCategory().createModel(ticketCategories);
        if (newModel == null) {
            Announcements.get().showError("Failed", "Could not create new category.", "Please try again.");
            return;
        }

        Announcements.get().showInfo("Category created successfully.", "Category name: " + nameField.getText());
        display.close(Route.CATEGORY_CREATOR);
    }

    private void initializeFields(final TicketCategoryModel model) {
        nameField.setText(model.getNameProperty());
        descriptionField.setText(model.getDescriptionProperty());
        colorPicker.setValue(Color.valueOf(model.getColorProperty()));
    }

    @FXML private void onReset() {
        nameField.clear();
        descriptionField.clear();
        colorPicker.setValue(Color.WHITE);
    }

    private void updateModel(final TicketCategoryModel model) {
        model.setNameProperty(nameField.getText());
        model.setColorProperty(colorPicker.getValue().toString());

        final String description = descriptionField.getText().isEmpty() ? "" : descriptionField.getText();
        model.setDescriptionProperty(description);

        if (!bridgeContext.getCategory().update(model)) {
            Announcements.get().showError("Failed", "Could not update category.", "Please try again.");
            return;
        }

        Announcements.get().showInfo("Success", "Category was successfully updated.");
        display.close(Route.CATEGORY_CREATOR);
    }
}
