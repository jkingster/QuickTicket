package io.jacobking.quickticket.gui.controller.impl.ticket;

import io.jacobking.quickticket.core.email.EmailBuilder;
import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.alert.Alerts;
import io.jacobking.quickticket.gui.alert.Notifications;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.CommentModel;
import io.jacobking.quickticket.gui.model.impl.EmployeeModel;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.utility.StyleCommons;
import io.jacobking.quickticket.tables.pojos.Comment;
import javafx.beans.property.ObjectProperty;
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
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewerController extends Controller {
    private static final int COMMENT_OFFSET = 50;

    private TicketModel                 ticketModel;
    private TableView<TicketModel>      ticketTable;
    private int                         ticketId;
    private ObjectProperty<TicketModel> lastViewed;

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
    @FXML private Button                 attachJournalButton;
    @FXML private Button                 viewJournalButton;
    @FXML private ListView<CommentModel> commentList;


    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        handleDataRelay();
        configureComments();
        postButton.disableProperty().bind(commentField.textProperty().isEmpty());

        updateLastViewed();
    }

    @FXML private void onPost() {
        comment.createModel(new Comment()
                .setPost(commentField.getText())
                .setPostedOn(DateUtil.nowAsLocalDateTime(DateUtil.DateFormat.DATE_TIME_ONE))
                .setTicketId(ticketId));

        commentField.clear();
        commentList.refresh();
        scrollToLastComment();
    }

    @FXML private void onDeleteComment() {
        final CommentModel selected = commentList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alerts.showError("Delete failure.", "No deletion occurred.", "You must select a comment to delete.");
            return;
        }

        final int commentId = selected.getId();
        Alerts.showConfirmation(
                () -> deleteComment(commentId),
                "Are you sure you want to delete this comment?",
                "The comment cannot be recovered."
        ).ifPresent(type -> {
            if (type == ButtonType.YES) {
                deleteComment(commentId);
            }
        });
    }

    private void deleteComment(final int commentId) {
        comment.remove(commentId);
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
            Alerts.showError("Update failure.", "No update occurred.", "You must select an employee.");
            return;
        }

        ticketModel.employeeProperty().setValue(model.getId());
        if (ticket.update(ticketModel)) {
            reloadPostUpdate(employeeComboBox, popOver);
            postSystemComment("System", "Ticket employee changed to: " + model.getFullName());
            Notifications.showInfo("Update", "Ticket employee updated successfully!");
            employeeField.setText(model.getFullName());
        }
    }

    private void updateTicketStatus(final SearchableComboBox<StatusType> statusComboBox, final PopOver popOver) {
        final StatusType originalStatus = ticketModel.statusProperty().getValue();
        final StatusType type = statusComboBox.getSelectionModel().getSelectedItem();
        if (type == null) {
            Alerts.showError("Update failure.", "No update occurred.", "You must select a status.");
            return;
        }

        ticketModel.statusProperty().setValue(type);

        if (ticket.update(ticketModel, originalStatus)) {
            postSystemComment("System", "Ticket status changed to: " + type.name());
            reloadPostUpdate(statusComboBox, popOver);
            Notifications.showInfo("Update", "Ticket status updated successfully!");
        }
    }

    private void updateTicketPriority(final SearchableComboBox<PriorityType> priorityComboBox, final PopOver popOver) {
        final PriorityType type = priorityComboBox.getSelectionModel().getSelectedItem();
        if (type == null) {
            Alerts.showError("Update failure.", "No update occurred.", "You must select a priority.");
            return;
        }

        ticketModel.priorityProperty().setValue(type);
        if (ticket.update(ticketModel)) {
            postSystemComment("System", "Ticket priority changed to: " + type.name());
            reloadPostUpdate(priorityComboBox, popOver);
            Notifications.showInfo("Update", "Ticket priority updated successfully!");
        }
    }

    private <T> void reloadPostUpdate(final SearchableComboBox<T> box, final PopOver popOver) {
        box.getSelectionModel().clearSelection();
        popOver.hide();

        refreshTable();
    }

    @FXML private void onMarkResolved() {
        ticketModel.statusProperty().setValue(StatusType.RESOLVED);
        postSystemComment("System", "This ticket has been marked resolved.");

        if (ticket.update(ticketModel)) {
            refreshTable();

            Alerts.showInput(
                    "Notify Employee",
                    "Would you like to notify the employee the ticket is resolved along with adding an ending comment?",
                    "No resolving comment was added."
            ).ifPresentOrElse(comment -> {
                final EmployeeModel model = employee.getModel(ticketModel.getEmployeeId());
                if (model == null) {
                    Alerts.showError("Error notifying employee.", "Could not retrieve employee.", "Is there an employee attached to this ticket?");
                    return;
                }

                final String email = model.getEmail();
                if (email.isEmpty()) {
                    Alerts.showError(
                            "Error notifying employee.",
                            "Could not send notification e-mail.",
                            "There is no e-mail attached for this employee."
                    );
                    return;
                }

                new EmailBuilder(email, EmailBuilder.EmailType.RESOLVED)
                        .format(
                                ticketModel.getId(),
                                ticketModel.getTitle(),
                                DateUtil.formatDateTime(DateUtil.DateFormat.DATE_TIME_ONE, ticketModel.getCreation()),
                                model.getFullName(),
                                comment
                        )
                        .email(emailConfig)
                        .setSubject(getSubject(ticketModel))
                        .sendEmail();

                postSystemComment("Ticket Resolved", comment);
            }, () -> postSystemComment("Ticket Resolved", "No resolving comment."));
        }
    }

    private String getSubject(final TicketModel ticketModel) {
        return String.format("Your support ticket has been resolved. | Ticket ID: %s", ticketModel.getId());
    }

    private void refreshTable() {
        if (ticketTable != null) {
            ticketTable.refresh();
        }
    }

    private void postSystemComment(final String prefix, final String commentText) {
        comment.createModel(new Comment().setTicketId(ticketId)
                .setPostedOn(DateUtil.nowAsLocalDateTime(DateUtil.DateFormat.DATE_TIME_ONE))
                .setPost(String.format("[%s]: %s", prefix, commentText)));
        commentList.refresh();
        scrollToLastComment();
    }

    private void populateData(final TicketModel ticketModel) {
        this.ticketModel = ticketModel;
        this.ticketId = ticketModel.getId();
        this.comments = comment.getCommentsByTicketId(ticketId);
        titleField.setText(ticketModel.getTitle());
        creationField.setText(String.format("Date: %s", DateUtil.formatDateTime(
                DateUtil.DateFormat.DATE_TIME_ONE,
                ticketModel.getCreation())
        ));
        priorityLabel.textProperty().bind(ticketModel.priorityProperty().asString());
        statusLabel.textProperty().bind(ticketModel.statusProperty().asString());

        final EmployeeModel employeeModel = employee.getModel(ticketModel.getEmployeeId());
        if (employeeModel != null) {
            employeeField.setText(employeeModel.getFullName());
        }

    }

    private void configureComments() {
        commentList.setItems(comments.sorted(Comparator.comparing(CommentModel::getPostedOn)));
        commentList.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(CommentModel commentModel, boolean b) {
                super.updateItem(commentModel, b);
                if (commentModel == null || b) {
                    setGraphic(null);
                    return;
                }

                final VBox vBox = new VBox();
                VBox.setVgrow(vBox, Priority.ALWAYS);

                final Label date = new Label(DateUtil.formatDateTime(
                        DateUtil.DateFormat.DATE_TIME_ONE,
                        commentModel.getPostedOn()
                ));

                date.setStyle(StyleCommons.COMMON_LABEL_STYLE);

                final Text comment = new Text(commentModel.getPost());
                comment.setWrappingWidth(commentList.getWidth() - date.getBoundsInLocal().getWidth() - COMMENT_OFFSET);

                if (commentModel.getPost().contains("[System]")) {
                    comment.setFill(Color.valueOf("#FF474C"));
                } else if (commentModel.getPost().contains("[Resolved]")) {
                    comment.setFill(Color.valueOf("#248232").brighter());
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
        }); // TODO: error

        final Optional<TableView<TicketModel>> tableOptional = dataRelay.mapTable(1);
        tableOptional.ifPresentOrElse(table -> {
            this.ticketTable = table;
        }, () -> {
        });// TODO: error

        final Optional<ObjectProperty<TicketModel>> lastViewedOptional = dataRelay.mapObjectProperty(2);
        lastViewedOptional.ifPresentOrElse(last -> {
            this.lastViewed = last;
            lastViewed.setValue(ticketModel);
        }, () -> {
            // TODO: SILENT LOG
        });
    }

    @FXML private void onDelete() {
        Alerts.showConfirmation(this::deleteTicket, "Are you sure you want to delete this ticket?", "It cannot be recovered.").ifPresent(type -> {
            if (type == ButtonType.YES) {
                deleteTicket();
            }
        });
    }

    private void deleteTicket() {
        ticket.remove(ticketId);
        lastViewed.setValue(null);
        Display.close(Route.VIEWER);
    }


    private void updateLastViewed() {
        ticketModel.lastViewedProperty().setValue(LocalDateTime.now());
        ticket.update(ticketModel);
    }

}
