package io.jacobking.quickticket.core.type;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;

public enum StatusType implements Serializable {
    OPEN,
    ACTIVE,
    PAUSED,
    RESOLVED,
    UNDEFINED;

    public static StatusType of(final String target) {
        for (final StatusType type : values()) {
            final String name = type.name().toLowerCase();
            if (name.equalsIgnoreCase(target))
                return type;
        }
        return null;
    }

    public static ObservableList<StatusType> asObservableList() {
        return FXCollections.observableArrayList(values());
    }
}
