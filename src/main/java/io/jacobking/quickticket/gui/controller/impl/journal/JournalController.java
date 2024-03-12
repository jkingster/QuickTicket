package io.jacobking.quickticket.gui.controller.impl.journal;

import io.jacobking.quickticket.gui.alert.Notify;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.JournalModel;
import io.jacobking.quickticket.gui.utility.FALoader;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.controlsfx.glyphfont.FontAwesome;

import java.net.URL;
import java.util.ResourceBundle;

public class JournalController extends Controller {
    private static final int COMMENT_OFFSET = 5;

    @FXML private ListView<JournalModel> journalList;
    @FXML private Button deleteButton;
    @FXML private Button editButton;

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

                final BorderPane borderPane = new BorderPane();
                final HBox topBox = new HBox();
                topBox.setSpacing(journalList.getWidth() - 130);

                final Text date = new Text(journalModel.getCreatedOnProperty());
                date.setStyle("-fx-font-weight: bolder; -fx-font-size: 1.25em;");
                date.setFill(Color.WHITE);

                final Button search = new Button();
                search.setGraphic(FALoader.createDefault(FontAwesome.Glyph.SEARCH));
                topBox.getChildren().addAll(date, search);
                borderPane.setTop(topBox);

                final Text comment = new Text(journalModel.getNoteProperty());
                comment.setFill(Color.WHITE);
                comment.setWrappingWidth(journalList.getWidth() - date.getBoundsInLocal().getWidth());
                borderPane.setLeft(comment);

                setGraphic(borderPane);
            }
        });
    }

    @FXML private void onAdd() {

    }

    @FXML private void onEdit() {

    }

    @FXML private void onDelete() {
        final JournalModel journalModel = journalList.getSelectionModel().getSelectedItem();
        Notify.showConfirmation("Are you sure?", "This action cannot be undone.").ifPresent(type -> {
            if (type == ButtonType.YES) {
                journal.remove(journalModel.getId());
                journalList.refresh();
            }
        });
    }
}
