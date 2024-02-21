package io.jacobking.quickticket.gui.controller.impl.ticket;

import io.jacobking.quickticket.core.email.EmailConfig;
import io.jacobking.quickticket.core.email.EmailSender;
import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.alert.Notify;
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

    private static final String TICKET_BODY = """
             <h4>Your support ticket has been created. <span style="color: red; font-weight: bold;">Do not reply to this e-mail. This inbox is not managed.</span></h4>
             <p>A help desk technician will reach out to you as soon as possible.</p>
             <br/>
             <b>Subject:</b> %s
             <br/>
             <b>Date:</b> %s
             <br/>
             <b>Ticket ID:</b> %d
            <br/>
             <b>Initial Comment:</b>
             <br/>
             %s
             <br/>
             <br/>
             """;

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

    @SuppressWarnings("unchecked") private void setDataRelay() {
        if (dataRelay == null) {
            return;
        }

        dataRelay.mapFirst(TableView.class).ifPresentOrElse(tableView -> {
            this.ticketTable = (TableView<TicketModel>) tableView;
        }, () -> {
            Notify.showError("Data Relay Failure", "TicketTable<TicketModel> was not passed via data relay.", "Please report this.");
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
        final TicketModel newTicket = ticket.createModel(new Ticket().setTitle(title).setCreatedOn(DateUtil.now()).setPriority(getPriority()).setStatus(getStatus()).setUserId(employeeModel == null ? 0 : employeeModel.getId()));

        insertInitialComment(newTicket);
        sendInitialEmail(newTicket);
        Display.close(Route.TICKET_CREATOR);
    }

    private void sendInitialEmail(final TicketModel ticketModel) {
        if (!emailCheckBox.isSelected()) return;

        final EmployeeModel model = employeeComboBox.getSelectionModel().getSelectedItem();
        if (model == null) return;

        final String email = model.getEmail();
        if (email.isEmpty()) return;

        final EmailSender emailSender = new EmailSender(EmailConfig.getInstance());
        emailSender.sendEmail(String.format("Ticket Created (Ticket ID: %d) | %s", ticketModel.getId(), ticketModel.getTitle()), email, TICKET_BODY.formatted(ticketModel.getTitle(), ticketModel.getCreation(), ticketModel.getId(), commentField.getText().isEmpty() ? "No initial information provided." : commentField.getText()));
    }


    private void insertInitialComment(final TicketModel ticketModel) {
        final String initialComment = commentField.getText();
        if (initialComment.isEmpty()) return;

        final int ticketId = ticketModel.getId();
        comment.createModel(new Comment().setTicketId(ticketId).setPost(initialComment).setPostedOn(DateUtil.nowWithTime()));
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
