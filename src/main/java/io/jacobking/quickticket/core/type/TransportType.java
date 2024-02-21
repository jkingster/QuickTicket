package io.jacobking.quickticket.core.type;

public enum TransportType {

    SSL_OR_TSL,
    STARTTLS;

    public static TransportType of(final String target) {
        for (final TransportType type : values()) {
            final String name = type.name().toLowerCase();
            if (name.equalsIgnoreCase(target))
                return type;
        }
        return null;
    }
}
