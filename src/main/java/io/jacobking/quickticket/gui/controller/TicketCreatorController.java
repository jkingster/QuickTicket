package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.Data;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.custom.EmployeeCheckBoxListView;
import io.jacobking.quickticket.gui.misc.PopOverBuilder;
import io.jacobking.quickticket.gui.model.*;
import io.jacobking.quickticket.tables.pojos.Comment;
import io.jacobking.quickticket.tables.pojos.Ticket;
import io.jacobking.quickticket.tables.pojos.TicketEmployee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TicketCreatorController extends Controller {


    private TableView<TicketModel>       ticketTable;
    private TicketController             ticketController;
    private EmployeeCheckBoxListView checkListView;

    @FXML private ComboBox<StatusType>                    statusTypeComboBox;
    @FXML private ComboBox<PriorityType>                  priorityTypeComboBox;
    @FXML private TextField                               titleField;
    @FXML private TextArea                                commentArea;
    @FXML private SearchableComboBox<EmployeeModel>       employeeComboBox;
    @FXML private SearchableComboBox<TicketCategoryModel> categoryComboBox;
    @FXML private CheckBox                                openCheckBox;
    @FXML private Button                                  createButton;
    @FXML private Button                                  assignMoreButton;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ticketTable = data.mapTable(0);
        this.ticketController = data.mapIndex(1, TicketController.class);
        this.checkListView = new EmployeeCheckBoxListView(bridgeContext);

        configureStatusBox();
        configurePriorityBox();
        configureEmployeeBox();
        configureCategoryBox();

        createButton.disableProperty().bind(titleField.textProperty().isEmpty());
    }

    private void configureStatusBox() {
        statusTypeComboBox.setItems(FXCollections.observableArrayList(StatusType.values()));
        statusTypeComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(StatusType statusType) {
                return (statusType == null) ? "Unknown" : statusType.name();
            }

            @Override public StatusType fromString(String s) {
                return null;
            }
        });
    }

    private void configurePriorityBox() {
        priorityTypeComboBox.setItems(FXCollections.observableArrayList(PriorityType.values()));
        priorityTypeComboBox.setConverter(new StringConverter<PriorityType>() {
            @Override public String toString(PriorityType priorityType) {
                return (priorityType == null) ? "Unknown" : priorityType.name();
            }

            @Override public PriorityType fromString(String s) {
                return null;
            }
        });
    }

    private void configureEmployeeBox() {
        employeeComboBox.setItems(bridgeContext.getEmployee().getObservableList());
        employeeComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(EmployeeModel employeeModel) {
                return (employeeModel == null) ? "Unknown" : employeeModel.getFullName();
            }

            @Override public EmployeeModel fromString(String s) {
                return null;
            }
        });

        employeeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldEmployee, newEmployee) -> {
            if (newEmployee == null) {
                return;
            }

            if (newEmployee.isIsDisabled()) {
                Announcements.get().showError("Error", "You cannot add this employee", "Employee is disabled.");
                employeeComboBox.getSelectionModel().clearSelection();
                return;
            }

            checkListView.getCheckModel().check(newEmployee);
            if (oldEmployee != null) {
                checkListView.getCheckModel().clearCheck(oldEmployee);
            }
        });
    }

    private void configureCategoryBox() {
        categoryComboBox.setItems(bridgeContext.getCategory().getObservableList());
        categoryComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(TicketCategoryModel ticketCategoryModel) {
                final TicketCategoryModel defaultCategory = bridgeContext.getCategory().getModel(0);
                if (defaultCategory == null) {
                    return "Unknown";
                }

                return (ticketCategoryModel == null) ? defaultCategory.getNameProperty() : ticketCategoryModel.getNameProperty();
            }

            @Override public TicketCategoryModel fromString(String s) {
                return null;
            }
        });
    }

    @FXML private void onCreate() {
        final TicketModel newTicket = getNewTicket();
        if (newTicket == null) {
            Announcements.get().showError("Error", "Could not create ticket.", "Try again.");
            return;
        }

        final int ticketId = newTicket.getId();
        handleComment(ticketId);
        handleEmployees(ticketId);
        handlePostProcessing(newTicket);
    }

    private void handleComment(final int ticketId) {
        final String comment = commentArea.getText();
        if (comment.isEmpty()) {
            return;
        }

        final CommentModel newComment = getNewComment(ticketId, comment);
        if (newComment == null) {
            Announcements.get().showError("Error", "Could not submit comment.", "Sorry about that.");
        }
    }

    private void handleEmployees(final int ticketId) {
        final ObservableList<EmployeeModel> checkedModels = checkListView.getCheckModel().getCheckedItems();
        if (!checkedModels.isEmpty()) {
            for (final EmployeeModel model : checkedModels) {
                createTicketEmployeeModel(ticketId, model.getId());
            }
            return;
        }

        final EmployeeModel selectedEmployee = employeeComboBox.getValue();
        if (selectedEmployee != null) {
            createTicketEmployeeModel(ticketId, selectedEmployee.getId());
        }
    }

    private void createTicketEmployeeModel(final int ticketId, final int employeeId) {
        bridgeContext.getTicketEmployee().createModel(new TicketEmployee()
                .setTicketId(ticketId)
                .setEmployeeId(employeeId)
        );
    }

    private void handlePostProcessing(final TicketModel ticket) {
        if (openCheckBox.isSelected()) {
            display.close(Route.TICKET_CREATOR);
            display.show(Route.VIEWER, Data.of(ticket));
            return;
        }

        ticketController.setTicketTable();
        display.close(Route.TICKET_CREATOR);
        ticketTable.refresh();
        ticketTable.scrollTo(0);
    }

    @FXML private void onSelectMultipleEmployees() {
        if (bridgeContext.getEmployee().getObservableList().isEmpty()) {
            return;
        }

        PopOverBuilder.build()
                .setDetached(true)
                .setTitle("Assign Multiple Employees..")
                .setAnimated(true)
                .setHideOnEscape(true)
                .setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER)
                .process(process -> checkListView)
                .show(assignMoreButton, 10);
    }


    @FXML private void onReset() {
    }

    // Utilities
    private TicketModel getNewTicket() {
        return bridgeContext.getTicket().createModel(new Ticket()
                .setTitle(titleField.getText())
                .setStatus(getStatus())
                .setPriority(getPriority())
                .setCategoryId(getCategoryId())
                .setCreatedOn(DateUtil.nowAsLocalDateTime(DateUtil.DateFormat.DATE_TIME_ONE))
        );
    }

    private CommentModel getNewComment(final int ticketId, final String comment) {
        return bridgeContext.getComment().createModel(new Comment()
                .setTicketId(ticketId)
                .setPost(comment)
                .setPostedOn(DateUtil.nowAsLocalDateTime(DateUtil.DateFormat.DATE_TIME_ONE))
        );
    }

    private String getStatus() {
        final StatusType selected = statusTypeComboBox.getValue();
        return (selected == null) ? StatusType.OPEN.name() : selected.name();
    }

    private String getPriority() {
        final PriorityType selected = priorityTypeComboBox.getValue();
        return (selected == null) ? PriorityType.LOW.name() : selected.name();
    }

    private int getCategoryId() {
        final TicketCategoryModel category = categoryComboBox.getValue();
        if (category == null) {
            return bridgeContext.getCategory()
                    .getModel(0)
                    .getId();
        }
        return category.getId();
    }
}
