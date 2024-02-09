package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.CommentModel;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewerController extends Controller {

    private final ObservableList<CommentModel> ticketComments = FXCollections.observableArrayList(
            new CommentModel(0, "Test comment,", "02/04/2000"),
            new CommentModel(1, "Test comment 2", "02/05/2000")
    );

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

        commentList.setItems(ticketComments);
    }

    @FXML
    private void onPost() {
        final String comment = commentField.getText();
        final CommentModel model = new CommentModel(3, comment, "02/05/2000");
        commentList.getItems().add(model);
    }

    private void populateData(final TicketModel ticketModel) {
        titleField.setText(ticketModel.getTitle());
        userField.textProperty().bind(ticketModel.userProperty().asString());
        creationField.setText(String.format("Date: %s", ticketModel.getCreation()));
        priorityLabel.textProperty().bind(ticketModel.priorityProperty().asString());
        statusLabel.textProperty().bind(ticketModel.statusProperty().asString());
    }

    private void configureComments() {

    }
}
