package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.CommentModel;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.gui.utility.StyleCommons;
import io.jacobking.quickticket.tables.pojos.Comment;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewerController extends Controller {
    private static final int COMMENT_OFFSET = 50;

    private int ticketId;

    private ObservableList<CommentModel> comments;

    @FXML
    private TextField titleField;
    @FXML
    private TextField creationField;
    @FXML
    private TextField userField;
    @FXML
    private Label statusLabel;
    @FXML
    private Label priorityLabel;
    @FXML
    private TextField commentField;
    @FXML
    private Button postButton;

    @FXML
    private ListView<CommentModel> commentList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final Optional<TicketModel> ticketModel = dataRelay.mapFirstInto(TicketModel.class);
        ticketModel.ifPresentOrElse(this::populateData, () -> {
            // TODO: Error?
        });

        configureComments();
        postButton.disableProperty().bind(commentField.textProperty().isEmpty());
    }

    @FXML
    private void onPost() {
        comment.createModel(new Comment()
                .setPost(commentField.getText())
                .setPostedOn(DateUtil.nowWithTime())
                .setTicketId(ticketId));

        commentField.clear();
        commentList.refresh();
        scrollTo(commentList.getItems().size() - 1);
    }

    @FXML
    private void onDeleteComment() {
        final CommentModel selected = commentList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            // TODO: error
            return;
        }
        commentList.getItems().removeIf(model -> model.getId() == selected.getId());
        commentList.refresh();
    }

    private void populateData(final TicketModel ticketModel) {
        this.ticketId = ticketModel.getId();
        this.comments = comment.getComments(ticketId);
        titleField.setText(ticketModel.getTitle());
        userField.textProperty().bind(ticketModel.userProperty().asString());
        creationField.setText(String.format("Date: %s", ticketModel.getCreation()));
        priorityLabel.textProperty().bind(ticketModel.priorityProperty().asString());
        statusLabel.textProperty().bind(ticketModel.statusProperty().asString());
    }

    private void configureComments() {
        commentList.setItems(comments);
        commentList.setCellFactory(data -> new ListCell<>() {
            @Override
            protected void updateItem(CommentModel commentModel, boolean b) {
                super.updateItem(commentModel, b);
                if (commentModel == null || b) {
                    setText(null);
                    return;
                }

                final VBox vBox = new VBox();
                VBox.setVgrow(vBox, Priority.ALWAYS);

                final Label date = new Label(commentModel.getPostedOn());
                date.setStyle(StyleCommons.COMMENT_DATE_LABEL);

                final Text comment = new Text(commentModel.getPost());
                comment.setWrappingWidth(commentList.getWidth() - date.getBoundsInLocal().getWidth() - COMMENT_OFFSET);
                comment.setFill(Color.WHITE);

                vBox.getChildren().addAll(date, comment);
                setGraphic(vBox);
            }
        });

    }

    private void scrollTo(final int commentIndex) {
        if (commentIndex < 0)
            return;
        commentList.scrollTo(commentIndex);
    }
}
