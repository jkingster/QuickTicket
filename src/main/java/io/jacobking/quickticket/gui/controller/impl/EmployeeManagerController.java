package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.gui.alert.Notify;
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
    @FXML private Button                  createButton;
    @FXML private Button                  deleteButton;
    @FXML private Button                  updateButton;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureEmployeeList();
        configureButtons();
        configureTicketListView();
    }

    @FXML private void onCreate() {
        final EmployeeModel selected = employeeList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Notify.showError("Failed to create employee.", "An employee is currently selected.", "You must deselect the employee first.");
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
                hBox.setSpacing(25.0);

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
