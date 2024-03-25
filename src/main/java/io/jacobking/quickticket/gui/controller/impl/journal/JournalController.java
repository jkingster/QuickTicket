package io.jacobking.quickticket.gui.controller.impl.journal;

import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.alert.Alerts;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.data.DataRelay;
import io.jacobking.quickticket.gui.misc.PopOverBuilder;
import io.jacobking.quickticket.gui.model.impl.JournalModel;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.utility.FALoader;
import io.jacobking.quickticket.tables.pojos.Journal;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;

import java.net.URL;
import java.util.ResourceBundle;

public class JournalController extends Controller {
    private static final int COMMENT_OFFSET = 5;

    private       TableView<TicketModel> ticketTable;
    @FXML private ListView<JournalModel> journalList;
    @FXML private Button                 addButton;
    @FXML private Button                 deleteButton;
    @FXML private Button                 editButton;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureJournalList();

        deleteButton.disableProperty().bind(journalList.getSelectionModel().selectedItemProperty().isNull());
        editButton.disableProperty().bind(deleteButton.disableProperty());
    }

    private void configureJournalList() {
        journalList.setItems(journal.getObservableList());
        journalList.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(JournalModel journalModel, boolean b) {
                super.updateItem(journalModel, b);
                if (b || journalModel == null) {
                    setGraphic(null);
                    return;
                }

                final HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setSpacing(10.0);

                final Button view = new Button();
                view.setGraphic(FALoader.createDefault(FontAwesome.Glyph.TICKET));
                view.setOnAction(event -> viewTickets(view, journalModel.getId()));
                hBox.getChildren().add(view);

                final VBox vBox = new VBox();
                view.setAlignment(Pos.TOP_CENTER);

                final Label date = new Label(journalModel.getCreatedOnProperty());
                date.setStyle("-fx-font-weight: bolder; -fx-text-fill: white;");

                final Label comment = new Label(journalModel.getNoteProperty());
                comment.setStyle("-fx-text-fill: white");
                vBox.getChildren().addAll(date, comment);

                hBox.getChildren().add(vBox);

                setGraphic(hBox);
            }
        });
    }

    private void viewTickets(final Button search, final int journalId) {
        final ObservableList<TicketModel> tickets = ticket.getFilteredList(ticketModel -> ticketModel.getAttachedJournalId() == journalId);
        if (tickets.isEmpty()) {
            Alerts.showError(
                    "No tickets to display.",
                    "There are no tickets associated with this journal.",
                    "Please attach one and try again."
            );
            return;
        }

        final BorderPane content = new BorderPane();
        final ListView<TicketModel> listView = new ListView<>(tickets);
        listView.getStyleClass().add("ticket-list-view");

        content.setCenter(listView);

        final PopOverBuilder builder = PopOverBuilder.build()
                .useDefault()
                .withTitle("Associated Tickets")
                .withContent(content)
                .setOwner(search);
        configureListView(builder.getPopOver(), listView);
        builder.show();
    }

    private void configureListView(final PopOver popOver, final ListView<TicketModel> listView) {
        listView.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(TicketModel ticketModel, boolean b) {
                super.updateItem(ticketModel, b);
                if (b || ticketModel == null) {
                    setGraphic(null);
                    return;
                }

                final HBox hBox = new HBox();
                hBox.setSpacing(5.0);

                final Button view = new Button();
                view.setGraphic(FALoader.createDefault(FontAwesome.Glyph.EYE));
                view.setOnAction(event -> openTicketModel(popOver, ticketModel));

                final Text info = new Text(String.format("%s | Ticket ID: %s", ticketModel.getTitle(), ticketModel.getId()));
                info.setFill(Color.WHITE);
                info.setStyle("-fx-font-weight: bolder; -fx-font-size: 1.25em;");

                hBox.setAlignment(Pos.CENTER);
                hBox.setPadding(new Insets(10, 10, 10, 10));
                hBox.getChildren().addAll(view, info);

                setGraphic(hBox);
            }
        });
    }

    private void openTicketModel(final PopOver popOver, final TicketModel ticketModel) {
        popOver.hide();

        Display.close(Route.JOURNAL);
        Display.show(Route.VIEWER, DataRelay.of(ticketModel));
    }

    @FXML private void onAdd() {
        final PopOver popOver = new PopOver();
        final VBox box = new VBox();
        box.setPadding(new Insets(10, 10, 10, 10));
        box.setSpacing(5.0);
        box.setAlignment(Pos.CENTER_LEFT);

        final TextArea textArea = new TextArea();
        textArea.setWrapText(true);

        final Button button = new Button("Create");
        button.setOnAction(event -> createJournal(popOver, textArea.getText()));

        box.getChildren().addAll(textArea, button);
        popOver.setContentNode(box);
        popOver.show(addButton, 10.0);
    }

    private void createJournal(final PopOver popOver, final String text) {
        journal.createModel(new Journal()
                .setCreatedOn(DateUtil.now())
                .setNote(text)
        );

        journalList.refresh();
        popOver.hide();
    }

    @FXML private void onEdit() {
        final JournalModel journalModel = journalList.getSelectionModel().getSelectedItem();
        final PopOver popOver = new PopOver();

        final VBox box = new VBox();
        box.setSpacing(5.0);
        final TextArea textArea = new TextArea();
        textArea.setText(journalModel.getNoteProperty());
        textArea.setWrapText(true);

        final Button button = new Button("Update");
        button.setOnAction(event -> updateJournal(popOver, journalModel, textArea.getText()));

        box.getChildren().addAll(textArea, button);

        popOver.setContentNode(box);
        popOver.show(editButton, 10.0);
    }

    private void updateJournal(final PopOver popOver, final JournalModel model, final String newText) {
        model.setNoteProperty(newText);
        journal.update(model);
        journalList.refresh();
        popOver.hide();
    }

    @FXML private void onDelete() {
        final JournalModel journalModel = journalList.getSelectionModel().getSelectedItem();
        Alerts.showConfirmation(() -> journal.remove(journalModel.getId()), "Are you sure?", "This action cannot be undone.").ifPresent(type -> {
            if (type == ButtonType.YES) {
                journal.remove(journalModel.getId());
                journalList.refresh();
            }
        });
    }


}
