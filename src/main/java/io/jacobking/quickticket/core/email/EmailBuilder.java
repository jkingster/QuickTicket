package io.jacobking.quickticket.core.email;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.gui.alert.Announcements;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EmailBuilder {

    private final String targetAddress;
    private       String resolvedHTML;

    public EmailBuilder(final String targetAddress, final EmailType emailType) {
        this.targetAddress = targetAddress;
        this.resolvedHTML = EmailType.getContent(emailType);
    }

    public EmailBuilder format(final Object... objects) {
        if (resolvedHTML.isEmpty())
            return this;

        if (objects.length == 0)
            return this;

        this.resolvedHTML = resolvedHTML.formatted(objects);
        return this;
    }

    public EmailSender email(final EmailConfig emailConfig) {
        return new EmailSender(emailConfig, targetAddress, resolvedHTML);
    }

    public enum EmailType {
        RESOLVED("resolved.html"), NEW_TICKET("new_ticket.html"), TEST("");

        private static final String RESOLVED_STRING   = getHTMLAsString(EmailType.RESOLVED);
        private static final String NEW_TICKET_STRING = getHTMLAsString(EmailType.NEW_TICKET);

        private final String path;

        EmailType(final String path) {
            this.path = path;
        }

        public static String getContent(final EmailType emailType) {
            return switch (emailType) {
                case RESOLVED -> RESOLVED_STRING;
                case NEW_TICKET -> NEW_TICKET_STRING;
                case TEST -> "This is a test e-mail that can be ignored.";
            };
        }

        private static String getHTMLAsString(final EmailType emailType) {
            final String path = emailType.path;
            try (final InputStream inputStream = App.class.getResourceAsStream("html/" + path)) {
                if (inputStream == null)
                    return "";

                return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            } catch (IOException e) {
                Announcements.get().showException("Failed to parse html.", e.fillInStackTrace());
                return "";
            }
        }
    }

    @Override public String toString() {
        return "EmailBuilder{" +
                "targetAddress='" + targetAddress + '\'' +
                ", resolvedHTML='" + resolvedHTML + '\'' +
                '}';
    }
}
