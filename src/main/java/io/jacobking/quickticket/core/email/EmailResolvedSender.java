package io.jacobking.quickticket.core.email;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.gui.alert.Notify;
import io.jacobking.quickticket.gui.model.impl.EmployeeModel;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EmailResolvedSender {

    private static final String RESOLVED_CONTENTS = getResolvedContents();

    private final String ticketId;
    private final String ticketSubject;
    private final String ticketCreation;
    private final String ticketEmployeeName;
    private final String ticketEmployeeEmail;
    private final String resolvedComment;

    public EmailResolvedSender(final TicketModel ticketModel, final EmployeeModel employeeModel, final String comment) {
        this.ticketId = String.valueOf(ticketModel.getId());
        this.ticketSubject = ticketModel.getTitle();
        this.ticketCreation = ticketModel.getCreation().format(DateUtil.DATE_TIME_FORMATTER);
        this.ticketEmployeeName = employeeModel.getFullName();
        this.ticketEmployeeEmail = employeeModel.getEmail();
        this.resolvedComment = comment;
    }

    public void sendEmail() {
        final EmailSender emailSender = new EmailSender(EmailConfig.getInstance());
        final String subject = String.format("Ticket Resolved (Ticket ID: %s) | %s", ticketId, ticketSubject);
        final String builtContent = RESOLVED_CONTENTS.formatted(
                ticketId,
                ticketSubject,
                ticketCreation,
                ticketEmployeeName,
                resolvedComment
        );
        emailSender.sendEmail(subject, ticketEmployeeEmail, builtContent);
    }

    private static String getResolvedContents() {
        try (final InputStream inputStream = App.class.getResourceAsStream("html/resolved.html")) {
            if (inputStream == null)
                return "";

            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Notify.showException("Failed to parse html.", e.fillInStackTrace());
            return "";
        }
    }

}
