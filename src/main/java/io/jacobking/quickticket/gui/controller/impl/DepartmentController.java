package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.gui.alert.Alerts;
import io.jacobking.quickticket.gui.alert.Notifications;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.CompanyModel;
import io.jacobking.quickticket.gui.model.impl.DepartmentModel;
import io.jacobking.quickticket.tables.pojos.Department;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentController extends Controller {

    @FXML private SearchableComboBox<CompanyModel>    companyComboBox;
    @FXML private SearchableComboBox<DepartmentModel> departmentComboBox;

    @FXML private CheckBox noCompanyCheckBox;

    @FXML private TextField nameField;
    @FXML private TextField emailField;

    @FXML private TextArea descriptionArea;

    @FXML private Button createButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureCompanyComboBox();
        configureDepartmentComboBox();
        configureNoCompanyCheckBox();
        configureButtons();
    }

    @FXML private void onCreate() {
        final String departmentName = nameField.getText();
        if (departmentName.isEmpty()) {
            Alerts.showError("Failed!", "Could not create department.", "A value in the name field is required!");
            return;
        }

        final Department newDepartment = new Department()
                .setName(departmentName)
                .setEmail(emailField.getText())
                .setDescription(descriptionArea.getText())
                .setCompanyId(getCompanyId());

        final DepartmentModel departmentModel = department.createModel(newDepartment);
        if (departmentModel != null) {
            Notifications.showInfo("Success!", "Department created successfully!");
            clearFields();
        }
    }

    @FXML private void onUpdate() {
        final DepartmentModel departmentModel = departmentComboBox.getSelectionModel().getSelectedItem();
        if (departmentModel == null)
            return;

        departmentModel.setCompanyId(getCompanyId());
        departmentModel.setDescription(descriptionArea.getText());
        departmentModel.setName(nameField.getText());
        departmentModel.setEmail(emailField.getText());

        if (department.update(departmentModel)) {
            Notifications.showInfo("Update Successful!", "This department model was updated.");
        }
    }

    @FXML private void onDelete() {
        final DepartmentModel departmentModel = departmentComboBox.getSelectionModel().getSelectedItem();
        if (departmentModel == null)
            return;

        Alerts.showConfirmation(() -> deleteDepartment(departmentModel),
                "Are you sure you want to delete this department?",
                "This action cannot be undone!").ifPresent(type -> {
            if (type == ButtonType.YES) {
                deleteDepartment(departmentModel);
            }
        });
    }

    private void deleteDepartment(final DepartmentModel departmentModel) {
        department.remove(departmentModel.getId());
        clearFields();
    }

    private void configureCompanyComboBox() {
        companyComboBox.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(CompanyModel companyModel, boolean b) {
                super.updateItem(companyModel, b);
                if (b || companyModel == null) {
                    setText(null);
                    return;
                }

                setText(companyModel.getName());
            }
        });

        companyComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(CompanyModel companyModel) {
                return (companyModel == null) ? "" : companyModel.getName();
            }

            @Override public CompanyModel fromString(String s) {
                return null;
            }
        });

        companyComboBox.setItems(company.getObservableList());

        companyComboBox.getSelectionModel().selectedItemProperty()
                .addListener(((observableValue, companyModel, t1) -> {
                    if (t1 == null || t1.getId() == 0) {
                        departmentComboBox.setItems(department.getObservableList());
                        return;
                    }

                    final int companyId = t1.getId();
                    departmentComboBox.setItems(department.getObservableListByFilter((
                            departmentModel -> departmentModel.getCompanyId() == companyId
                    )));
                }));
    }

    private void configureDepartmentComboBox() {
        departmentComboBox.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(DepartmentModel departmentModel, boolean b) {
                super.updateItem(departmentModel, b);
                if (b || departmentModel == null) {
                    setGraphic(null);
                    return;
                }
                setText(String.format("ID: %s | %s", departmentModel.getId(), departmentModel.getName()));
            }
        });

        departmentComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(DepartmentModel departmentModel) {
                return (departmentModel == null) ? "" : departmentModel.getName();
            }

            @Override public DepartmentModel fromString(String s) {
                return null;
            }
        });

        departmentComboBox.setItems(department.getObservableListByFilter(dm -> dm.getId() != 0));
        departmentComboBox.getSelectionModel().selectedItemProperty()
                .addListener(((observableValue, departmentModel, t1) -> {
                    if (t1 == null) {
                        clearFields();
                        return;
                    }
                    populateFields(t1);
                }));
    }

    private void configureButtons() {
        createButton.disableProperty().bind(departmentComboBox.getSelectionModel().selectedItemProperty().isNotNull());

        updateButton.disableProperty().bind(createButton.disabledProperty().not());
        deleteButton.disableProperty().bind(createButton.disabledProperty().not());
    }

    private void configureNoCompanyCheckBox() {
        noCompanyCheckBox.selectedProperty().addListener(((observableValue, aBoolean, t1) -> {
            companyComboBox.disableProperty().bind(observableValue);

            if (t1) {
                departmentComboBox.setItems(department.getObservableListByFilter(dm -> dm.getId() != 0));
            }
        }));
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        descriptionArea.clear();
        departmentComboBox.getSelectionModel().clearSelection();
        companyComboBox.getSelectionModel().clearSelection();
    }

    private void populateFields(final DepartmentModel departmentModel) {
        nameField.setText(departmentModel.getName());
        emailField.setText(departmentModel.getEmail());
        descriptionArea.setText(departmentModel.getDescription());
    }

    private int getCompanyId() {
        final CompanyModel companyModel = companyComboBox.getSelectionModel().getSelectedItem();
        return companyModel == null ? 0 : companyModel.getId();
    }

    @FXML private void onReset() {
        clearFields();
    }
}
