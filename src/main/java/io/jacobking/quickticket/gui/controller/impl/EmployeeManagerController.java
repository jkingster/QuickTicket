package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.gui.alert.Alerts;
import io.jacobking.quickticket.gui.alert.Notifications;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.data.DataRelay;
import io.jacobking.quickticket.gui.model.impl.EmployeeModel;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.utility.FALoader;
import io.jacobking.quickticket.gui.utility.StyleCommons;
import io.jacobking.quickticket.tables.pojos.Employee;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.controlsfx.glyphfont.FontAwesome;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeManagerController extends Controller {

    @FXML private ListView<EmployeeModel> employeeList;
    @FXML private ListView<TicketModel>   ticketListView;
    @FXML private TextField               firstNameField;
    @FXML private TextField               lastNameField;
    @FXML private TextField               titleField;
    @FXML private TextField               departmentField;
    @FXML private TextField               emailField;
    @FXML private TextField               searchField;
    @FXML private Button                  createButton;
    @FXML private Button                  deleteButton;
    @FXML private Button                  updateButton;
    @FXML private Button                  searchButton;


    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureEmployeeList();
        configureButtons();
        configureTicketListView();
    }

    @FXML private void onCreate() {
        final EmployeeModel selected = employeeList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alerts.showError("Failed to create employee.", "An employee is currently selected.", "You must deselect the employee first.");
            clearFields();
            return;
        }

        employee.createModel(new Employee()
                .setFirstName(firstNameField.getText())
                .setLastName(lastNameField.getText())
                .setEmail(emailField.getText())
                .setDepartment(departmentField.getText())
                .setTitle(titleField.getText()));
        clearFields();
    }

    @FXML private void onDelete() {
        final EmployeeModel selected = employeeList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alerts.showError("Failed to delete.", "No employee was deleted.", "You must select an employee first.");
            return;
        }

        Alerts.showConfirmation(() -> employee.remove(selected.getId()), "Are you sure you want to delete this employee?", "This action cannot be undone.")
                .ifPresent(type -> {
                    if (type == ButtonType.YES) {
                        employee.remove(selected.getId());
                        clearFields();
                    }
                });

    }

    @FXML private void onUpdate() {
        final EmployeeModel model = employeeList.getSelectionModel().getSelectedItem();

        model.setFirstName(firstNameField.getText());
        model.setLastName(lastNameField.getText());
        model.setEmail(emailField.getText());
        model.setDepartmentProperty(departmentField.getText());
        model.setTitle(titleField.getText());

        if (employee.update(model)) {
            Notifications.showInfo("Update", "Employee updated successfully!");
        }
        employeeList.refresh();
    }

    @FXML private void onClear() {
        clearFields();
    }

    @FXML private void onSearch() {
        final EmployeeModel employee = findEmployee(searchField.getText());
        if (employee == null) {
            Alerts.showError(
                    "Error",
                    "Could not find an employee record.",
                    "Please try another search query."
            );
            return;
        }
        selectEmployee(employee);
    }

    private EmployeeModel findEmployee(final String searchQuery) {
        for (final EmployeeModel model : employeeList.getItems()) {
            final String email = model.getEmail();
            final String fullName = model.getFullName();
            final String title = model.getTitle();

            if (containsIgnoreCase(email, searchQuery)
                    || containsIgnoreCase(fullName, searchQuery)
                    || containsIgnoreCase(title, searchQuery)
            ) {
                return model;
            }
        }
        return null;
    }

    private void selectEmployee(final EmployeeModel employee) {
        employeeList.getSelectionModel().select(employee);
        employeeList.scrollTo(employee);
        populateFields(employee);
    }

    // https://stackoverflow.com/questions/14018478/string-contains-ignore-case
    private boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }

    private void configureButtons() {
        createButton.disableProperty().bind(firstNameField.textProperty().isEmpty().or(lastNameField.textProperty().isEmpty()).or(employeeList.getSelectionModel().selectedItemProperty().isNotNull()));

        deleteButton.disableProperty().bind(employeeList.getSelectionModel().selectedItemProperty().isNull());
        updateButton.disableProperty().bind(employeeList.getSelectionModel().selectedItemProperty().isNull());

        searchButton.disableProperty().bind(searchField.textProperty().isEmpty());
        searchButton.setGraphic(FALoader.createDefault(FontAwesome.Glyph.SEARCH));
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
        ticketListView.setItems(ticket.getFilteredList(target -> target.getEmployeeId() == model.getId()));
    }

    private void configureTicketListView() {
        ticketListView.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(TicketModel ticketModel, boolean b) {
                super.updateItem(ticketModel, b);
                if (b || ticketModel == null) {
                    setGraphic(null);
                    return;
                }

                final HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setSpacing(15.0);

                final Button view = new Button();
                view.setOnAction(event -> openTicket(ticketModel));
                view.setGraphic(FALoader.createDefault(FontAwesome.Glyph.TICKET));
                hBox.getChildren().add(view);

                final HBox hBoxTwo = new HBox();
                hBoxTwo.setAlignment(Pos.CENTER_RIGHT);
                final Label info = new Label(String.format("%s | Ticket ID: %s", ticketModel.getTitle(), ticketModel.getId()));
                info.setStyle("-fx-font-weight: bolder; -fx-text-fill: white");
                hBoxTwo.getChildren().add(info);

                hBox.getChildren().add(hBoxTwo);

                setGraphic(hBox);
            }
        });
    }

    private void openTicket(final TicketModel ticketModel) {
        Display.close(Route.EMPLOYEE_MANAGER);
        Display.show(Route.VIEWER, DataRelay.of(ticketModel));
    }

    private void clearFields() {
        employeeList.getSelectionModel().clearSelection();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        departmentField.clear();
        titleField.clear();
        ticketListView.setItems(null);
    }
}
