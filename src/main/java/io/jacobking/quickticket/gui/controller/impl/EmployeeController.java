package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.alert.Alerts;
import io.jacobking.quickticket.gui.alert.Notifications;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.data.DataRelay;
import io.jacobking.quickticket.gui.model.impl.EmployeeModel;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.utility.FALoader;
import io.jacobking.quickticket.tables.pojos.Employee;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.glyphfont.FontAwesome;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class EmployeeController extends Controller {

    @FXML private SearchableComboBox<EmployeeModel> employeeComboBox;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField titleField;
    @FXML private TextField emailField;
    @FXML private TextField workPhoneField;
    @FXML private TextField workPhoneExtensionField;
    @FXML private TextField mobilePhoneField;

    @FXML private Button emailButton;
    @FXML private Button createButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button updateInfoButton;

    @FXML private TextArea infoTextArea;

    @FXML private TableView<TicketModel>           ticketTable;
    @FXML private TableColumn<TicketModel, Void>   actionsColumn;
    @FXML private TableColumn<TicketModel, String> ticketIdColumn;
    @FXML private TableColumn<TicketModel, String>        titleColumn;
    @FXML private TableColumn<TicketModel, LocalDateTime> createdOnColumn;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureEmployeeComboBox();
        configureWorkExtensionField();
        configureTicketTable();
        configureButtons();
    }

    @FXML private void onCreate() {
        final String firstName = firstNameField.getText();
        final String lastName = lastNameField.getText();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Alerts.showError("Failed to create employee.", "You must provide a first and last name.", "Try again!");
            return;
        }

        final EmployeeModel model = employee.createModel(new Employee()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(emailField.getText())
                .setMobilePhone(mobilePhoneField.getText())
                .setWorkPhone(workPhoneField.getText())
                .setTitle(titleField.getText())
                .setWorkExtension(getExtension())
                .setCompanyId(-1)
                .setDepartmentId(-1)
        );

        if (model != null) {
            Notifications.showInfo("Employee Created Successfully", "Employee list updated.");
            onReset();
        }
    }

    @FXML private void onDelete() {
        Alerts.showConfirmation(this::deleteEmployee, "Are you sure you want to delete this employee?", "This process cannot be undone.")
                .ifPresent(type -> {
                    if (type == ButtonType.YES) {
                        deleteEmployee();
                    }
                });
    }

    private void deleteEmployee() {
        final EmployeeModel model = employeeComboBox.getSelectionModel().getSelectedItem();
        if (model == null)
            return;

        employee.remove(model.getId());
        Notifications.showWarning("Employee Deleted", "Employee list updated.");
        clearFields();
    }

    private void configureEmployeeComboBox() {
        employeeComboBox.setItems(employee.getObservableList());
        employeeComboBox.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(EmployeeModel employeeModel, boolean b) {
                super.updateItem(employeeModel, b);
                if (b || employeeModel == null) {
                    setText(null);
                    return;
                }
                setText(employeeModel.getFullName());
            }
        });

        employeeComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(EmployeeModel employeeModel) {
                if (employeeModel == null)
                    return "";
                return employeeModel.getFullName();
            }

            @Override public EmployeeModel fromString(String s) {
                return null;
            }
        });

        employeeComboBox.getSelectionModel().selectedItemProperty().
                addListener(((observableValue, employeeModel, t1) -> {
                    if (t1 == null) {
                        clearFields();
                        return;
                    }
                    populateFields(t1);
                    loadTickets(t1);
                }));
    }

    private void configureButtons() {
        emailButton.setGraphic(FALoader.createDefault(FontAwesome.Glyph.SEND));

        createButton.disableProperty().bind(employeeComboBox.getSelectionModel().selectedItemProperty().isNotNull());
        updateButton.disableProperty().bind(createButton.disabledProperty().not());
        deleteButton.disableProperty().bind(createButton.disabledProperty().not());
        updateInfoButton.disableProperty().bind(createButton.disabledProperty().not());
    }

    private void populateFields(final EmployeeModel employeeModel) {
        firstNameField.setText(employeeModel.getFirstName());
        lastNameField.setText(employeeModel.getLastName());
        titleField.setText(employeeModel.getTitle());
        infoTextArea.setText(employeeModel.getMiscInfoProperty());
        emailField.setText(employeeModel.getEmail());
        workPhoneField.setText(employeeModel.getWorkPhoneProperty());
        workPhoneExtensionField.setText(String.valueOf(employeeModel.getWorkExtensionProperty()));
        mobilePhoneField.setText(employeeModel.getMobilePhoneProperty());
    }

    @FXML private void onReset() {
        clearFields();
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        titleField.clear();
        emailField.clear();
        workPhoneField.clear();
        workPhoneExtensionField.clear();
        mobilePhoneField.clear();
        infoTextArea.clear();
        employeeComboBox.getSelectionModel().clearSelection();
        ticketTable.setItems(null);
    }

    private void configureWorkExtensionField() {
        workPhoneExtensionField.textProperty().addListener(((observableValue, s, t1) -> {
            if (!t1.matches("\\d*")) {
                workPhoneExtensionField.setText(t1.replaceAll("\\D", ""));
            }
        }));
    }

    private int getExtension() {
        final String extension = workPhoneExtensionField.getText();
        return extension.isEmpty() ? -1 : Integer.parseInt(extension);
    }

    private void loadTickets(final EmployeeModel employeeModel) {
        final int employeeId = employeeModel.getId();
        loadTicketsById(employeeId);
    }

    private void loadTicketsById(final int employeeId) {
        ticketTable.setItems(ticket.getFilteredList(ticketModel
                -> ticketModel.getEmployeeId() == employeeId));
    }

    private void configureTicketTable() {
        actionsColumn.setCellFactory(data -> new TableCell<>() {
            @Override protected void updateItem(Void unused, boolean b) {
                super.updateItem(unused, b);
                if (b) {
                    setGraphic(null);
                    return;
                }

                final HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(5.0);

                final Button open = new Button();
                open.setGraphic(FALoader.createDefault(FontAwesome.Glyph.TICKET));
                open.setOnAction(event -> openTicket(getTableRow().getItem()));

                final Button unAssign = new Button();
                unAssign.setGraphic(FALoader.createDefault(FontAwesome.Glyph.UNLINK));
                unAssign.setOnAction(event -> onUnAssignTicket(getTableRow().getItem()));

                hBox.getChildren().addAll(open, unAssign);
                setGraphic(hBox);
            }
        });

        ticketIdColumn.setCellValueFactory(data -> data.getValue().getIdProperty().asString());
        titleColumn.setCellValueFactory(data -> data.getValue().titleProperty());
        createdOnColumn.setCellValueFactory(data -> data.getValue().createdProperty());
    }

    private void onUnAssignTicket(final TicketModel ticketModel) {
        Alerts.showConfirmation(() -> unAssignTicket(ticketModel), "Are you sure you want to un-assign this ticket?", "This ticket will be removed from the employee?")
                .ifPresent(type -> {
                    if (type == ButtonType.YES) {
                        unAssignTicket(ticketModel);
                    }
                });
    }

    private void unAssignTicket(final TicketModel ticketModel) {
        final int employeeId = ticketModel.getEmployeeId();
        ticketModel.employeeProperty().setValue(0);
        if (ticket.update(ticketModel)) {
            Notifications.showInfo("Update Successful", "Employee was unassigned from ticket.");
            loadTicketsById(employeeId);
        }
    }

    @FXML private void onUpdateInfo() {
        final String comment = infoTextArea.getText();
        if (comment.isEmpty()) {
            Alerts.showError("Failed to update.", "Could not update employee misc. info.", "There was no information provided.");
            return;
        }

        final EmployeeModel employeeModel = employeeComboBox.getSelectionModel().getSelectedItem();
        employeeModel.setMiscInfoProperty(comment);

        if (employee.update(employeeModel)) {
            Notifications.showInfo("Update Successful", "Comment successfully added to employee.");
        }
    }

    @FXML private void onUpdate() {
        final EmployeeModel model = employeeComboBox.getSelectionModel().getSelectedItem();
        if (model == null)
            return;

        model.setFirstName(firstNameField.getText());
        model.setLastName(lastNameField.getText());
        model.setEmail(emailField.getText());
        model.setTitle(titleField.getText());
        model.setWorkExtensionProperty(getExtension());
        model.setWorkPhoneProperty(workPhoneExtensionField.getText());
        model.setMobilePhoneProperty(mobilePhoneField.getText());
        model.setMiscInfoProperty(infoTextArea.getText());

        if (employee.update(model)) {
            Notifications.showInfo("Update Successful", "All employee fields updated.");
        }
    }

    private void openTicket(final TicketModel ticketModel) {
        Display.show(Route.VIEWER, DataRelay.of(ticketModel));
    }

    @FXML private void onCompanyManager() {
        Display.show(Route.COMPANY);
    }

    @FXML private void onDepartmentManager() {
        Display.show(Route.DEPARTMENT);
    }
}
