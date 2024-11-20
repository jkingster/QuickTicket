package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.gui.Data;
import io.jacobking.quickticket.gui.Route;
import io.jacobking.quickticket.gui.alert.Announcements;
import io.jacobking.quickticket.gui.misc.PopOverBuilder;
import io.jacobking.quickticket.gui.model.*;
import io.jacobking.quickticket.gui.utility.IconLoader;
import io.jacobking.quickticket.tables.pojos.Comment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.SearchableComboBox;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ViewerController extends Controller {

    private static final int NTH_MODIFIER = 50, BASE_HEIGHT = 15, INSET = 10;
    private       TicketModel                             ticket;
    private       TableView<TicketModel>                  ticketTable;
    @FXML private TextField                               ticketIdField;
    @FXML private TextField                               titleField;
    @FXML private SearchableComboBox<StatusType>          statusBox;
    @FXML private SearchableComboBox<PriorityType>        priorityBox;
    @FXML private TextField                               employeeField;
    @FXML private SearchableComboBox<TicketCategoryModel> categoryBox;
    @FXML private TextField                               createdField;
    @FXML private Button                                  findEmployeeButton;
    @FXML private Button                                  deleteEmployeeButton;
    @FXML private TextArea                                commentArea;
    @FXML private Button                                  submitCommentButton;
    @FXML private ListView<CommentModel>                  commentList;

    @FXML private ListView<LinkModel> attachmentList;
    @FXML private Button              addURLButton;
    @FXML private Button              addDocButton;


    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        this.ticket = data.mapIndex(0, TicketModel.class);
        this.ticketTable = data.mapTable(1);

        if (ticket == null) {
            display.close(Route.VIEWER);
            return;
        }

        configureStatusBox();
        configurePriorityBox();
        populateFields();
        configureButtons();
        configureComments();
        configureAttachmentList();
    }

    private void configureStatusBox() {
        statusBox.setItems(FXCollections.observableArrayList(StatusType.values()));
        statusBox.setConverter(new StringConverter<>() {
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

    private void configureButtons() {
        findEmployeeButton.setGraphic(IconLoader.getMaterialIcon(Material2MZ.PERSON_ADD));
        deleteEmployeeButton.setGraphic(IconLoader.getMaterialIcon(Material2MZ.PERSON_REMOVE));
        submitCommentButton.setGraphic(IconLoader.getMaterialIcon(Material2MZ.MODE_COMMENT));
        submitCommentButton.disableProperty().bind(commentArea.textProperty().isEmpty());

        addURLButton.setGraphic(IconLoader.getMaterialIcon(MaterialDesign.MDI_LINK));
        addDocButton.setGraphic(IconLoader.getMaterialIcon(Material2AL.ATTACH_FILE));
    }

    private void configureComments() {
        final URL url = App.class.getResource("css/core/comment-list-view.css");
        if (url != null) {
            commentList.getStylesheets().add(url.toExternalForm());
            commentList.getStyleClass().add("comment-list");
        }

        final int ticketId = ticket.getId();
        final ObservableList<CommentModel> orderedComments = bridgeContext.getComment()
                .getCommentsByTicketId(ticketId)
                .sorted(Comparator.comparing(CommentModel::getPostedOn));

        commentList.setItems(orderedComments);
        commentList.scrollTo(commentList.getItems().size() - 1);
        commentList.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(CommentModel commentModel, boolean empty) {
                super.updateItem(commentModel, empty);
                if (commentModel == null || empty) {
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                    return;
                }

                setStyle("");
                final VBox container = new VBox(5);
                final HBox topBox = new HBox(2.5);
                final Label trashLabel = new Label();
                trashLabel.setGraphic(IconLoader.getMaterialIcon(Material2AL.DELETE_FOREVER));
                trashLabel.setOnMousePressed(event -> handleCommentDeletion(commentModel));
                final Label dateLabel = new Label(DateUtil.formatDateTime(DateUtil.DateFormat.DATE_TIME_ONE, commentModel.getPostedOn()));
                topBox.getChildren().addAll(trashLabel, dateLabel);

                final Text comment = new Text(commentModel.getPost());
                comment.setFill(Color.WHITE);
                comment.setWrappingWidth(575);
                container.getChildren().addAll(topBox, comment);


                setGraphic(container);
            }
        });
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

    private void handleCommentDeletion(final CommentModel commentModel) {
        Announcements.get().showConfirmation(() -> deleteComment(commentModel),
                        "Are you sure you want to delete this comment?", "It cannot be recovered.")
                .ifPresent(type -> {
                    if (type == ButtonType.YES) {
                        deleteComment(commentModel);
                    }
                });
    }

    private void deleteComment(final CommentModel commentModel) {
        if (!bridgeContext.getComment().remove(commentModel.getId())) {
            Announcements.get().showError("Error", "Failed to delete comment.", "Try again.");
            return;
        }

        Announcements.get().showConfirm("Success", "Comment deleted.");
    }

    private void configureAttachmentList() {
        final int ticketId = ticket.getId();
        attachmentList.setItems(bridgeContext.getTicketLink().getLinksForTicket(ticketId));
        attachmentList.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(LinkModel linkModel, boolean empty) {
                super.updateItem(linkModel, empty);
                if (linkModel == null || empty) {
                    setGraphic(null);
                    return;
                }

                final HBox box = new HBox(2.5);
                box.setAlignment(Pos.CENTER_LEFT);
                final Button open = new Button();
                open.setGraphic(IconLoader.getMaterialIcon(Material2MZ.OPEN_IN_BROWSER));
                open.setOnAction(event -> openLink(linkModel));
                open.setMaxWidth(Region.USE_PREF_SIZE);
                open.setTooltip(new Tooltip(linkModel.getLink()));


                final String description = linkModel.getDescription();
                final Label descriptionLabel = new Label(description);
                descriptionLabel.setStyle("-fx-font-weight: bolder; -fx-text-fill: white;");

                box.getChildren().addAll(open, descriptionLabel);

                setGraphic(box);
            }
        });

    }

    private void openLink(final LinkModel linkModel) {

    }

    @FXML private void onUpdateTicket() {
        final String title = titleField.getText();
        if (title == null || title.isEmpty()) {
            Announcements.get().showError("Error", "Could not update ticket.", "Ticket title required.");
            return;
        }


    }

    @FXML private void onFindEmployees() {
        display.show(Route.FIND_EMPLOYEE, Data.of(ticket, employeeField));
    }

    @FXML private void onDeleteEmployees() {
        PopOverBuilder.build()
                .setTitle("Remove Employees")
                .setDetached(true)
                .setAnimated(true)
                .process(this::getDeleteBox)
                .show(deleteEmployeeButton, 10);
    }

    private Pane getDeleteBox(PopOverBuilder popOver) {
        final ObservableList<EmployeeModel> employeeList = bridgeContext.getTicketEmployee()
                .getEmployeeModelsForTicket(ticket.getId());

        final Pane pane = new Pane();
        pane.setPadding(new Insets(INSET, 0, INSET, 0));

        final VBox vBox = new VBox(5.0);
        vBox.setAlignment(Pos.CENTER);

        final double calculatedHeight = (NTH_MODIFIER * employeeList.size()) + BASE_HEIGHT;
        vBox.setPrefHeight(calculatedHeight);

        final CheckListView<EmployeeModel> checkListView = getCheckListView(employeeList);

        final Button delete = new Button("Delete");
        delete.setOnAction(event -> deleteEmployeeFromTicket(popOver, ticket.getId(), checkListView));

        vBox.getChildren().addAll(checkListView, delete);
        pane.getChildren().add(vBox);
        return pane;
    }

    private CheckListView<EmployeeModel> getCheckListView(final ObservableList<EmployeeModel> employeeModels) {
        return new CheckListView<>(employeeModels);
    }

    private void deleteEmployeeFromTicket(PopOverBuilder popOver, final int ticketId, final CheckListView<EmployeeModel> listView) {
        for (final EmployeeModel remove : listView.getCheckModel().getCheckedItems()) {
            final int employeeId = remove.getId();
            bridgeContext.getTicketEmployee().removeByTicketAndEmployeeId(ticketId, employeeId);
        }

        popOver.hide();
        resetEmployeeField();
    }

    private void resetEmployeeField() {
        final String employees = bridgeContext.getTicketEmployee().getEmployeeModelsForTicket(ticket.getId())
                .stream()
                .map(EmployeeModel::getFullName)
                .collect(Collectors.joining(", "));

        employeeField.setText(employees);
    }

    @FXML private void onSubmitComment() {
        final String comment = commentArea.getText();

        final CommentModel newComment = bridgeContext.getComment()
                .createModel(new Comment()
                        .setTicketId(ticket.getId())
                        .setPost(comment)
                        .setPostedOn(DateUtil.nowAsLocalDateTime(DateUtil.DateFormat.DATE_TIME_ONE))
                );

        if (newComment == null) {
            Announcements.get().showError("Error", "Could not post comment.", "Please try again.");
            return;
        }

        commentList.scrollTo(commentList.getItems().size() - 1);
        commentArea.clear();
    }

    @FXML private void onAddUrl() {
        display.show(Route.ADD_URL, Data.of(ticket, attachmentList));
    }

    @FXML private void onAddDocument() {

    }

    // Utilities

    private TicketModel getUpdatedTicket() {
        ticket.titleProperty().setValue(titleField.getText());
        ticket.statusProperty().setValue(statusBox.getValue());
        ticket.priorityProperty().setValue(priorityBox.getValue());
        ticket.categoryProperty().setValue(getCategoryId());
        return ticket;
    }

    private int getCategoryId() {
        final TicketCategoryModel category = categoryBox.getValue();
        return (category == null) ? 0 : category.getId();
    }
}
