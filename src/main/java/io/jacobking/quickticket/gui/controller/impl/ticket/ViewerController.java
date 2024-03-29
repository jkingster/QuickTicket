package io.jacobking.quickticket.gui.controller.impl.ticket;

import io.jacobking.quickticket.core.email.EmailConfig;
import io.jacobking.quickticket.core.email.EmailSender;
import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.alert.Notify;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.CommentModel;
import io.jacobking.quickticket.gui.model.impl.EmployeeModel;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.utility.StyleCommons;
import io.jacobking.quickticket.tables.pojos.Comment;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewerController extends Controller {
    private static final int COMMENT_OFFSET = 50;

    private TicketModel            ticketModel;
    private TableView<TicketModel> ticketTable;
    private int                    ticketId;

    private ObservableList<CommentModel> comments;

    @FXML private TextField              titleField;
    @FXML private TextField              creationField;
    @FXML private TextField              employeeField;
    @FXML private Label                  statusLabel;
    @FXML private Label                  priorityLabel;
    @FXML private Label                  totalCommentsLabel;
    @FXML private TextField              commentField;
    @FXML private Button                 postButton;
    @FXML private Button                 updateUserButton;
    @FXML private Button                 statusButton;
    @FXML private Button                 priorityButton;
    @FXML private Button                 resolvedButton;
    @FXML private ListView<CommentModel> commentList;


    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        handleDataRelay();
        configureComments();
        postButton.disableProperty().bind(commentField.textProperty().isEmpty());
    }

    @FXML private void onPost() {
        comment.createModel(new Comment().setPost(commentField.getText()).setPostedOn(DateUtil.nowWithTime()).setTicketId(ticketId));

        commentField.clear();
        commentList.refresh();
        scrollToLastComment();
    }

    @FXML private void onDeleteComment() {
        final CommentModel selected = commentList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Notify.showError("Delete failure.", "No deletion occurred.", "You must select a comment to delete.");
            return;
        }
        commentList.getItems().removeIf(model -> model.getId() == selected.getId());
        commentList.refresh();
    }

    @FXML private void onUpdateUser() {
        final SearchableComboBox<EmployeeModel> employeeComboBox = new SearchableComboBox<>(employee.getObservableList());
        final Button update = new Button("Update");
        update.disableProperty().bind(employeeComboBox.getSelectionModel().selectedItemProperty().isNull());

        final VBox box = new VBox();
        box.setPrefWidth(300);
        box.setPadding(new Insets(10));

        final HBox hBox = new HBox();
        hBox.setSpacing(5.0);
        hBox.getChildren().addAll(employeeComboBox, update);
        hBox.setAlignment(Pos.CENTER);

        box.getChildren().add(hBox);

        final PopOver popOver = new PopOver();
        popOver.setTitle("Update Employee");
        popOver.setContentNode(box);
        popOver.setAnimated(true);
        popOver.setDetachable(false);
        popOver.setHeaderAlwaysVisible(true);

        update.setOnAction(event -> updateTicketEmployee(employeeComboBox, popOver));
        popOver.show(updateUserButton, 25);
    }

    @FXML private void onUpdateStatus() {
        final SearchableComboBox<StatusType> statuComboBox = new SearchableComboBox<>(FXCollections.observableArrayList(StatusType.values()));
        final Button update = new Button("Update");
        update.disableProperty().bind(statuComboBox.getSelectionModel().selectedItemProperty().isNull());

        final VBox box = new VBox();
        box.setPrefWidth(300);
        box.setPadding(new Insets(10));

        final HBox hBox = new HBox();
        hBox.setSpacing(5.0);
        hBox.getChildren().addAll(statuComboBox, update);
        hBox.setAlignment(Pos.CENTER);

        box.getChildren().add(hBox);

        final PopOver popOver = new PopOver();
        popOver.setTitle("Update Status");
        popOver.setContentNode(box);
        popOver.setAnimated(true);
        popOver.setDetachable(false);
        popOver.setHeaderAlwaysVisible(true);

        update.setOnAction(event -> updateTicketStatus(statuComboBox, popOver));
        popOver.show(statusButton, 25);
    }

    @FXML private void onUpdatePriority() {
        final SearchableComboBox<PriorityType> priorityComboBox = new SearchableComboBox<>(FXCollections.observableArrayList(PriorityType.values()));
        final Button update = new Button("Update");
        update.disableProperty().bind(priorityComboBox.getSelectionModel().selectedItemProperty().isNull());

        final VBox box = new VBox();
        box.setPrefWidth(300);
        box.setPadding(new Insets(10));

        final HBox hBox = new HBox();
        hBox.setSpacing(5.0);
        hBox.getChildren().addAll(priorityComboBox, update);
        hBox.setAlignment(Pos.CENTER);

        box.getChildren().add(hBox);

        final PopOver popOver = new PopOver();
        popOver.setTitle("Update Priority");
        popOver.setContentNode(box);
        popOver.setAnimated(true);
        popOver.setDetachable(false);
        popOver.setHeaderAlwaysVisible(true);

        update.setOnAction(event -> updateTicketPriority(priorityComboBox, popOver));
        popOver.show(priorityButton, 25);
    }

    private void updateTicketEmployee(final SearchableComboBox<EmployeeModel> employeeComboBox, final PopOver popOver) {
        final EmployeeModel model = employeeComboBox.getSelectionModel().getSelectedItem();
        if (model == null) {
            Notify.showError("Update failure.", "No update occurred.", "You must select an employee.");
            return;
        }

        ticketModel.employeeProperty().setValue(model.getId());
        postSystemComment("Ticket employee changed to: " + model.getFullName());

        ticket.update(ticketModel);
        reloadPostUpdate(employeeComboBox, popOver);
    }

    private void updateTicketStatus(final SearchableComboBox<StatusType> statusComboBox, final PopOver popOver) {
        final StatusType type = statusComboBox.getSelectionModel().getSelectedItem();
        if (type == null) {
            Notify.showError("Update failure.", "No update occurred.", "You must select a status.");
            return;
        }

        ticketModel.statusProperty().setValue(type);
        postSystemComment("Ticket status changed to: " + type.name());
        ticket.update(ticketModel);
        reloadPostUpdate(statusComboBox, popOver);
    }

    private void updateTicketPriority(final SearchableComboBox<PriorityType> priorityComboBox, final PopOver popOver) {
        final PriorityType type = priorityComboBox.getSelectionModel().getSelectedItem();
        if (type == null) {
            Notify.showError("Update failure.", "No update occurred.", "You must select a priority.");
            return;
        }

        ticketModel.priorityProperty().setValue(type);
        postSystemComment("Ticket priority changed to: " + type.name());
        ticket.update(ticketModel);
        reloadPostUpdate(priorityComboBox, popOver);
    }

    private <T> void reloadPostUpdate(final SearchableComboBox<T> box, final PopOver popOver) {
        box.getSelectionModel().clearSelection();
        popOver.hide();
        ticketTable.refresh();
    }

    @FXML private void onMarkResolved() {
        ticketModel.statusProperty().setValue(StatusType.RESOLVED);
        postSystemComment("This ticket has been marked resolved.");
        ticket.update(ticketModel);
        ticketTable.refresh();

        Notify.showConfirmation("Do you want to notify the employee?", "Click yes to send an e-mail.")
                .ifPresent(type -> {
                    if (type == ButtonType.YES) {
                        final EmployeeModel employeeModel = employee.getModel(ticketModel.getEmployeeId());
                        if (employeeModel == null) {
                            Notify.showError("Could not notify employee.", "No employee attached to ticket.", "Please set an employee.");
                            return;
                        }

                        final String targetEmail = employeeModel.getEmail();
                        if (targetEmail == null || targetEmail.isEmpty()) {
                            Notify.showError("Could not notify employee.", "The attached employee does not have an e-mail set.", "Please set one and try again.");
                            return;
                        }

                        final EmailSender emailSender = new EmailSender(EmailConfig.getInstance());
                        final String subject = String.format("Ticket Resolved (Ticket ID: %s) | %s", ticketModel.getId(), ticketModel.getTitle());
                        final String body = getTicketBody();

                        emailSender.sendEmail(subject, targetEmail, body);
                    }
                });
    }

    private String getTicketBody() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<b>").append("Your support ticket has been resolved.").append("</b>");
        stringBuilder.append("<br/><span style=\"color:red; font-weight:bolder;\">").append("Do not reply to this e-mail. This inbox is not managed.").append("</span>");
        stringBuilder.append("<br/><br/>");
        stringBuilder.append("<b>Ticket Comments:</b>");
        commentList.getItems().forEach(model -> stringBuilder.append("<br/><span style=\"font-size: 15px;\">")
                .append(model.getPostedOn())
                .append("</span>: ")
                .append(model.getPost()));
        return stringBuilder.toString();
    }

    private void postSystemComment(final String commentText) {
        comment.createModel(new Comment().setTicketId(ticketId).setPostedOn(DateUtil.nowWithTime()).setPost(String.format("[System] %s", commentText)));
        commentList.refresh();

        scrollToLastComment();
    }

    private void populateData(final TicketModel ticketModel) {
        this.ticketModel = ticketModel;
        this.ticketId = ticketModel.getId();
        this.comments = comment.getComments(ticketId);
        titleField.setText(ticketModel.getTitle());
        creationField.setText(String.format("Date: %s", ticketModel.getCreation()));
        priorityLabel.textProperty().bind(ticketModel.priorityProperty().asString());
        statusLabel.textProperty().bind(ticketModel.statusProperty().asString());

        final EmployeeModel employeeModel = employee.getModel(ticketModel.getEmployeeId());
        if (employeeModel != null) {
            employeeField.setText(employeeModel.getFullName());
        }
    }

    private void configureComments() {
        commentList.setItems(comments);
        commentList.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(CommentModel commentModel, boolean b) {
                super.updateItem(commentModel, b);
                if (commentModel == null || b) {
                    setGraphic(null);
                    return;
                }

                final VBox vBox = new VBox();
                VBox.setVgrow(vBox, Priority.ALWAYS);

                final Label date = new Label(commentModel.getPostedOn());
                date.setStyle(StyleCommons.COMMON_LABEL_STYLE);

                final Text comment = new Text(commentModel.getPost());
                comment.setWrappingWidth(commentList.getWidth() - date.getBoundsInLocal().getWidth() - COMMENT_OFFSET);

                if (commentModel.getPost().contains("[System]")) {
                    comment.setFill(Color.valueOf("#FF474C"));
                } else {
                    comment.setFill(Color.WHITE);
                }

                vBox.getChildren().addAll(date, comment);
                setGraphic(vBox);
            }
        });

        totalCommentsLabel.setText(String.valueOf(comments.size()));
        comments.addListener((ListChangeListener<? super CommentModel>) change -> {
            while (change.next()) {
                final int size = comments.size();
                totalCommentsLabel.setText(String.valueOf(size));
            }
        });
    }

    private void scrollToLastComment() {
        commentList.scrollTo(commentList.getItems().size() - 1);
    }

    private void handleDataRelay() {
        dataRelay.mapFirst(TicketModel.class).ifPresentOrElse(model -> {
            resolvedButton.disableProperty().bind(model.statusProperty().isEqualTo(StatusType.RESOLVED));
            populateData(model);
        }, () -> {
            Notify.showError("Data Relay Failure", "TicketModel was not passed via data relay.", "Please report this.");
        }); // TODO: error

        final Optional<TableView<TicketModel>> tableOptional = dataRelay.mapTable(1);
        tableOptional.ifPresentOrElse(table -> {
            this.ticketTable = table;
        }, () -> {
            Notify.showError("Data Relay Failure", "TicketTable<TicketModel> was not passed via data relay.", "Please report this.");
        });// TODO: error
    }

    @FXML private void onDelete() {
        Notify.showConfirmation("Are you sure you want to delete this ticket?", "It cannot be recovered.").ifPresent(type -> {
            if (type == ButtonType.YES) {
                ticket.remove(ticketId);
                Display.close(Route.VIEWER);
            }
        });
    }
}
