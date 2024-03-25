package io.jacobking.quickticket.gui.alert;

import io.jacobking.quickticket.gui.alert.builder.NotificationBuilder;

public class Notifications {

    private Notifications() {
    }

    private static NotificationBuilder buildDefault(final String title, final String content) {
        return new NotificationBuilder()
                .withTitle(title)
                .withContent(content);
    }

    public static void showInfo(final String title, final String content) {
        buildDefault(title, content).showInfo();
    }


    public static void show(final String title, final String content) {
        buildDefault(title, content).show();
    }

    public static void showWarning(final String title, final String content) {
        buildDefault(title, content).showWarning();
    }

    public static void showError(final String title, final String content) {
        buildDefault(title, content).showError();
    }

    public static void showConfirm(final String title, final String content) {
        buildDefault(title, content).showConfirm();
    }


}
