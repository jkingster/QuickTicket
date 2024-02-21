package io.jacobking.quickticket.core.email;

import io.jacobking.quickticket.gui.alert.Notify;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

public class EmailSender {

    private final Session session;
    private final String  fromEmail;

    public EmailSender(EmailConfig emailConfig) {
        this.session = Session.getDefaultInstance(emailConfig.getProperties());
        this.fromEmail = emailConfig.getEmail().getFromAddress();
    }

    public void sendEmail(final String subject, final String recipient, final String body) {
        try {
            System.out.println(fromEmail);
            System.out.println(recipient);
            final MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addHeader("Content-Type", "text/HTML; charset=UTF-8");
            mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");
            mimeMessage.setFrom(new InternetAddress(fromEmail));
            mimeMessage.setSubject(subject, "UTF-8");
            mimeMessage.setSentDate(new Date());
            mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipient));
            mimeMessage.setContent(body, "text/html; charset=utf-8");
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            Notify.showException("Failed to send e-mail.", e.fillInStackTrace());
        }
    }

}
