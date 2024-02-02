package io.jacobking.quickticket.core.utility;

public final class Checks {
    private Checks() {

    }

    public static void notNull(final Object object, final String name) {
        if (object == null) {
            throw new IllegalArgumentException(name + " cannot be null!");
        }
    }

}
