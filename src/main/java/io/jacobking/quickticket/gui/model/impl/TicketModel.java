package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.gui.model.ViewModel;
import io.jacobking.quickticket.tables.pojos.Ticket;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TicketModel extends ViewModel<Ticket> {

    private final StringProperty titleProperty = new SimpleStringProperty();
    private final ObjectProperty<StatusType> statusProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<PriorityType> priorityProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<UserModel> userProperty = new SimpleObjectProperty<>();
    private final StringProperty createdProperty = new SimpleStringProperty();

    public TicketModel(int id, String title, StatusType statusType, PriorityType priorityType, UserModel userModel, String created) {
        super(id);
        this.titleProperty.setValue(title);
        this.statusProperty.setValue(statusType);
        this.priorityProperty.setValue(priorityType);
        this.userProperty.setValue(userModel);
        this.createdProperty.setValue(created);
    }

    public String getTitle() {
        return titleProperty.getValueSafe();
    }

    public String getCreation() {
        return createdProperty.getValueSafe();
    }

    public StringProperty titleProperty() {
        return titleProperty;
    }

    public ObjectProperty<StatusType> statusProperty() {
        return statusProperty;
    }

    public ObjectProperty<PriorityType> priorityProperty() {
        return priorityProperty;
    }

    public ObjectProperty<UserModel> userProperty() {
        return userProperty;
    }

    public StringProperty createdProperty() {
        return createdProperty;
    }

    @Override
    public String toString() {
        return "TicketModel{" +
                "titleProperty=" + titleProperty +
                ", statusProperty=" + statusProperty +
                ", priorityProperty=" + priorityProperty +
                ", userProperty=" + userProperty +
                ", createdProperty=" + createdProperty +
                '}';
    }
}
