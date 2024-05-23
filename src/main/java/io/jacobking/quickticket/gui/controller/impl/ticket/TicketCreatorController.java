package io.jacobking.quickticket.gui.controller.impl.ticket;

import io.jacobking.quickticket.core.email.EmailBuilder;
import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.EmployeeModel;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
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

    @FXML private ComboBox<StatusType> statusTypeComboBox;

    @FXML private ComboBox<PriorityType> priorityTypeComboBox;

    @FXML private SearchableComboBox<EmployeeModel> employeeComboBox;

    @FXML private TextField titleField;

    @FXML private TextArea commentField;

    @FXML private Button createButton;

    @FXML private CheckBox emailCheckBox;


    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataRelay();
        configureStatusComboBox();
        configurePriorityComboBox();
        configureEmployeeComboBox();
        createButton.disableProperty().bind(titleField.textProperty().isEmpty());
    }

    private void setDataRelay() {
        if (dataRelay == null) {
            return;
        }

        dataRelay.mapIndex(0, TicketController.class).ifPresent(controller -> {
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
        employeeComboBox.setItems(employee.getObservableList());
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
    }

    @FXML private void onCreate() {
        final String title = titleField.getText();
        final EmployeeModel employeeModel = employeeComboBox.getSelectionModel().getSelectedItem();
        final TicketModel newTicket = ticket.createModel(new Ticket()
                .setTitle(title)
                .setCreatedOn(DateUtil.nowAsLocalDateTime(DateUtil.DateFormat.DATE_TIME_ONE))
                .setPriority(getPriority())
                .setStatus(getStatus())
                .setEmployeeId(employeeModel == null ? 0 : employeeModel.getId()));

        insertInitialComment(newTicket);
        sendInitialEmail(newTicket);
        Display.close(Route.TICKET_CREATOR);

        ticketController.setTicketTable();
        ticketTable.scrollTo(0);
    }

    private void sendInitialEmail(final TicketModel ticketModel) {
        if (!emailCheckBox.isSelected()) return;

        final EmployeeModel model = employeeComboBox.getSelectionModel().getSelectedItem();
        if (model == null) return;

        final String email = model.getEmail();
        if (email.isEmpty()) return;

        final String creation = DateUtil.formatDateTime(DateUtil.DateFormat.DATE_TIME_ONE, ticketModel.getCreation());
        new EmailBuilder(email, EmailBuilder.EmailType.NEW_TICKET)
                .format(model.getFullName(), ticketModel.getId(), ticketModel.getTitle(), model.getFullName(), creation)
                .email(emailConfig)
                .setSubject(getSubject(ticketModel))
                .sendEmail();
    }

    private String getSubject(final TicketModel ticketModel) {
        return String.format("Your support ticket has been created. | Ticket ID: %s", ticketModel.getId());
    }


    private void insertInitialComment(final TicketModel ticketModel) {
        final String initialComment = commentField.getText();
        if (initialComment.isEmpty()) return;

        final int ticketId = ticketModel.getId();
        comment.createModel(new Comment()
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
