package io.jacobking.quickticket.gui.controller.impl.ticket;

import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.CommentModel;
import io.jacobking.quickticket.gui.model.impl.EmployeeModel;
import io.jacobking.quickticket.gui.model.impl.LinkedTicketModel;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.gui.utility.FALoader;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.controlsfx.glyphfont.FontAwesome;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

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

    @FXML private Button postButton;
    @FXML private Button deleteButton;

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

        final LocalDateTime resolveDate = ticketModel.getResolveBy();
        final String resolveBy = (resolveDate == null) ? "No resolve by date." : DateUtil.formatDateTime(DateUtil.DateFormat.DATE_TIME_ONE, resolveDate);
        resolveByField.setText(resolveBy);

        final EmployeeModel employeeModel = employee.getModel(ticketModel.getEmployeeId());
        final String employeeName = (employeeModel == null) ? "No employee." : employeeModel.getFullName();
        employeeField.setText(employeeName);
    }

    private void loadComments(final TicketModel ticketModel) {
        commentList.setItems(comment.getCommentsByTicketId(ticketModel.getId()));
    }

}
