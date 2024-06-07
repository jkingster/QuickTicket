package io.jacobking.quickticket.gui.alert.builder;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.core.utility.Checks;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Optional;

public class InputDialogBuilder {

    private final Dialog<String> dialog;

    public InputDialogBuilder() {
        this.dialog = new Dialog<>();
        initializeStyle();
    }

    public InputDialogBuilder buildDialog(final String title, final String content) {
        Checks.notEmpty(title, "Dialog Title");
        Checks.notEmpty(content, "Dialog Content");
        this.dialog.setTitle(title);
        this.dialog.setContentText(content);

        final Text text = new Text(content);
        text.setStyle("-fx-font-weight: bolder; -fx-font-size: 1.25em;");
        text.setFill(Paint.valueOf("5DADD5"));
        final TextArea textArea = new TextArea();
        textArea.setWrapText(true);

        final VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5.0);
        vBox.getChildren().addAll(text, textArea);

        this.dialog.getDialogPane().setContent(vBox);
        this.dialog.getDialogPane().getButtonTypes().clear();
        this.dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

        dialog.setResultConverter(type -> {
            if (type == ButtonType.YES) {
                return textArea.getText();
            } else if (type == ButtonType.NO) {
                final String comment = textArea.getText();
                return comment.isEmpty() ? "" : comment;
            }
            return "";
        });
        return this;
    }

    public Optional<String> result() {
        return dialog.showAndWait();
    }

    private void initializeStyle() {
        final URL stylesheet = App.class.getResource("css/core/dialog.css");
        if (stylesheet == null)
            return;

        final String externalForm = stylesheet.toExternalForm();
        dialog.getDialogPane().getStylesheets().add(externalForm);
    }

}
