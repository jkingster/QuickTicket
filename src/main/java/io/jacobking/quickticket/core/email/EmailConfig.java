package io.jacobking.quickticket.core.email;

import io.jacobking.quickticket.gui.alert.Alerts;
import io.jacobking.quickticket.tables.pojos.Email;

import javax.mail.Session;
import java.util.Properties;

import static io.jacobking.quickticket.core.email.EmailConfig.EmailCommons.*;

public class EmailConfig {

    private static final String      DEFAULT_CONNECTION_TIMEOUT = "15000";
    private static final String      DEFAULT_TIMEOUT            = "60000";
    private static       EmailConfig instance;

    private final Properties properties;
    private       Email      email;
    private       Session    session;

    private EmailConfig() {
        this.properties = new Properties();
    }

    public static synchronized EmailConfig getInstance() {
        if (instance == null) {
            instance = new EmailConfig();
        }
        return instance;
    }

    public EmailConfig applySettings() {
        if (this.email == null) {
            Alerts.showError("Failed to apply settings.", "Could not apply e-mail settings.", "Passed e-mail is null in config.. ");
            return this;
        }
        apply();
        return this;
    }

    public boolean isConfigured() {
        return !email.getHost().isEmpty();
    }

    public Email getEmail() {
        return email;
    }

    public Session getSession() {
        return session;
    }

    public EmailConfig setEmail(final Email email) {
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

        this.session = Session.getDefaultInstance(properties);
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
