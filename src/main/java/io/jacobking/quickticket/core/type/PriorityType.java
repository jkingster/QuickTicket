package io.jacobking.quickticket.core.type;

public enum PriorityType {
    LOW, MEDIUM, HIGH;

    public static PriorityType of(final String target) {
        for (final PriorityType type : values()) {
            final String name = type.name().toLowerCase();
            if (target.equalsIgnoreCase(name))
                return type;
        }
        return null;
    }
}
