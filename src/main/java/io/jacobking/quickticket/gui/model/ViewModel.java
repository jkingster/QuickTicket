package io.jacobking.quickticket.gui.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ViewModel<T> {

    private final IntegerProperty idProperty = new SimpleIntegerProperty();

    public ViewModel(final int id) {
        this.idProperty.setValue(id);
    }

    public int getId() {
        return idProperty.getValue();
    }

    public IntegerProperty getIdProperty() {
        return idProperty;
    }
}
