package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.core.database.entity.impl.UserEntity;
import io.jacobking.quickticket.gui.model.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserModel extends ViewModel<UserEntity> {

    private final StringProperty fullNameProperty = new SimpleStringProperty();

    public UserModel(int id, String fullName) {
        super(id);
        this.fullNameProperty.setValue(fullName);
    }

    public void setFullName(final String fullName) {
        this.fullNameProperty.setValue(fullName);
    }

    public StringProperty nameProperty() {
        return fullNameProperty;
    }

    @Override
    public String toString() {
        return fullNameProperty.getValueSafe();
    }
}
