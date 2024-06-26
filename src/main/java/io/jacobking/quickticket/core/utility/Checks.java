package io.jacobking.quickticket.core.utility;

public final class Checks {
    private Checks() {

    }

    public static void notNull(final Object object, final String name) {
        if (object == null) {
            throw new IllegalArgumentException(name + " cannot be null!");
        }
    }

    public static void notEmpty(final String string, final String name) {
        notNull(string, name);
        if (string.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be empty!");
        }
    }

    public static void ifNotNullRun(final Runnable runnable, final Object... nullableObjects) {
        for (final Object object : nullableObjects) {
            if (object == null)
                return;
        }
        runnable.run();
    }
}
