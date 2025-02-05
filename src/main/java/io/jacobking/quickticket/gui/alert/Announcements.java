package io.jacobking.quickticket.gui.alert;

import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.core.utility.ImmutablePair;
import io.jacobking.quickticket.core.utility.Logs;
import io.jacobking.quickticket.gui.alert.builder.AlertBuilder;
import io.jacobking.quickticket.gui.alert.builder.InputDialogBuilder;
import io.jacobking.quickticket.gui.alert.builder.NotificationBuilder;
import io.jacobking.quickticket.gui.model.AlertModel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Map;
import java.util.Optional;

public class Announcements {

    /**
     * TODO: Rework the implementation of this class. Being in a global state I'm not fond of.
     *       Additionally the "announcement" should be encapsulated by a type (ALERT, NOTIFICATION, OVERRIDE?)
     */

    private static Announcements instance = null;

    private Map<String, AlertModel> alertMap;

    private Announcements() {
    }

    public void establishSettings(final BridgeContext bridgeContext) {
        this.alertMap = bridgeContext.getAlerts().getAlertMap();
    }

    public static Announcements get() {
        if (instance == null) {
            instance = new Announcements();
        }
        return instance;
    }

    public void showInfo(final String title, final String header, final String content) {
        if (!getAlertState("INFORMATIONAL_ALERTS"))
            return;

        new AlertBuilder(Alert.AlertType.INFORMATION)
                .withTitle(title)
                .withHeader(header)
                .withContent(content)
                .show();
    }

    public void showError(final String title, final String header, final String content) {
        if (!getAlertState("ERROR_ALERTS"))
            return;

        new AlertBuilder(Alert.AlertType.ERROR)
                .withTitle(title)
                .withHeader(header)
                .withContent(content)
                .show();
    }

    public void showWarning(final String title, final String header, final String content) {
        if (!getAlertState("WARNING_ALERTS"))
            return;

        new AlertBuilder(Alert.AlertType.WARNING)
                .withTitle(title)
                .withHeader(header)
                .withContent(content)
                .show();
    }

    // HANDLE THIS EXTERNALLY WHERE WE NEED CONFIRMATION. NOT INSIDE THE BASE METHOD.
    public Optional<ButtonType> showConfirmation(final Runnable preProcessing, final String title, final String header, final String content, final ButtonType... buttonTypes) {
        if (!getAlertState("CONFIRMATION_ALERTS")) {
            preProcessing.run();
            return Optional.empty();
        }

        return new AlertBuilder(Alert.AlertType.CONFIRMATION)
                .withTitle(title)
                .withHeader(header)
                .withContent(content)
                .withButtons(buttonTypes)
                .showAndWait();
    }

    // HANDLE THIS EXTERNALLY WHERE WE NEED CONFIRMATION. NOT INSIDE THE BASE METHOD.
    public Optional<ButtonType> showWarningConfirmation(final String title, final String header, final String content, final ButtonType... buttonTypes) {
        return new AlertBuilder(Alert.AlertType.WARNING)
                .withTitle(title)
                .withHeader(header)
                .withContent(content)
                .withButtons(buttonTypes)
                .showAndWait();
    }

    public Optional<ButtonType> showWarningConfirmation(final String header, final String content) {
        return showWarningConfirmation("WARNING!", header, content, ButtonType.YES, ButtonType.NO);
    }

    public Optional<ButtonType> showConfirmation(final Runnable runnable, final String header, final String content) {
        return showConfirmation(runnable, "Are you sure?", header, content, ButtonType.YES, ButtonType.NO);
    }

    public void showException(final String content, final Throwable throwable) {
        new AlertBuilder(Alert.AlertType.ERROR)
                .withException(content, throwable)
                .show();
    }

    public void showErrorOverride(final String title, final String content) {
        Logs.warn("Error: {} | {}", title, content);
        new AlertBuilder(Alert.AlertType.ERROR)
                .withTitle("Error")
                .withContent(content)
                .withHeader(title)
                .showAndWait();
    }

    public Optional<ImmutablePair<ButtonType, String>> showInput(final String title, final String content) {
        return new InputDialogBuilder()
                .buildDialog(title, content)
                .result();
    }


    private static NotificationBuilder buildDefault(final String title, final String content) {
        return new NotificationBuilder()
                .withTitle(title)
                .withContent(content);
    }

    public void showInfo(final String title, final String content) {
        if (!getAlertState("INFORMATION_NOTIFICATIONS"))
            return;
        buildDefault(title, content).showInfo();
    }


    public void show(final String title, final String content) {
        buildDefault(title, content).show();
    }

    public void showWarning(final String title, final String content) {
        if (!getAlertState("WARNING_NOTIFICATIONS"))
            return;
        buildDefault(title, content).showWarning();
    }

    public void showError(final String title, final String content) {
        if (!getAlertState("ERROR_NOTIFICATIONS"))
            return;
        buildDefault(title, content).showError();
    }

    public void showConfirm(final String title, final String content) {
        if (!getAlertState("CONFIRMATION_NOTIFICATIONS"))
            return;
        buildDefault(title, content).showConfirm();
    }

    private boolean getAlertState(final String alertName) {
        if (alertMap == null || alertMap.isEmpty())
            return false;

        final AlertModel model = alertMap.get(alertName);
        if (model == null)
            return false;

        return model.getAlertState();
    }

}
