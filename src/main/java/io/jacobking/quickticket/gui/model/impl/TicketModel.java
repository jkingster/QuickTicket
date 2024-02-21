package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.gui.model.ViewModel;
import io.jacobking.quickticket.tables.pojos.Ticket;
import javafx.beans.property.*;

public class TicketModel extends ViewModel<Ticket> {

    private final StringProperty                titleProperty    = new SimpleStringProperty();
    private final ObjectProperty<StatusType>    statusProperty   = new SimpleObjectProperty<>();
    private final ObjectProperty<PriorityType> priorityProperty = new SimpleObjectProperty<>();
    private final IntegerProperty               employeeProperty = new SimpleIntegerProperty();
    private final StringProperty               createdProperty  = new SimpleStringProperty();

    public TicketModel(int id, String title, StatusType statusType, PriorityType priorityType, int employeeId, String created) {
        super(id);
        this.titleProperty.setValue(title);
        this.statusProperty.setValue(statusType);
        this.priorityProperty.setValue(priorityType);
        this.employeeProperty.setValue(employeeId);
        this.createdProperty.setValue(created);
    }

    public TicketModel(final Ticket ticket) {
        this(
                ticket.getId(),
                ticket.getTitle(),
                StatusType.of(ticket.getStatus()),
                PriorityType.of(ticket.getPriority()),
                ticket.getUserId(), // need to update from user -> employee eventually.
                ticket.getCreatedOn()
        );
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

    public IntegerProperty employeeProperty() {
        return employeeProperty;
    }

    public StringProperty createdProperty() {
        return createdProperty;
    }

    public String getStatus() {
        return statusProperty.getValue().name();
    }

    public String getPriority() {
        return priorityProperty.getValue().name();
    }

    public int getEmployeeId() {
        return employeeProperty.getValue();
    }

    @Override
    public String toString() {
        return "TicketModel{" +
                "titleProperty=" + titleProperty +
                ", statusProperty=" + statusProperty +
                ", priorityProperty=" + priorityProperty +
                ", userProperty=" + employeeProperty +
                ", createdProperty=" + createdProperty +
                '}';
    }


    @Override
    public Ticket toEntity() {
        return new Ticket(
                getId(),
                getTitle(),
                getStatus(),
                getPriority(),
                getCreation(),
                getEmployeeId()
        );
    }
}
