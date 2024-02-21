package io.jacobking.quickticket.core.type;

public enum StatusType {
    OPEN,
    ACTIVE,
    PAUSED,
    RESOLVED;

    public static StatusType of(final String target) {
        for (final StatusType type : values()) {
            final String name = type.name().toLowerCase();
            if (name.equalsIgnoreCase(target))
                return type;
        }
        return null;
    }
}
