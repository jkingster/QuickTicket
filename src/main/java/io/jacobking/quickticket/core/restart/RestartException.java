package io.jacobking.quickticket.core.restart;

public class RestartException extends RuntimeException{

    public RestartException(final String message) {
        super(message);
    }

    public RestartException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

}
