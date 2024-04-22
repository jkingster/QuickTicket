package io.jacobking.quickticket.gui.alert.builder;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.core.utility.Checks;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Optional;

public class AlertBuilder {

    private final Alert alert;

    public AlertBuilder(final Alert.AlertType alertType) {
        this.alert = new Alert(alertType);
        initializeStyle();
    }

    public AlertBuilder withException(final String content, final Throwable throwable) {
        Checks.notEmpty(content, "Alert Exception Content");
        this.alert.setContentText(content);
        setExceptionGraphic(throwable);
        return this;
    }

    public AlertBuilder withTitle(final String title) {
        Checks.notEmpty(title, "Alert Title");
        this.alert.setTitle(title);
        return this;
    }

    public AlertBuilder withHeader(final String header) {
        Checks.notEmpty(header, "Alert Header");
        this.alert.setHeaderText(header);
        return this;
    }

    public AlertBuilder withContent(final String content) {
        Checks.notEmpty(content, "Alert Content");
        this.alert.setContentText(content);
        return this;
    }

    public AlertBuilder withButtons(final ButtonType... buttonTypes) {
        if (buttonTypes.length == 0)
            return this;

        this.alert.getButtonTypes().clear();
        this.alert.getButtonTypes().addAll(buttonTypes);
        return this;
    }

    public void show() {
        this.alert.show();
    }

    public Optional<ButtonType> showAndWait() {
        return this.alert.showAndWait();
    }

    private void initializeStyle() {
        final URL stylesheet = App.class.getResource("css/core/dialog.css");
        if (stylesheet == null)
            return;

        final String externalForm = stylesheet.toExternalForm();
        alert.getDialogPane().getStylesheets().add(externalForm);
    }

    // thanks to http://www.java2s.com/example/java/javafx/show-javafx-exception-dialog.html, modified a little bit
    private void setExceptionGraphic(final Throwable throwable) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);

        final String exception = stringWriter.toString();

        final TextArea textArea = new TextArea(exception);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        final Pane exceptionHeaderPane = new Pane();
        final Label exceptionHeader = new Label("Exception Stacktrace: ");
        exceptionHeaderPane.getChildren().add(exceptionHeader);

        final GridPane pane = new GridPane();
        pane.setMaxHeight(Double.MAX_VALUE);
        pane.add(exceptionHeaderPane, 0, 0);
        pane.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(pane);
        alert.getDialogPane().setExpanded(true);
    }

}
