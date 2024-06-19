package io.jacobking.quickticket.gui.controller.impl.ticket;

import io.jacobking.quickticket.core.email.EmailBuilder;
import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.alert.Alerts;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.data.DataRelay;
import io.jacobking.quickticket.gui.misc.PopOverBuilder;
import io.jacobking.quickticket.gui.model.impl.CommentModel;
import io.jacobking.quickticket.gui.model.impl.EmployeeModel;
import io.jacobking.quickticket.gui.model.impl.LinkedTicketModel;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.utility.FALoader;
import io.jacobking.quickticket.tables.pojos.Comment;
import io.jacobking.quickticket.tables.pojos.LinkedTicket;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
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
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.glyphfont.FontAwesome;

import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ViewerController extends Controller {

    private TableView<TicketModel>      ticketTable;
    private TicketModel                 viewedTicket;
    private ObjectProperty<TicketModel> lastViewedTicket;

    @FXML private TextField priorityField;
    @FXML private TextField statusField;
    @FXML private TextField titleField;
    @FXML private TextField createdOnField;
    @FXML private TextField resolveByField;
    @FXML private TextField employeeField;
    @FXML private TextField commentField;

    @FXML private ListView<CommentModel>      commentList;
    @FXML private ListView<LinkedTicketModel> linkedTicketList;

    @FXML private Button priorityButton;
    @FXML private Button statusButton;
    @FXML private Button userButton;
    @FXML private Button titleButton;
    @FXML private Button resolveByButton;
    @FXML private Button postButton;
    @FXML private Button deleteButton;
    @FXML private Button linkTicketButton;
    @FXML private Button removeTicketButton;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureCommentList();
        configureCommentButtons();
        initializeRelay();
    }

    private void configureCommentList() {
        commentList.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(CommentModel commentModel, boolean b) {
                super.updateItem(commentModel, b);
                if (commentModel == null || b) {
                    setGraphic(null);
                    return;
                }

                final String commentDate = DateUtil.formatDateTime(DateUtil.DateFormat.DATE_TIME_ONE, commentModel.getPostedOn());
                final String comment = commentModel.getPost();

                final VBox vBox = new VBox(2.5);
                VBox.setVgrow(vBox, Priority.ALWAYS);

                final Label dateLabel = new Label(commentDate);
                dateLabel.setStyle("-fx-font-weight: bolder; -fx-font-size: 1.10em;");

                final int offset = 30;
                final Text commentText = new Text(comment);
                commentText.setFill(Color.WHITE);
                commentText.setWrappingWidth(
                        commentList.getWidth() - dateLabel.getBoundsInLocal().getWidth() - offset
                );

                vBox.getChildren().addAll(dateLabel, commentText);
                setGraphic(vBox);
            }
        });
    }

    private void configureCommentButtons() {
        postButton.setGraphic(FALoader.createDefault(FontAwesome.Glyph.SEND_ALT));
        deleteButton.setGraphic(FALoader.createDefault(FontAwesome.Glyph.CLOSE));

        postButton.disableProperty().bind(commentField.textProperty().isEmpty());
        deleteButton.disableProperty().bind(commentList.getSelectionModel().selectedItemProperty().isNull());
    }

    private void updatePriorityColor(final PriorityType type) {
        switch (type) {
            case LOW -> {
                priorityField.setStyle("-fx-text-fill: GREEN;");
                priorityField.setText("LOW");
            }
            case MEDIUM -> {
                priorityField.setStyle("-fx-text-fill: YELLOW;");
                priorityField.setText("MEDIUM");
            }
            case HIGH -> {
                priorityField.setStyle("-fx-text-fill: ORANGE;");
                priorityField.setText("HIGH");
            }
        }
    }

    private void updateStatusColor(final StatusType type) {
        switch (type) {
            case ACTIVE -> {
                statusField.setStyle("-fx-text-fill: #FF5733");
                statusField.setText("ACTIVE");
            }
            case RESOLVED -> {
                statusField.setStyle("-fx-text-fill: #5DADD5;");
                statusField.setText("RESOLVED");
            }
            case OPEN -> {
                statusField.setStyle("-fx-text-fill: #3498DB;");
                statusField.setText("OPEN");
            }
            case PAUSED -> {
                statusField.setStyle("-fx-text-fill: #FFC300");
                statusField.setText("PAUSED");
            }
        }
    }

    private void configureTicketLinks() {
        linkedTicketList.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(LinkedTicketModel linkedTicketModel, boolean b) {
                super.updateItem(linkedTicketModel, b);
                if (linkedTicketModel == null || b) {
                    setGraphic(null);
                    return;
                }

                final int targetTicketId = linkedTicketModel.getLinkedTicketIdProperty();
                final TicketModel targetTicket = ticket.getModel(targetTicketId);
                if (targetTicket == null) {
                    setGraphic(null);
                    return;
                }

                final Button open = new Button();
                open.setOnAction(event -> openTicket(targetTicket));
                open.setGraphic(FALoader.createDefault(FontAwesome.Glyph.FOLDER_OPEN));

                final Label label = new Label(String.format("Ticket ID: %d", targetTicket.getId()));

                final HBox hBox = new HBox(5.0);
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.getChildren().addAll(open, label);

                final Tooltip tooltip = new Tooltip(targetTicket.getTitle());
                tooltip.setShowDelay(Duration.ZERO);
                Tooltip.install(hBox, tooltip);

                setGraphic(hBox);
            }
        });
        linkedTicketList.setItems(linkedTicket.getLinkedTickets(viewedTicket.getId()));
    }

    private void openTicket(final TicketModel ticketModel) {
        Display.show(Route.VIEWER, DataRelay.of(ticketModel, ticketTable));
    }

    @FXML private void onLinkTicket() {
        setPopOver("Link Ticket", linkTicketButton, ticket.getObservableList(), ((popOver, ticketModelSearchableComboBox) -> {
            final TicketModel selectedTicket = ticketModelSearchableComboBox.getSelectionModel().getSelectedItem();
            if (selectedTicket == null)
                return;

            final LinkedTicket newLinkedTicket = new LinkedTicket()
                    .setTicketId(viewedTicket.getId())
                    .setLinkedId(selectedTicket.getId());

            final LinkedTicketModel newModel = linkedTicket.createModel(newLinkedTicket);
            if (newModel != null) {
                ticketModelSearchableComboBox.getSelectionModel().clearSelection();
                popOver.hide();
                linkedTicketList.refresh();
            }
        }), cell -> {
            final TicketModel item = cell.getItem();
            cell.setGraphic(new Label(String.format("Ticket ID: %d | %s", item.getId(), item.getTitle())));
        });
    }

    @FXML private void onRemoveTicket() {
        final LinkedTicketModel linkedTicketModel = linkedTicketList.getSelectionModel().getSelectedItem();
        if (linkedTicketModel == null) {
            Alerts.showError("Failure", "Could not remove linked ticket.", "Please select one and try again.");
            return;
        }

        final int linkedTicketId = linkedTicketModel.getId();
        Alerts.showConfirmation(() -> removeLinkedTicket(linkedTicketId),
                "Are you sure you want to remove this linked ticket?",
                "This action cannot be undone."
        ).ifPresent(type -> {
            if (type == ButtonType.YES) {
                removeLinkedTicket(linkedTicketId);
            }
        });
    }

    private void removeLinkedTicket(final int linkedTicketId) {
        Platform.runLater(() -> {
            linkedTicket.remove(linkedTicketId);
            linkedTicketList.refresh();
        });
    }

    private void initializeRelay() {
        dataRelay.mapFirst(TicketModel.class).ifPresent(model -> {
            this.viewedTicket = model;
            handleTicket(model);
        });

        final Optional<TableView<TicketModel>> mappedTable = dataRelay.mapTable(1);
        mappedTable.ifPresent(table -> this.ticketTable = table);

        final Optional<ObjectProperty<TicketModel>> mappedObject = dataRelay.mapObjectProperty(2);
        mappedObject.ifPresent(last -> {
            this.lastViewedTicket = last;
            lastViewedTicket.setValue(viewedTicket);
        });
    }

    private void handleTicket(final TicketModel ticketModel) {
        initializeFields(ticketModel);
        configureTicketLinks();
        loadComments(ticketModel);
    }

    private void initializeFields(final TicketModel ticketModel) {
        final String title = ticketModel.getTitle();
        titleField.setText(title);

        final String createdOn = DateUtil.formatDateTime(DateUtil.DateFormat.DATE_TIME_ONE, ticketModel.getCreation());
        createdOnField.setText(createdOn);

        final LocalDate resolveDate = ticketModel.getResolveBy();
        final String resolveBy = (resolveDate == null)
                                 ? "No resolve by date."
                                 : String.format("Resolve by: %s", DateUtil.formatDate(resolveDate.toString()));
        resolveByField.setText(resolveBy);

        final EmployeeModel employeeModel = employee.getModel(ticketModel.getEmployeeId());
        final String employeeName = (employeeModel == null) ? "No employee." : employeeModel.getFullName();
        employeeField.setText(employeeName);

        handlePriority(ticketModel.priorityProperty());
        handleStatus(ticketModel.statusProperty());
    }

    private void handlePriority(final ObjectProperty<PriorityType> priority) {
        priorityField.setText(priority.getValue().name());
        updatePriorityColor(priority.getValue());
    }

    private void handleStatus(final ObjectProperty<StatusType> status) {
        statusField.setText(status.getValue().name());
        updateStatusColor(status.getValue());
    }

    private void loadComments(final TicketModel ticketModel) {
        final ObservableList<CommentModel> commentsList = comment.getCommentsByTicketId(ticketModel.getId());
        commentList.setItems(commentsList.sorted(Comparator.comparing(CommentModel::getPostedOn)));
    }

    @FXML private void onQuickResolve() {
        final StatusType originalStatus = viewedTicket.statusProperty().getValue();
        viewedTicket.statusProperty().setValue(StatusType.RESOLVED);

        if (ticket.update(viewedTicket, originalStatus)) {
            promptNotifyEmployeeAlert(viewedTicket);
        }
    }

    private void promptNotifyEmployeeAlert(final TicketModel viewedTicket) {
        Alerts.showInput("This ticket has been marked resolved.", "Would you like to notify the employee? Please provide any closing comments.")
                .ifPresent(pair -> {
                    final ButtonType type = pair.getLeft();
                    final String comment = pair.getRight();
                    if (type == ButtonType.YES) {
                        postComment(viewedTicket, comment);
                        processNotificationEmail(viewedTicket, comment);
                    } else if (type == ButtonType.NO) {
                        postComment(viewedTicket, comment);
                    } else if (type == ButtonType.CANCEL) {
                        postFailureComment(viewedTicket, "Notification not sent and resolving comment unknown.");
                    }
                });
    }

    private void processNotificationEmail(final TicketModel viewedTicket, final String resolvingComment) {
        final EmployeeModel employee = getEmployee();
        if (employee == null) {
            Alerts.showError("Failed to retrieve record.", "Employee could not be fetched from database.", "Please attach an employee.");
            postFailureComment(viewedTicket, "Could not retrieve employee record.");
            return;
        }

        final String employeeEmail = employee.getEmail();
        if (employeeEmail.isEmpty()) {
            Alerts.showError("Failed to retrieve e-mail.", "Could not notify employee.", "PLease attach an e-mail to employees' record.");
            postFailureComment(viewedTicket, "Could not retrieve employee e-mail.");
            return;
        }

        notifyEmployee(viewedTicket, employeeEmail, resolvingComment, employee);
    }

    private void postEmptyResolvingComment(final TicketModel viewedTicket) {
        postComment(viewedTicket, "No resolving comment.");
    }

    private void postFailureComment(final TicketModel ticketModel, final String comment) {
        postComment(ticketModel, String.format("[SYSTEM]: %s", comment));
    }

    private void notifyEmployee(final TicketModel viewedTicket, final String email, final String resolvingComment, final EmployeeModel employeeModel) {
        new EmailBuilder(email, EmailBuilder.EmailType.RESOLVED)
                .format(
                        viewedTicket.getId(),
                        viewedTicket.getTitle(),
                        DateUtil.formatDateTime(DateUtil.DateFormat.DATE_TIME_ONE, viewedTicket.getCreation()),
                        employeeModel.getFullName(),
                        resolvingComment
                )
                .email(emailConfig)
                .setSubject(getSubject(viewedTicket))
                .sendEmail();
    }

    private String getSubject(final TicketModel ticketModel) {
        return String.format("Your support ticket has been resolved. | Ticket ID: %s", ticketModel.getId());
    }

    private void postComment(final TicketModel ticket, final String commentText) {
        if (commentText.isEmpty()) {
            postEmptyResolvingComment(ticket);
            return;
        }

        comment.createModel(new Comment()
                .setTicketId(ticket.getId())
                .setPost(commentText)
                .setPostedOn(DateUtil.nowAsLocalDateTime(DateUtil.DateFormat.DATE_TIME_ONE))
        );
    }

    private EmployeeModel getEmployee() {
        return employee.getModel(viewedTicket.getEmployeeId());
    }

    @FXML private void onDeleteTicket() {
        Alerts.showConfirmation(() -> deleteTicket(viewedTicket.getId()),
                "Are you sure you want to delete this ticket?", "It cannot be recovered. All associated comments will be purged."
        ).ifPresent(type -> {
            if (type == ButtonType.YES) {
                deleteTicket(viewedTicket.getId());
            }
        });
    }

    private void deleteTicket(final int ticketId) {
        comment.removeCommentsByTicketId(ticketId);
        ticket.remove(ticketId);
        ticketTable.refresh();
        Display.close(Route.VIEWER);
    }

    @FXML private void onPostComment() {
        final String commentText = commentField.getText();
        final CommentModel newComment = comment.createModel(new Comment()
                .setTicketId(viewedTicket.getId())
                .setPost(commentText)
                .setPostedOn(DateUtil.nowAsLocalDateTime(DateUtil.DateFormat.DATE_TIME_ONE))
        );

        if (newComment == null) {
            Alerts.showError("Failure", "Failed to post comment.", "Please try again.");
            return;
        }

        scrollToComment();
        commentField.clear();
    }

    private void scrollToComment() {
        final int targetIndex = commentList.getItems().size() - 1;
        Platform.runLater(() -> commentList.scrollTo(targetIndex));
    }

    @FXML private void onDeleteComment() {
        final CommentModel selectedComment = commentList.getSelectionModel().getSelectedItem();
        if (selectedComment != null) {
            Alerts.showConfirmation(() -> deleteComment(selectedComment),
                            "Are you sure you want to delete this comment?",
                            "It cannot be recovered."
                    )
                    .ifPresent(type -> {
                        if (type == ButtonType.YES) {
                            deleteComment(selectedComment);
                        }
                    });
        }
    }

    private void deleteComment(final CommentModel commentModel) {
        comment.remove(commentModel.getId());
    }

    @FXML private void onUpdatePriority() {
        setPopOver("Update Priority", priorityButton, FXCollections.observableArrayList(PriorityType.values()), (popOver, comboBox) -> {
            final PriorityType newPriority = comboBox.getSelectionModel().getSelectedItem();
            viewedTicket.priorityProperty().setValue(newPriority);

            if (ticket.update(viewedTicket)) {
                updatePriorityColor(newPriority);
                popOver.hide();
            }
        }, null);
    }

    @FXML private void onUpdateStatus() {
        setPopOver("Update Status", statusButton, FXCollections.observableArrayList(StatusType.values()), ((popOver, comboBox) -> {
            final StatusType originalStatus = viewedTicket.statusProperty().getValue();
            final StatusType newStatus = comboBox.getSelectionModel().getSelectedItem();

            viewedTicket.statusProperty().setValue(newStatus);
            if (ticket.update(viewedTicket, originalStatus)) {
                updateStatusColor(newStatus);
                popOver.hide();
            }
        }), null);
    }

    @FXML private void onUpdateUser() {
        setPopOver("Update User", userButton, employee.getObservableList(), ((popOver, comboBox) -> {
            final EmployeeModel newEmployee = comboBox.getSelectionModel().getSelectedItem();
            viewedTicket.employeeProperty().setValue(newEmployee.getId());

            if (ticket.update(viewedTicket)) {
                updateEmployee(newEmployee);
                popOver.hide();
            }
        }), null);
    }

    @FXML private void onUpdateTitle() {
        final Button update = new Button();
        update.setGraphic(FALoader.createDefault(FontAwesome.Glyph.CHECK_SQUARE));

        final TextField inputTitleField = new TextField();

        final HBox hBox = new HBox(5.0);
        hBox.setPrefWidth(200.0);
        hBox.setPadding(new Insets(10));
        hBox.getChildren().addAll(inputTitleField, update);
        hBox.setAlignment(Pos.CENTER);

        final PopOverBuilder popOverBuilder = new PopOverBuilder()
                .setOwner(titleButton)
                .useDefaultSettings()
                .setTitle("Update Title")
                .setContent(hBox);

        update.setOnAction(event -> {
            final String newTitle = inputTitleField.getText();
            if (newTitle.isEmpty()) {
                Alerts.showError("Failed.", "Could not update ticket title.", "Empty title field.");
                return;
            }

            viewedTicket.titleProperty().setValue(newTitle);
            if (ticket.update(viewedTicket)) {
                titleField.setText(newTitle);
                inputTitleField.clear();
                popOverBuilder.get().hide();
            }
        });

        popOverBuilder.show();
    }

    @FXML private void onResolveBy() {
        final Button update = new Button();
        update.setGraphic(FALoader.createDefault(FontAwesome.Glyph.CHECK_SQUARE));

        final DatePicker datePicker = new DatePicker();


        final HBox hBox = new HBox(5.0);
        hBox.setPrefWidth(200.0);
        hBox.setPadding(new Insets(10));
        hBox.getChildren().addAll(datePicker, update);
        hBox.setAlignment(Pos.CENTER);

        final PopOverBuilder popOverBuilder = new PopOverBuilder()
                .setOwner(resolveByButton)
                .useDefaultSettings()
                .setTitle("Update Resolve By Date")
                .setContent(hBox);

        update.setOnAction(event -> {
            final LocalDate localDate = datePicker.getValue();
            final LocalDate now = LocalDate.now();
            if (localDate.isBefore(now)) {
                Alerts.showError("Failed to update.", "The resolve-by date cannot be in the past.", "Please try again.");
                return;
            }

            viewedTicket.resolveByProperty().setValue(localDate);
            if (ticket.update(viewedTicket)) {
                resolveByField.setText(String.format("Resolve by: %s", DateUtil.formatDate(localDate.toString())));
                popOverBuilder.get().hide();
            }
        });

        popOverBuilder.show();
    }

    private void updateEmployee(final EmployeeModel employeeModel) {
        employeeField.setText(employeeModel.getFullName());
    }

    private <T> void setPopOver(final String title, final Button owner, final ObservableList<T> observableList, final BiConsumer<PopOver, SearchableComboBox<T>> biConsumer, final Consumer<ListCell<T>> cellFactoryConsumer) {
        final SearchableComboBox<T> comboBox = new SearchableComboBox<>(observableList);
        final Button update = new Button();
        update.setGraphic(FALoader.createDefault(FontAwesome.Glyph.CHECK_SQUARE));

        final HBox hBox = new HBox(5.0);
        hBox.setPrefWidth(200.0);
        hBox.setPadding(new Insets(10));
        hBox.getChildren().addAll(comboBox, update);
        hBox.setAlignment(Pos.CENTER);

        if (cellFactoryConsumer != null) {
            comboBox.setCellFactory(data -> new ListCell<>() {
                @Override protected void updateItem(T t, boolean b) {
                    super.updateItem(t, b);
                    if (t == null || b) {
                        setGraphic(null);
                        return;
                    }
                    cellFactoryConsumer.accept(this);
                }
            });
        }

        final PopOverBuilder popOverBuilder = new PopOverBuilder()
                .setOwner(owner)
                .setTitle(title)
                .useDefaultSettings()
                .setContent(hBox)
                .setArrowOrientation(PopOver.ArrowLocation.BOTTOM_RIGHT);

        update.setOnAction(event -> biConsumer.accept(popOverBuilder.get(), comboBox));
        popOverBuilder.show();
    }


}
