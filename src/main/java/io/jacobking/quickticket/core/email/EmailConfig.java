package io.jacobking.quickticket.core.email;

import io.jacobking.quickticket.gui.alert.Notify;
import io.jacobking.quickticket.tables.pojos.Email;

import java.util.Properties;

import static io.jacobking.quickticket.core.email.EmailConfig.EmailCommons.*;

public class EmailConfig {

    private static final String      DEFAULT_CONNECTION_TIMEOUT = "15000";
    private static final String      DEFAULT_TIMEOUT            = "60000";
    private static final EmailConfig instance                   = new EmailConfig();

    private final Properties properties;
    private       Email      email;

    private EmailConfig() {
        this.properties = new Properties();
    }

    public static EmailConfig getInstance() {
        return instance;
    }

    public EmailConfig applySettings() {
        if (this.email == null) {
            Notify.showError("Failed to apply settings.", "Could not apply e-mail settings.", "Passed e-mail is null in config.. ");
            return this;
        }
        apply();
        return this;
    }

    public boolean isConfigured() {
        return !email.getHost().isEmpty();
    }

    public Properties getProperties() {
        return properties;
    }

    public Email getEmail() {
        return email;
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
