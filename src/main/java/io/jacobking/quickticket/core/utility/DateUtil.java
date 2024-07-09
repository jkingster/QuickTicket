package io.jacobking.quickticket.core.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public enum DateFormat {
        DATE_ONE("MM/dd/yyyy"),
        DATE_TWO("MM-dd-yyyy"),
        DATE_THREE("MMddyyyy"),
        DATE_FOUR("yyyy-MM-dd"),
        DATE_TIME_ONE("MM/dd/yyyy HH:mm:ss a"),
        DATE_TIME_TWO("MM-dd-yyyy_HH-mm-ss"),
        DATE_TIME_EXTENDED("MM-dd-yyyy HH:mm:ss.SSS");

        private final DateTimeFormatter formatter;
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

        public DateTimeFormatter getFormatter() {
            return formatter;
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

    public static String formatDate(final String inputDate) {
        final LocalDate localDate = LocalDate.parse(inputDate, DateFormat.DATE_FOUR.getFormatter());
        return DateFormat.DATE_ONE.getFormatter().format(localDate);
    }

    public static String formatDate(final DateFormat dateFormat, final String inputDate) {
        final LocalDate localDate = LocalDate.parse(inputDate, dateFormat.formatter);
        return dateFormat.getFormatter().format(localDate);
    }
}
