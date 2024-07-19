package io.jacobking.quickticket.core.email;

import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.gui.alert.Alerts;
import io.jacobking.quickticket.tables.pojos.EmailInterface;

import javax.mail.Session;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import static io.jacobking.quickticket.core.email.EmailConfig.EmailCommons.*;

public class EmailConfig {

    private static final String DEFAULT_CONNECTION_TIMEOUT = "15000";
    private static final String DEFAULT_TIMEOUT            = "60000";

    private final Properties     properties;
    private       EmailInterface email;
    private       Session        session;

    public EmailConfig() {
        this.properties = new Properties();
    }

    public EmailConfig applySettings() {
        if (this.email == null) {
            Alerts.get().showError("Failed to apply settings.", "Could not apply e-mail settings.", "Passed e-mail is null in config.. ");
            return this;
        }
        apply();
        return this;
    }

    public boolean isConfigured() {
        return !email.getHost().isEmpty();
    }

    public EmailInterface getEmail() {
        return email;
    }

    public Session getSession() {
        return session;
    }

    public EmailConfig setEmail(final EmailInterface email) {
        this.email = email;
        return this;
    }

    private void apply() {
        final String host = email.getHost();
        properties.setProperty(EmailCommons.SMTP_HOST, host);

        final String port = email.getPort();
        properties.setProperty(EmailCommons.SMTP_PORT, port.isEmpty() ? DEFAULT_PORT : port);

        if (email.getOverSslOrTls()) {
            properties.setProperty(EmailCommons.SSL_ENABLE, "true");
        }

        if (email.getStarttls()) {
            properties.setProperty(EmailCommons.STARTTLS_ENABLE, "true");
        }

        if (email.getAuthentication()) {
            properties.setProperty(EmailCommons.AUTH, "true");
        }

        properties.setProperty(SMTP_CONNECTION_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT);
        properties.setProperty(SMTP_TIMEOUT, DEFAULT_TIMEOUT);

        properties.setProperty("mail.debug", booleanAsString(email.getDebugging()));
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        this.session = Session.getDefaultInstance(properties);

        if (email.getDebugging() != null && email.getDebugging()) {
            configureDebugLogs();
        }
    }

    private String booleanAsString(final Boolean state) {
        if (state == null) {
            return "false";
        }
        return state ? "true" : "false";
    }

    private void configureDebugLogs() {
        try {
            final String fileName = String.format("mail-debug-%s.log", DateUtil.nowAsString(DateUtil.DateFormat.DATE_TIME_TWO));
            final FileOutputStream output = new FileOutputStream(FileIO.getPath("logs") + File.separator + fileName);
            final PrintStream printStream = new PrintStream(output);
            session.setDebugOut(printStream);
        } catch (IOException e) {
            Alerts.get().showException("Configuring Debug Logs", e.fillInStackTrace());
        }
    }

    static class EmailCommons {
        static final String SMTP_HOST = "mail.smtp.host";
        static final String SMTP_PORT = "mail.smtp.port";

        static final String SMTP_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";
        static final String SMTP_TIMEOUT            = "mail.smtp.timeout";
        static final String STARTTLS_ENABLE         = "mail.smtp.starttls.enable";

        static final String SSL_ENABLE = "mail.smtp.ssl.enable";
        static final String AUTH       = "mail.smtp.auth";

        static final String DEFAULT_PORT = "25";
    }
}
