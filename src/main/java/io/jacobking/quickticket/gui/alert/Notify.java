package io.jacobking.quickticket.gui.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Notify {

    private Notify() {

    }

    public static void showInfo(final String title, final String header, final String content) {
        new AlertBuilder(Alert.AlertType.INFORMATION)
                .withTitle(title)
                .withHeader(header)
                .withContent(content)
                .show();
    }

    public static void showError(final String title, final String header, final String content) {
        new AlertBuilder(Alert.AlertType.ERROR)
                .withTitle(title)
                .withHeader(header)
                .withContent(content)
                .show();
    }

    public static void showError(final String title, final String header, final String content, final ButtonType... buttonTypes) {
        new AlertBuilder(Alert.AlertType.ERROR)
                .withTitle(title)
                .withHeader(header)
                .withContent(content)
                .withButtons(buttonTypes)
                .showAndWait();
    }

    public static void showWarning(final String title, final String header, final String content) {
        new AlertBuilder(Alert.AlertType.WARNING)
                .withTitle(title)
                .withHeader(header)
                .withContent(content)
                .show();
    }

    public static Optional<ButtonType> showConfirmation(final String title, final String header, final String content, final ButtonType... buttonTypes) {
        return new AlertBuilder(Alert.AlertType.CONFIRMATION)
                .withTitle(title)
                .withHeader(header)
                .withContent(content)
                .withButtons(buttonTypes)
                .showAndWait();
    }

    public static Optional<ButtonType> showWarningConfirmation(final String title, final String header, final String content, final ButtonType... buttonTypes) {
        return new AlertBuilder(Alert.AlertType.WARNING)
                .withTitle(title)
                .withHeader(header)
                .withContent(content)
                .withButtons(buttonTypes)
                .showAndWait();
    }

    public static Optional<ButtonType> showConfirmation(final String header, final String content) {
        return showConfirmation("Are you sure?", header, content, ButtonType.YES, ButtonType.NO);
    }

    public static Optional<ButtonType> showWarningConfirmation(final String header, final String content) {
        return showWarningConfirmation("WARNING!", header, content, ButtonType.YES, ButtonType.NO);
    }

    public static void showException(final String content, final Throwable throwable) {
        new AlertBuilder(Alert.AlertType.ERROR)
                .withException(content, throwable)
                .show();
    }

    public static Optional<String> showInput(final String title, final String content, final String defaultValue) {
        return new InputDialogBuilder(defaultValue)
                .buildDialog(title, content)
                .result();
    }


}
