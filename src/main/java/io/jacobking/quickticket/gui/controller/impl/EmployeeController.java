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
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.glyphfont.FontAwesome;

import java.net.URL;
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

    @FXML private ListView<TicketModel> ticketList;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureEmployeeComboBox();
        configureWorkExtensionField();
        configureTicketList();
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
            employeeComboBox.getSelectionModel().select(model);
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
        ticketList.setItems(null);
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
        ticketList.setItems(ticket.getFilteredList(ticketModel
                -> ticketModel.getEmployeeId() == employeeId));
    }

    private void configureTicketList() {
        ticketList.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(TicketModel ticketModel, boolean b) {
                super.updateItem(ticketModel, b);
                if (b || ticketModel == null) {
                    setGraphic(null);
                    return;
                }

                final HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setSpacing(5.0);

                final Button ticket = new Button();
                ticket.setGraphic(FALoader.createDefault(FontAwesome.Glyph.TICKET));
                ticket.setOnAction(event -> openTicket(ticketModel));

                final Label ticketId = new Label(String.format("Ticket ID: %s", ticketModel.getId()));
                final Separator separator = new Separator();
                final Label ticketSubject = new Label(ticketModel.getTitle());
                final Separator separatorTwo = new Separator();
                final Label createdOn = new Label(ticketModel.getCreation().format(DateUtil.DATE_TIME_FORMATTER));

                hBox.getChildren().addAll(ticket, ticketId, separator, ticketSubject, separatorTwo, createdOn);
                setGraphic(hBox);
            }
        });
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
}
