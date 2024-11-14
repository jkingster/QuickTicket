package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.Data;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.model.EmployeeModel;
import io.jacobking.quickticket.gui.model.TicketCategoryModel;
import io.jacobking.quickticket.gui.model.TicketModel;
import io.jacobking.quickticket.tables.pojos.Comment;
import io.jacobking.quickticket.tables.pojos.Ticket;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TicketCreatorController extends Controller {

    private TicketController       ticketController;
    private TableView<TicketModel> ticketTable;

    @FXML private ComboBox<StatusType>                    statusTypeComboBox;
    @FXML private ComboBox<PriorityType>                  priorityTypeComboBox;
    @FXML private SearchableComboBox<EmployeeModel>       employeeComboBox;
    @FXML private SearchableComboBox<TicketCategoryModel> categoryComboBox;
    @FXML private TextField                               titleField;
    @FXML private TextArea                                commentField;
    @FXML private Button                                  createButton;
    @FXML private CheckBox                                emailCheckBox;
    @FXML private CheckBox                                openCheckBox;


    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataRelay();
        configureStatusComboBox();
        configurePriorityComboBox();
        configureEmployeeComboBox();
        configureCategoryComboBox();
        createButton.disableProperty().bind(titleField.textProperty().isEmpty());
    }

    private void setDataRelay() {
        if (data == null) {
            return;
        }

        data.mapIndex(0, TicketController.class).ifPresent(controller -> {
            this.ticketController = controller;
            this.ticketTable = ticketController.getTicketTable();
        });
    }

    private void configureStatusComboBox() {
        statusTypeComboBox.setItems(FXCollections.observableArrayList(StatusType.values()));
        statusTypeComboBox.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(StatusType statusType, boolean b) {
                super.updateItem(statusType, b);
                if (b || statusType == null) {
                    setText(null);
                    return;
                }

                final String name = statusType.name();
                setText(name);
            }
        });
    }

    private void configurePriorityComboBox() {
        priorityTypeComboBox.setItems(FXCollections.observableArrayList(PriorityType.values()));
        priorityTypeComboBox.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(PriorityType priorityType, boolean b) {
                super.updateItem(priorityType, b);
                if (b || priorityType == null) {
                    setText(null);
                    return;
                }

                final String name = priorityType.name();
                setText(name);
            }
        });
    }

    private void configureEmployeeComboBox() {
        employeeComboBox.setItems(bridgeContext.getEmployee().getObservableList());
        employeeComboBox.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(EmployeeModel employeeModel, boolean b) {
                super.updateItem(employeeModel, b);
                if (employeeModel == null || b) {
                    setText(null);
                    return;
                }
                setText(employeeModel.getFullName());
            }
        });

        employeeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldEmployee, newEmployee) -> {
            if (newEmployee == null) {
                return;
            }

            if (newEmployee.isIsDisabled()) {
                Announcements.get().showError("Error", "Employee is disabled.", "New tickets cannot be assigned to this employee. Re-activate them first.");
                employeeComboBox.getSelectionModel().clearSelection();
            }
        });
    }

    private void configureCategoryComboBox() {
        categoryComboBox.setItems(bridgeContext.getCategory().getObservableList());
        categoryComboBox.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(TicketCategoryModel ticketCategoryModel, boolean b) {
                super.updateItem(ticketCategoryModel, b);
                if (ticketCategoryModel == null || b) {
                    setText(null);
                    return;
                }
                setText(ticketCategoryModel.getNameProperty());
            }
        });
    }

    @FXML private void onCreate() {
        final String title = titleField.getText();
        final EmployeeModel employeeModel = employeeComboBox.getSelectionModel().getSelectedItem();
        final TicketModel newTicket = bridgeContext.getTicket().createModel(new Ticket()
                .setTitle(title)
                .setCreatedOn(DateUtil.nowAsLocalDateTime(DateUtil.DateFormat.DATE_TIME_ONE))
                .setPriority(getPriority())
                .setStatus(getStatus())
                .setCategoryId(getCategoryId())
                .setEmployeeId(employeeModel == null ? 0 : employeeModel.getId()));

        insertInitialComment(newTicket);
        display.close(Route.TICKET_CREATOR);

        if (openCheckBox.isSelected()) {
            display.show(Route.VIEWER, Data.of(newTicket, ticketTable));
        }

        ticketController.setTicketTable();
        ticketTable.scrollTo(0);
        ticketTable.refresh();
    }

    private int getCategoryId() {
        final TicketCategoryModel selected = categoryComboBox.getSelectionModel().getSelectedItem();
        return selected == null ? 0 : selected.getId();
    }


    private void insertInitialComment(final TicketModel ticketModel) {
        final String initialComment = commentField.getText();
        if (initialComment.isEmpty()) return;

        final int ticketId = ticketModel.getId();
        bridgeContext.getComment().createModel(new Comment()
                .setTicketId(ticketId)
                .setPost(initialComment)
                .setPostedOn(DateUtil.nowAsLocalDateTime(DateUtil.DateFormat.DATE_TIME_ONE)));
    }

    @FXML private void onReset() {
        titleField.clear();
        commentField.clear();
        priorityTypeComboBox.getSelectionModel().clearSelection();
        statusTypeComboBox.getSelectionModel().clearSelection();
        employeeComboBox.getSelectionModel().clearSelection();
        categoryComboBox.getSelectionModel().clearSelection();
    }

    private String getPriority() {
        final PriorityType type = priorityTypeComboBox.getSelectionModel().getSelectedItem();
        return type == null ? "LOW" : type.name();
    }

    private String getStatus() {
        final StatusType type = statusTypeComboBox.getSelectionModel().getSelectedItem();
        return type == null ? "OPEN" : type.name();
    }
}
