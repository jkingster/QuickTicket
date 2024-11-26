package io.jacobking.quickticket.core.type;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;

public enum PriorityType implements Serializable {
    LOW, MEDIUM, HIGH;

    public static PriorityType of(final String target) {
        for (final PriorityType type : values()) {
            final String name = type.name().toLowerCase();
            if (target.equalsIgnoreCase(name))
                return type;
        }
        return null;
    }

    public static ObservableList<PriorityType> asObservableList() {
        return FXCollections.observableArrayList(values());
    }
}
