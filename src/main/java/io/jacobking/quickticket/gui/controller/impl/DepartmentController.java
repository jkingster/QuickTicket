package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.gui.alert.Alerts;
import io.jacobking.quickticket.gui.alert.Notifications;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.CompanyModel;
import io.jacobking.quickticket.gui.model.impl.DepartmentModel;
import io.jacobking.quickticket.tables.pojos.Department;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentController extends Controller {

    @FXML private CheckBox newDepartmentCheckBox;

    @FXML private SearchableComboBox<CompanyModel>    companySearchBox;
    @FXML private SearchableComboBox<DepartmentModel> departmentSearchBox;

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextArea  descriptionArea;

    @FXML private Button createButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;


    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureNewDepartmentCheckBox();
        configureCompanySearchBox();
        configureDepartmentSearchBox();
        configureButtons();
    }

    @FXML private void onCreate() {
        final String name = nameField.getText();
        if (name.isEmpty()) {
            Alerts.showError(
                    "Failed!",
                    "Could not create department.",
                    "Department name is required!"
            );
            return;
        }

        final DepartmentModel newDepartment = department.createModel(getNewDepartment());
        if (newDepartment != null) {
            Notifications.showInfo("Success!", "Department has been created/");
        }
    }

    @FXML private void onUpdate() {
        final DepartmentModel departmentModel = departmentSearchBox.getSelectionModel().getSelectedItem();
        if (departmentModel == null) {
            Alerts.showError(
                    "Failure!",
                    "Could not update department.",
                    "No department selected!"
            );
            return;
        }

        departmentModel.setName(nameField.getText());
        departmentModel.setEmail(emailField.getText());
        departmentModel.setDescription(descriptionArea.getText());
        departmentModel.setCompanyId(getCompanyId());

        if (department.update(departmentModel)) {
            Notifications.showInfo("Success!", "Department updated successfully.");
        }
    }

    @FXML private void onDelete() {
        final DepartmentModel departmentModel = departmentSearchBox.getSelectionModel().getSelectedItem();
        if (departmentModel == null) {
            Alerts.showError(
                    "Failure!",
                    "Could not delete department.",
                    "No department selected!"
            );
            return;
        }

        Alerts.showConfirmation(() -> deleteDepartment(departmentModel),
                "Are you sure you want to do this?",
                "This department cannot be recovered if deleted."
        ).ifPresent(type -> {
            if (type == ButtonType.YES) {
                deleteDepartment(departmentModel);
            }
        });
    }

    private void deleteDepartment(final DepartmentModel departmentModel) {
        department.remove(departmentModel.getId());
        Notifications.showWarning("Success", "Department has been deleted.");
    }

    @FXML private void onReset() {
        clearFields();
    }

    private void configureNewDepartmentCheckBox() {
        departmentSearchBox.disableProperty().bind(newDepartmentCheckBox.selectedProperty());

        newDepartmentCheckBox.selectedProperty().addListener(((observableValue, aBoolean, t1) -> {
            if (t1 != null && t1) {
                departmentSearchBox.getSelectionModel().clearSelection();
                nameField.clear();
                descriptionArea.clear();
                emailField.clear();
            }
        }));
    }

    private void configureCompanySearchBox() {
        companySearchBox.setConverter(new StringConverter<>() {
            @Override public String toString(CompanyModel companyModel) {
                return companyModel != null ? companyModel.getName() : "";
            }

            @Override public CompanyModel fromString(String s) {
                return null;
            }
        });
        companySearchBox.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(CompanyModel companyModel, boolean b) {
                super.updateItem(companyModel, b);
                if (b || companyModel == null) {
                    setText(null);
                    return;
                }
                setText(String.format("ID: %s | %s", companyModel.getId(), companyModel.getName()));
            }
        });
        companySearchBox.getSelectionModel().selectedItemProperty().addListener(((observableValue, companyModel, t1) -> {
            if (t1 == null)
                return;

            final int companyId = t1.getId();
            if (companyId == 0) {
                departmentSearchBox.setItems(department.getObservableList()
                        .filtered(dm -> dm.getId() != 0)
                );
            } else if (companyId > 0) {
                departmentSearchBox.setItems(department.getObservableListByFilter(
                        dm -> dm.getCompanyId() == companyId
                ));
            }
        }));
        companySearchBox.setItems(company.getObservableList());
    }

    private void configureDepartmentSearchBox() {
        departmentSearchBox.setConverter(new StringConverter<>() {
            @Override public String toString(DepartmentModel departmentModel) {
                return departmentModel != null ? departmentModel.getName() : "";
            }

            @Override public DepartmentModel fromString(String s) {
                return null;
            }
        });

        departmentSearchBox.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(DepartmentModel departmentModel, boolean b) {
                super.updateItem(departmentModel, b);
                if (b || departmentModel == null) {
                    setText(null);
                    return;
                }
                setText(String.format("ID: %s | %s", departmentModel.getId(), departmentModel.getName()));
            }
        });

        departmentSearchBox.getSelectionModel().selectedItemProperty().addListener(((observableValue, departmentModel, t1) -> {
            if (t1 == null) {
                return;
            }
            if (newDepartmentCheckBox.isSelected())
                return;

            populateFields(t1);
        }));
    }

    private void configureButtons() {
        final BooleanBinding firstConditionCheck = Bindings.and(newDepartmentCheckBox.selectedProperty(),
                companySearchBox.getSelectionModel().selectedItemProperty().isNotNull()).not();

        final BooleanBinding secondConditionCheck = Bindings.or(
                companySearchBox.getSelectionModel().selectedItemProperty().isNotNull(),
                departmentSearchBox.getSelectionModel().selectedItemProperty().isNotNull()
        ).not();

        createButton.disableProperty().bind(firstConditionCheck.or(secondConditionCheck));

        deleteButton.disableProperty().bind(createButton.disabledProperty().not());
        updateButton.disableProperty().bind(createButton.disabledProperty().not());
    }

    private void populateFields(final DepartmentModel departmentModel) {
        nameField.setText(departmentModel.getName());
        emailField.setText(departmentModel.getEmail());
        descriptionArea.setText(departmentModel.getDescription());
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        descriptionArea.clear();
        departmentSearchBox.getSelectionModel().clearSelection();
        companySearchBox.getSelectionModel().clearSelection();
    }

    private int getCompanyId() {
        return companySearchBox.getSelectionModel()
                .getSelectedItem()
                .getId();
    }

    private Department getNewDepartment() {
        return new Department()
                .setName(nameField.getText())
                .setEmail(emailField.getText())
                .setDescription(descriptionArea.getText())
                .setCompanyId(getCompanyId());
    }
}
