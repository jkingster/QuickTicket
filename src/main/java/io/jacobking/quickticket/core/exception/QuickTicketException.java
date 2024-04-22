package io.jacobking.quickticket.core.exception;

public class QuickTicketException extends RuntimeException {

    public QuickTicketException(final String message) {
        super(message);
    }

    public QuickTicketException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

}
