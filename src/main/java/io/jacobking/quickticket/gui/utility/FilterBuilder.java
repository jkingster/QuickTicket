package io.jacobking.quickticket.gui.utility;

import java.util.Arrays;

public class FilterBuilder {

    private boolean[] filters;

    private FilterBuilder(boolean... filters) {
        this.filters = Arrays.copyOf(filters, filters.length);
    }

}
