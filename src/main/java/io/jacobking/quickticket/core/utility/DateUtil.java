package io.jacobking.quickticket.core.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm a");

    private DateUtil() {
    }

    public static String now() {
        return LocalDate.now().format(DATE_FORMAT);
    }

    public static String nowWithTime() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }
}
