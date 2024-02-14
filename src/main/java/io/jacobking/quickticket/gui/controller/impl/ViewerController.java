package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.CommentModel;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.gui.utility.StyleCommons;
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
        final String date = DateUtil.now();
        final String comment = commentField.getText();

        final CommentModel model = new CommentModel(commentList.getItems().size() - 1, comment, date);
        commentList.getItems().add(model);
        commentField.clear();

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
        titleField.setText(ticketModel.getTitle());
        userField.textProperty().bind(ticketModel.userProperty().asString());
        creationField.setText(String.format("Date: %s", ticketModel.getCreation()));
        priorityLabel.textProperty().bind(ticketModel.priorityProperty().asString());
        statusLabel.textProperty().bind(ticketModel.statusProperty().asString());
    }

    private void configureComments() {
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

                final Label date = new Label(commentModel.getCommentDate());
                date.setStyle(StyleCommons.COMMENT_DATE_LABEL);

                final Text comment = new Text(commentModel.getComment());
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
