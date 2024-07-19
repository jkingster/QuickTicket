package io.jacobking.quickticket.gui.utility;

import io.jacobking.quickticket.core.utility.Checks;
import io.jacobking.quickticket.gui.alert.Announcements;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MiscUtil {

    private MiscUtil() {

    }

    public static void openLink(final String link) {
        Checks.notEmpty(link, "URL/Link");
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(link));
            }
        } catch (IOException | URISyntaxException e) {
            Announcements.get().showException("Failed to open link.", e.fillInStackTrace());
        }
    }

}
