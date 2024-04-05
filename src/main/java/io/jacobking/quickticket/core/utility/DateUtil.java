package io.jacobking.quickticket.core.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public enum DateFormat {
        DATE("MM-dd-yyyy"),
        DATE_TWO("MMddyyyy"),
        DATE_TIME_ONE("MM/dd/yyyy HH:mm:ss a"),
        DATE_TIME_TWO("MM-dd-yyyy_HH-mm-ss"),
        DATE_TIME_EXTENDED("MM-dd-yyyy HH:mm:ss.SSS");

        private       DateTimeFormatter formatter;
        private final String            pattern;

        DateFormat(String pattern) {
            this.pattern = pattern;
            this.formatter = DateTimeFormatter.ofPattern(pattern);
        }

        public String nowAsString() {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
        }

        public LocalDateTime nowAsLocalDateTime() {
            final String stringFormat = nowAsString();
            return LocalDateTime.parse(stringFormat, formatter);
        }
    }

    private DateUtil() {
    }

    public static String nowAsString(final DateFormat dateFormat) {
        return dateFormat.nowAsString();
    }

    public static LocalDateTime nowAsLocalDateTime(final DateFormat dateFormat) {
        return dateFormat.nowAsLocalDateTime();
    }

    public static String formatDateTime(final DateFormat dateFormat, final LocalDateTime localDateTime) {
        return localDateTime.format(dateFormat.formatter);
    }

    public static String formatDateTime(final DateFormat dateFormat, final String string) {
        return LocalDateTime.parse(string, dateFormat.formatter).toString();
    }
}
