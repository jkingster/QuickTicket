package io.jacobking.quickticket.gui.controller.impl.ticket;

import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.data.DataRelay;
import io.jacobking.quickticket.gui.misc.PopOverBuilder;
import io.jacobking.quickticket.gui.model.impl.*;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.utility.FALoader;
import io.jacobking.quickticket.tables.pojos.Comment;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
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
import java.time.temporal.ChronoUnit;
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
    @FXML private TextField categoryField;

    @FXML private ListView<CommentModel>      commentList;

    @FXML private Button priorityButton;
    @FXML private Button statusButton;
    @FXML private Button userButton;
    @FXML private Button titleButton;
    @FXML private Button resolveByButton;
    @FXML private Button postButton;
    @FXML private Button deleteButton;
    @FXML private Button linkTicketButton;
    @FXML private Button removeTicketButton;
    @FXML private Button categoryButton;

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

        commentField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                final String comment = commentField.getText();
                if (comment.isEmpty())
                    return;

                postComment(viewedTicket, comment);
                commentField.clear();
                scrollToComment();
            }
        });
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
        refreshTable();
    }



    private void openTicket(final TicketModel ticketModel) {
        Display.show(Route.VIEWER, DataRelay.of(ticketModel, ticketTable));
    }

    private void refreshTable() {
        if (ticketTable != null) {
            ticketTable.refresh();
        }
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
        loadComments(ticketModel);
    }

    private void initializeFields(final TicketModel ticketModel) {
        final String title = ticketModel.getTitle();
        titleField.setText(title);

        final String createdOn = DateUtil.formatDateTime(DateUtil.DateFormat.DATE_TIME_ONE, ticketModel.getCreation());
        createdOnField.setText(createdOn);

        final EmployeeModel employeeModel = employee.getModel(ticketModel.getEmployeeId());
        final String employeeName = (employeeModel == null) ? "No employee." : employeeModel.getFullName();
        employeeField.setText(employeeName);

        final TicketCategoryModel categoryModel = category.getModel(ticketModel.getCategory());
        handleCategory(categoryModel);

        handlePriority(ticketModel.priorityProperty());
        handleStatus(ticketModel.statusProperty());
    }

    private void handleDate(final LocalDate localDate) {
        if (localDate == null)
            return;

        final LocalDate now = LocalDate.now();
        final long difference = ChronoUnit.DAYS.between(now, localDate);

        if (now.isEqual(localDate)) {
            resolveByField.setStyle("-fx-text-fill: RED;");
        } else if (Math.abs(difference) <= 5) {
            resolveByField.setStyle("-fx-text-fill: YELLOW;");
        }
    }

    private void handlePriority(final ObjectProperty<PriorityType> priority) {
        priorityField.setText(priority.getValue().name());
        updatePriorityColor(priority.getValue());
    }

    private void handleStatus(final ObjectProperty<StatusType> status) {
        statusField.setText(status.getValue().name());
        updateStatusColor(status.getValue());
    }

    private void handleCategory(final TicketCategoryModel categoryModel) {
        if (categoryModel == null) {
            categoryField.setText("Undefined");
            return;
        }

        final String name = categoryModel.getNameProperty();
        categoryField.setText(name);

        categoryField.setStyle("-fx-text-fill: " + categoryModel.getColorAsRGB() + ";");
    }

    private void loadComments(final TicketModel ticketModel) {
        final ObservableList<CommentModel> commentsList = comment.getCommentsByTicketId(ticketModel.getId());
        commentList.setItems(commentsList.sorted(Comparator.comparing(CommentModel::getPostedOn)));
    }

    @FXML private void onQuickResolve() {
        final StatusType originalStatus = viewedTicket.statusProperty().getValue();
        viewedTicket.statusProperty().setValue(StatusType.RESOLVED);
        updateStatusColor(StatusType.RESOLVED);

        if (ticket.update(viewedTicket, originalStatus)) {
            promptNotifyEmployeeAlert(viewedTicket);
        }
    }

    private void promptNotifyEmployeeAlert(final TicketModel viewedTicket) {
        Announcements.get().showInput("This ticket has been marked resolved.", "Would you like to notify the employee? Please provide any closing comments.")
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
            Announcements.get().showError("Failed to retrieve record.", "Employee could not be fetched from database.", "Please attach an employee.");
            postFailureComment(viewedTicket, "Could not retrieve employee record.");
            return;
        }

        final String employeeEmail = employee.getEmail();
        if (employeeEmail.isEmpty()) {
            Announcements.get().showError("Failed to retrieve e-mail.", "Could not notify employee.", "PLease attach an e-mail to employees' record.");
            postFailureComment(viewedTicket, "Could not retrieve employee e-mail.");
            return;
        }

    }

    private void postEmptyResolvingComment(final TicketModel viewedTicket) {
        postComment(viewedTicket, "No resolving comment.");
    }

    private void postFailureComment(final TicketModel ticketModel, final String comment) {
        postComment(ticketModel, String.format("[SYSTEM]: %s", comment));
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
        Announcements.get().showConfirmation(() -> deleteTicket(viewedTicket.getId()),
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
        refreshTable();
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
            Announcements.get().showError("Failure", "Failed to post comment.", "Please try again.");
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
            Announcements.get().showConfirmation(() -> deleteComment(selectedComment),
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
                refreshTable();
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
              refreshTable();
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
              refreshTable();
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
                Announcements.get().showError("Failed.", "Could not update ticket title.", "Empty title field.");
                return;
            }

            viewedTicket.titleProperty().setValue(newTitle);
            if (ticket.update(viewedTicket)) {
                titleField.setText(newTitle);
                inputTitleField.clear();
                popOverBuilder.get().hide();
                refreshTable();
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
                Announcements.get().showError("Failed to update.", "The resolve-by date cannot be in the past.", "Please try again.");
                return;
            }

            if (ticket.update(viewedTicket)) {
                refreshTable();
                resolveByField.setText(String.format("Resolve by: %s", DateUtil.formatDate(localDate.toString())));
                popOverBuilder.get().hide();
            }
        });

        popOverBuilder.show();
    }

    @FXML private void onUpdateCategory() {
        setPopOver("Update Category", categoryButton, category.getObservableList(), ((popOver, comboBox) -> {
            final TicketCategoryModel newCategory = comboBox.getSelectionModel().getSelectedItem();
            if (newCategory == null) {
                Announcements.get().showError("Failure", "Could not update ticket category.", "Please select one and try again.");
                return;
            }

            viewedTicket.categoryProperty().setValue(newCategory.getId());
            if (ticket.update(viewedTicket)) {
                handleCategory(newCategory);
                popOver.hide();
            }
        }), null);
    }


    private void updateEmployee(final EmployeeModel employeeModel) {
        employeeField.setText(employeeModel.getFullName());
        refreshTable();
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
