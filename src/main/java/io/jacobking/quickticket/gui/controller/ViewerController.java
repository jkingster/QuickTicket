package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.model.EmployeeModel;
import io.jacobking.quickticket.gui.model.TicketCategoryModel;
import io.jacobking.quickticket.gui.model.TicketModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewerController extends Controller {

    private TicketModel            ticket;
    private TableView<TicketModel> ticketTable;

    @FXML private TextField                               ticketIdField;
    @FXML private TextField                               titleField;
    @FXML private SearchableComboBox<StatusType>          statusBox;
    @FXML private SearchableComboBox<PriorityType>        priorityBox;
    @FXML private TextField                               employeeField;
    @FXML private SearchableComboBox<TicketCategoryModel> categoryBox;
    @FXML private TextField                               createdField;


    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ticket = data.mapIndex(0, TicketModel.class);
        this.ticketTable = data.mapTable(1);

        if (ticket == null) {
            display.close(Route.VIEWER);
            return;
        }

        configureStatusBox();
        configurePriorityBox();
        configureCategoryBox();
        populateFields();
    }

    private void configureStatusBox() {
        statusBox.setItems(FXCollections.observableArrayList(StatusType.values()));
        statusBox.setConverter(new StringConverter<StatusType>() {
            @Override public String toString(StatusType statusType) {
                return (statusType == null) ? "Unknown" : statusType.name();
            }

            @Override public StatusType fromString(String s) {
                return null;
            }
        });
    }

    private void configurePriorityBox() {
        priorityBox.setItems(FXCollections.observableArrayList(PriorityType.values()));
        priorityBox.setConverter(new StringConverter<PriorityType>() {
            @Override public String toString(PriorityType priorityType) {
                return (priorityType == null) ? "Unknown" : priorityType.name();
            }

            @Override public PriorityType fromString(String s) {
                return null;
            }
        });
    }

    private void configureCategoryBox() {
        categoryBox.setItems(bridgeContext.getCategory().getObservableList());
        categoryBox.setConverter(new StringConverter<TicketCategoryModel>() {
            @Override public String toString(TicketCategoryModel ticketCategoryModel) {
                return (ticketCategoryModel == null) ? "Unknown" : ticketCategoryModel.getNameProperty();
            }

            @Override public TicketCategoryModel fromString(String s) {
                return null;
            }
        });
    }

    private void populateFields() {
        ticketIdField.setText(ticket.getId() + "");
        titleField.setText(ticket.getTitle());
        statusBox.getSelectionModel().select(StatusType.of(ticket.getStatus()));
        priorityBox.getSelectionModel().select(PriorityType.of(ticket.getPriority()));

        final EmployeeModel employee = bridgeContext.getEmployee().getModel(ticket.getEmployeeId());
        if (employee != null) {
            employeeField.setText(employee.getFullName());
            employeeField.setId(employee.getId() + "");
        }

        final TicketCategoryModel category = bridgeContext.getCategory().getModel(ticket.getCategory());
        if (category != null) {
            categoryBox.getSelectionModel().select(category);
        }

        createdField.setText(DateUtil.formatDateTime(
                DateUtil.DateFormat.DATE_TIME_ONE,
                ticket.getCreation()
        ));
    }

    @FXML private void onUpdateTicket() {
        final String title = titleField.getText();
        if (title == null || title.isEmpty()) {
            Announcements.get().showError("Error", "Could not update ticket.", "Ticket title required.");
            return;
        }


    }

    // Utilities
    private int getEmployeeId() {
        final int currentId = Integer.parseInt(employeeField.getId());
        if (currentId == ticket.getEmployeeId()) {
            return ticket.getEmployeeId();
        }
        return currentId;
    }

    private int getCategoryId() {
        final TicketCategoryModel category = categoryBox.getValue();
        return (category == null) ? 0 : category.getId();
    }

    private TicketModel getUpdatedTicket() {
        ticket.titleProperty().setValue(titleField.getText());
        ticket.statusProperty().setValue(statusBox.getValue());
        ticket.priorityProperty().setValue(priorityBox.getValue());
        ticket.employeeProperty().setValue(getEmployeeId());
        ticket.categoryProperty().setValue(getCategoryId());
        return ticket;
    }
}
