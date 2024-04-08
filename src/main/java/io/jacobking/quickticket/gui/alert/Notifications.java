package io.jacobking.quickticket.gui.alert;

import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.gui.alert.builder.NotificationBuilder;
import io.jacobking.quickticket.gui.model.impl.AlertSettingsModel;

public class Notifications {

    private static final AlertSettingsModel settings = Database.getInstance()
            .getBridgeContext()
            .getAlertSettings()
            .getModel(0);

    private Notifications() {
    }

    private static NotificationBuilder buildDefault(final String title, final String content) {
        return new NotificationBuilder()
                .withTitle(title)
                .withContent(content);
    }

    public static void showInfo(final String title, final String content) {
        if (settings.isDisableInfoNotificationProperty())
            return;
        buildDefault(title, content).showInfo();
    }


    public static void show(final String title, final String content) {
        buildDefault(title, content).show();
    }

    public static void showWarning(final String title, final String content) {
        if (settings.isDisableWarningNotificationProperty())
            return;
        buildDefault(title, content).showWarning();
    }

    public static void showError(final String title, final String content) {
        if (settings.isDisableErrorNotificationProperty())
            return;
        buildDefault(title, content).showError();
    }

    public static void showConfirm(final String title, final String content) {
        if (settings.isDisableConfirmationNotificationProperty())
            return;
        buildDefault(title, content).showConfirm();
    }


}
