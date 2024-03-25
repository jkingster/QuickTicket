package io.jacobking.quickticket.gui.alert;

import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.gui.alert.builder.AlertBuilder;
import io.jacobking.quickticket.gui.alert.builder.InputDialogBuilder;
import io.jacobking.quickticket.gui.model.impl.AlertSettingsModel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alerts {

    private static final AlertSettingsModel settings = BridgeContext.alertSettings().getModel(0);

    private Alerts() {

    }

    public static void showInfo(final String title, final String header, final String content) {
        if (settings.isDisableInfoAlertsProperty())
            return;

        new AlertBuilder(Alert.AlertType.INFORMATION)
                .withTitle(title)
                .withHeader(header)
                .withContent(content)
                .show();
    }

    public static void showError(final String title, final String header, final String content) {
        if (settings.isDisableErrorAlertsProperty())
            return;

        new AlertBuilder(Alert.AlertType.ERROR)
                .withTitle(title)
                .withHeader(header)
                .withContent(content)
                .show();
    }

    public static void showWarning(final String title, final String header, final String content) {
        if (settings.isDisableWarningAlertsProperty())
            return;

        new AlertBuilder(Alert.AlertType.WARNING)
                .withTitle(title)
                .withHeader(header)
                .withContent(content)
                .show();
    }

    // HANDLE THIS EXTERNALLY WHERE WE NEED CONFIRMATION. NOT INSIDE THE BASE METHOD.
    public static Optional<ButtonType> showConfirmation(final String title, final String header, final String content, final ButtonType... buttonTypes) {
        return new AlertBuilder(Alert.AlertType.CONFIRMATION)
                .withTitle(title)
                .withHeader(header)
                .withContent(content)
                .withButtons(buttonTypes)
                .showAndWait();
    }

    // HANDLE THIS EXTERNALLY WHERE WE NEED CONFIRMATION. NOT INSIDE THE BASE METHOD.
    public static Optional<ButtonType> showWarningConfirmation(final String title, final String header, final String content, final ButtonType... buttonTypes) {
        return new AlertBuilder(Alert.AlertType.WARNING)
                .withTitle(title)
                .withHeader(header)
                .withContent(content)
                .withButtons(buttonTypes)
                .showAndWait();
    }

    public static Optional<ButtonType> showWarningConfirmation(final String header, final String content) {
        return showConfirmation("WARNING!", header, content, ButtonType.YES, ButtonType.NO);
    }

    public static Optional<ButtonType> showConfirmation(final String header, final String content) {
        return showConfirmation("Are you sure?", header, content, ButtonType.YES, ButtonType.NO);
    }

    public static void showException(final String content, final Throwable throwable) {
        new AlertBuilder(Alert.AlertType.ERROR)
                .withException(content, throwable)
                .show();
    }

    public static Optional<String> showInput(final String title,  final String content, final String defaultValue) {
        return new InputDialogBuilder(defaultValue)
                .buildDialog(title, content)
                .result();
    }


}
