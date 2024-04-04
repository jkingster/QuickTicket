package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.gui.alert.Alerts;
import io.jacobking.quickticket.gui.alert.Notifications;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.CompanyModel;
import io.jacobking.quickticket.tables.pojos.Company;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CompanyController extends Controller {

    @FXML private SearchableComboBox<CompanyModel> companyComboBox;

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private TextField stateField;
    @FXML private TextField zipCodeField;
    @FXML private TextField countryField;

    @FXML private TextArea descriptionArea;

    @FXML private Button createButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureCompanyComboBox();
        configureButtonBindings();
        configureZipCodeField();
    }

    @FXML private void onCreate() {
        final String companyName = nameField.getText();
        if (companyName.isEmpty()) {
            Alerts.showError("Failed!", "Could not create company.", "Company name required!");
            return;
        }

        final Company newCompany = getCompany();
        final CompanyModel companyModel = company.createModel(newCompany);
        if (companyModel != null) {
            Notifications.showInfo("Creation Successful", "Your new company has been created.");
        }
    }

    @FXML private void onUpdate() {
        final CompanyModel companyModel = companyComboBox.getSelectionModel().getSelectedItem();
        if (companyModel == null)
            return;

        companyModel.setName(nameField.getText());
        companyModel.setEmail(emailField.getText());
        companyModel.setDescription(descriptionArea.getText());
        companyModel.setStreet(addressField.getText());
        companyModel.setState(stateField.getText());
        companyModel.setCountry(countryField.getText());
        companyModel.setZipCode(getZipCode());

        if (company.update(companyModel)) {
            Notifications.showInfo("Update Successful", "Company model was updated.");
        }
    }

    @FXML private void onDelete() {
        final CompanyModel companyModel = companyComboBox.getSelectionModel().getSelectedItem();
        if (companyModel == null)
            return;

        Alerts.showConfirmation(
                this::deleteCompany,
                "Are you sure you want to delete this company?",
                "This process cannot be undone."
        ).ifPresent(type -> {
            if (type == ButtonType.YES) {
                deleteCompany();
            }
        });
    }

    private void deleteCompany() {
        final CompanyModel companyModel = companyComboBox.getSelectionModel().getSelectedItem();
        if (companyModel == null)
            return;

        final int companyId = companyModel.getId();
        if (companyId == 0) {
            Notifications.showError("Could not delete company.", "This company is locked by internals!");
            return;
        }
        company.remove(companyId);
    }

    @FXML private void onReset() {
        nameField.clear();
        emailField.clear();
        addressField.clear();
        stateField.clear();
        zipCodeField.clear();
        countryField.clear();
        descriptionArea.clear();
        companyComboBox.getSelectionModel().clearSelection();
    }

    private void configureCompanyComboBox() {
        companyComboBox.setItems(company.getObservableListByFilter(cm -> cm.getId() != 0));
        companyComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(CompanyModel companyModel) {
                if (companyModel == null)
                    return "";
                return companyModel.getName();
            }

            @Override public CompanyModel fromString(String s) {
                return null;
            }
        });

        companyComboBox.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(CompanyModel companyModel, boolean b) {
                super.updateItem(companyModel, b);
                if (b || companyModel == null || companyModel.getId() == 0) {
                    setText(null);
                    return;
                }
                setText(String.format("ID: %s | %s", companyModel.getId(), companyModel.getName()));
            }
        });

        companyComboBox.getSelectionModel().selectedItemProperty().addListener(((observableValue, companyModel, t1) -> {
            if (t1 == null) {
                onReset();
                return;
            }
            populateFields(t1);
        }));
    }

    private void configureButtonBindings() {
        createButton.disableProperty().bind(companyComboBox.getSelectionModel().selectedItemProperty().isNotNull());

        updateButton.disableProperty().bind(createButton.disabledProperty().not());
        deleteButton.disableProperty().bind(createButton.disabledProperty().not());
    }

    private void populateFields(final CompanyModel companyModel) {
        nameField.setText(companyModel.getName());
        emailField.setText(companyModel.getEmail());
        addressField.setText(companyModel.getStreet());
        stateField.setText(companyModel.getState());
        zipCodeField.setText(String.valueOf(companyModel.getZipCode()));
        countryField.setText(companyModel.getCountry());
        descriptionArea.setText(companyModel.getDescription());
    }

    private void configureZipCodeField() {
        zipCodeField.textProperty().addListener(((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                zipCodeField.setText(t1.replaceAll("\\D", ""));
            }
        }));
    }

    private Company getCompany() {
        return new Company()
                .setName(nameField.getText())
                .setEmail(emailField.getText())
                .setDescription(descriptionArea.getText())
                .setStreet(addressField.getText())
                .setState(stateField.getText())
                .setCountry(countryField.getText())
                .setZipcode(getZipCode());
    }

    private int getZipCode() {
        final String zipCode = zipCodeField.getText();
        return ((zipCode == null || zipCode.isEmpty()))
               ? -1 : Integer.parseInt(zipCode);
    }
}
