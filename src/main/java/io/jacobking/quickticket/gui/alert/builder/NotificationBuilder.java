package io.jacobking.quickticket.gui.alert.builder;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.core.utility.Checks;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;

public class NotificationBuilder {

    private final Notifications notification;

    public NotificationBuilder() {
        this.notification = Notifications.create();
        notification.position(Pos.BOTTOM_RIGHT);
        notification.hideAfter(Duration.seconds(3));
    }

    public static String getStylesheet() {
        final URL url = App.class.getResource("css/core/notification.css");
        if (url == null)
            return null;
        return url.toExternalForm();
    }

    public NotificationBuilder withTitle(final String title) {
        Checks.notEmpty(title, "Notification Title");
        notification.title(title);
        return this;
    }

    public NotificationBuilder withContent(final String content) {
        Checks.notEmpty(content, "Notification Content");
        notification.text(content);
        return this;
    }

    public void show() {
        notification.show();
    }

    public void showInfo() {
        notification.showInformation();
    }

    public void showWarning() {
        notification.showWarning();
    }

    public void showError() {
        notification.showError();
    }


    public void showConfirm() {
        notification.showConfirm();
    }


}
