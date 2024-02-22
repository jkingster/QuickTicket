package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.utility.ChangelogReader;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.utility.StyleCommons;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangelogController extends Controller {
    @FXML private ListView<ChangelogReader.Changelog> changeList;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        changeList.setItems(FXCollections.observableArrayList(ChangelogReader.getChanges()));
        changeList.setCellFactory(data -> new ListCell<>() {
            @Override protected void updateItem(ChangelogReader.Changelog changelog, boolean b) {
                super.updateItem(changelog, b);
                if (changelog == null || b) {
                    setGraphic(null);
                    return;
                }

                final VBox box = new VBox();
                VBox.setVgrow(box, Priority.ALWAYS);
                box.setSpacing(2.0);

                final Label header = new Label(changelog.getVersion());
                header.setStyle("-fx-font-weight: bolder; -fx-text-fill: #5DADD5;");

                box.getChildren().add(header);

                for (final String change : changelog.getChanges()) {
                    final Text changeText = new Text("- "+change);
                    changeText.setWrappingWidth(changeList.getWidth() - header.getBoundsInLocal().getWidth() - 50);
                    changeText.setFill(Color.WHITE);
                    changeText.setStyle("-fx-font-weight: bolder;");
                    box.getChildren().add(changeText);
                }

                setGraphic(box);
            }
        });
    }
}
