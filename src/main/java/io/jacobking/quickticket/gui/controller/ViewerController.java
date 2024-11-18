package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.custom.CompanySearchBox;
import io.jacobking.quickticket.gui.model.EmployeeModel;
import io.jacobking.quickticket.gui.model.TicketCategoryModel;
import io.jacobking.quickticket.gui.model.TicketModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

        final TicketCategoryModel category = bridgeContext.getCategory().getModel(ticket.getCategory());
        if (category != null) {
            categoryBox.getSelectionModel().select(category);
        }

        createdField.setText(DateUtil.formatDateTime(
                DateUtil.DateFormat.DATE_TIME_ONE,
                ticket.getCreation()
        ));

        findAttachedEmployees();
    }

    private void findAttachedEmployees() {
        final ObservableList<EmployeeModel> attachedEmployees = bridgeContext.getTicketEmployee()
                .getEmployeeModelsForTicket(ticket.getId());

        if (attachedEmployees.isEmpty())
            return;

        final String employees = attachedEmployees.stream()
                .map(EmployeeModel::getFullName)
                .collect(Collectors.joining(", "));

        employeeField.setText(employees);
    }

    @FXML private void onUpdateTicket() {
        final String title = titleField.getText();
        if (title == null || title.isEmpty()) {
            Announcements.get().showError("Error", "Could not update ticket.", "Ticket title required.");
            return;
        }


    }

    @FXML private void onFindEmployees() {
        final CompanySearchBox companySearchBox = new CompanySearchBox(bridgeContext.getCompany().getObservableList());
    }

    // Utilities


    private int getCategoryId() {
        final TicketCategoryModel category = categoryBox.getValue();
        return (category == null) ? 0 : category.getId();
    }

    private TicketModel getUpdatedTicket() {
        ticket.titleProperty().setValue(titleField.getText());
        ticket.statusProperty().setValue(statusBox.getValue());
        ticket.priorityProperty().setValue(priorityBox.getValue());
        ticket.categoryProperty().setValue(getCategoryId());
        return ticket;
    }
}
