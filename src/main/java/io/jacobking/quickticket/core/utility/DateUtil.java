package io.jacobking.quickticket.core.utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class DateUtil {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private DateUtil() {}

    public static String now() {
        return LocalDate.now().format(DATE_FORMAT);
    }
}
