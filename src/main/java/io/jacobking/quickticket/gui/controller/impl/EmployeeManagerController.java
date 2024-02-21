package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.gui.alert.Notify;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.EmployeeModel;
import io.jacobking.quickticket.gui.utility.StyleCommons;
import io.jacobking.quickticket.tables.pojos.Employee;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeManagerController extends Controller {

    @FXML private ListView<EmployeeModel> employeeList;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField titleField;
    @FXML private TextField departmentField;
    @FXML private TextField emailField;

    @FXML private Button createButton;

    @FXML private Button deleteButton;

    @FXML private Button updateButton;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureEmployeeList();
        configureButtons();
    }

    @FXML private void onCreate() {
        final EmployeeModel selected = employeeList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Notify.showError("Failed to create employee.", "An employee is currently selected.", "You must deselect the employee first.");
            clearFields();
            return;
        }

        employee.createModel(new Employee().setFirstName(firstNameField.getText()).setLastName(lastNameField.getText()).setEmail(emailField.getText()).setDepartment(departmentField.getText()).setTitle(titleField.getText()));

        clearFields();
    }

    @FXML private void onDelete() {
        final EmployeeModel selected = employeeList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Notify.showError("Failed to delete.", "No employee was deleted.", "You must select an employee first.");
            return;
        }
        employee.remove(selected.getId());
        clearFields();
    }

    @FXML private void onUpdate() {
        final EmployeeModel model = employeeList.getSelectionModel().getSelectedItem();

        model.setFirstName(firstNameField.getText());
        model.setLastName(lastNameField.getText());
        model.setEmail(emailField.getText());
        model.setDepartmentProperty(departmentField.getText());
        model.setTitle(titleField.getText());

        employee.update(model);
        employeeList.refresh();
    }

    @FXML private void onClear() {
        clearFields();
    }

    private void configureButtons() {
        createButton.disableProperty().bind(firstNameField.textProperty().isEmpty().or(lastNameField.textProperty().isEmpty()).or(employeeList.getSelectionModel().selectedItemProperty().isNotNull()));

        deleteButton.disableProperty().bind(employeeList.getSelectionModel().selectedItemProperty().isNull());
        updateButton.disableProperty().bind(employeeList.getSelectionModel().selectedItemProperty().isNull());
    }

    private void configureEmployeeList() {
        employeeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        employeeList.setItems(employee.getObservableList());
        employeeList.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(EmployeeModel employeeModel, boolean b) {
                super.updateItem(employeeModel, b);
                if (employeeModel == null || b) {
                    setGraphic(null);
                    return;
                }

                setAlignment(Pos.CENTER);
                final Label label = new Label(employeeModel.getFullName());
                label.setStyle(StyleCommons.COMMON_LABEL_STYLE_TWO);
                setGraphic(label);
            }
        });

        employeeList.getSelectionModel().selectedItemProperty().addListener(((observableValue, employeeModel, t1) -> {
            if (t1 == null) {
                clearFields();
                return;
            }
            populateFields(t1);
        }));
    }

    private void populateFields(final EmployeeModel model) {
        if (model == null) {
            return;
        }

        firstNameField.setText(model.getFirstName());
        lastNameField.setText(model.getLastName());
        emailField.setText(model.getEmail());
        departmentField.setText(model.getDepartment());
        titleField.setText(model.getTitle());
    }

    private void clearFields() {
        employeeList.getSelectionModel().clearSelection();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        departmentField.clear();
        titleField.clear();
    }
}
