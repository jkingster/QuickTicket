package io.jacobking.quickticket.core.email;

import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.utility.Checks;
import io.jacobking.quickticket.core.utility.Logs;
import io.jacobking.quickticket.gui.alert.AlertPopup;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

public class EmailSender {

    private final EmailConfig emailConfig;
    private final Session     session;
    private final String      targetAddress;
    private final String      resolvedContents;

    private String subject;

    public EmailSender(final EmailConfig emailConfig, final String targetAddress, final String resolvedContents) {
        this.emailConfig = emailConfig;
        this.session = emailConfig.getSession();
        this.targetAddress = targetAddress;
        this.resolvedContents = resolvedContents;
    }

    public EmailSender setSubject(final String subject) {
        Checks.notEmpty(subject, "E-mail Subject");
        this.subject = subject;
        return this;
    }

    public void sendEmail() {
        if (!emailConfig.isConfigured()) {
            AlertPopup.get().showError("Could not send e-mail.", "Your SMTP information is not configured.", "Please check settings.");
            return;
        }

        if (subject.isEmpty()) {
            AlertPopup.get().showError(
                    "Could not send e-mail",
                    "No subject was set.",
                    "Please set one and try again."
            );
            return;
        }

        QuickTicket.execute(() -> {
            try {
                final MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.addHeader("Content-Type", "text/HTML; charset=UTF-8");
                mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");
                mimeMessage.setFrom(new InternetAddress(emailConfig.getEmail().getFromAddress()));
                mimeMessage.setSubject(subject, "UTF-8");
                mimeMessage.setSentDate(new Date());
                mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(targetAddress));
                final String bcc = emailConfig.getEmail().getBccAddress();
                if (bcc != null && !bcc.isEmpty()) {
                    mimeMessage.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(bcc));
                }

                mimeMessage.setContent(resolvedContents, "text/html; charset=utf-8");
                Transport.send(mimeMessage);
            } catch (MessagingException e) {
                Logs.warn(e.fillInStackTrace().getMessage());
            }
        });
    }


}
