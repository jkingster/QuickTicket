package io.jacobking.quickticket.gui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class Model<E> {

    private final IntegerProperty idProperty = new SimpleIntegerProperty();

    public Model(final int id) {
        this.idProperty.setValue(id);
    }

    public int getId() {
        return idProperty.getValue();
    }

    public IntegerProperty getIdProperty() {
        return idProperty;
    }

    public abstract E toEntity();

}
