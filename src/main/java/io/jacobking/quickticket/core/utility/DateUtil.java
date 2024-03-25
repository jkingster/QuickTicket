package io.jacobking.quickticket.core.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter DATE_FORMAT         = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public static final  DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm a");
    public static final DateTimeFormatter DATE_TIME_FORMATTER_TWO = DateTimeFormatter.ofPattern("MMddyyyy-HHmm-a");

    private DateUtil() {
    }

    public static String now() {
        return LocalDate.now().format(DATE_FORMAT);
    }

    public static LocalDateTime nowWithTime() {
        final String localDateTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        return LocalDateTime.parse(localDateTime, DATE_TIME_FORMATTER);
    }

    public static String parseAsString(final LocalDateTime localDateTime) {
        return localDateTime.format(DATE_TIME_FORMATTER);
    }
}
