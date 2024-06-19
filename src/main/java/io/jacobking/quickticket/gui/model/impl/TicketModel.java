package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.gui.model.ViewModel;
import io.jacobking.quickticket.tables.pojos.Ticket;
import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TicketModel extends ViewModel<Ticket> {

    private final StringProperty                titleProperty       = new SimpleStringProperty();
    private final ObjectProperty<StatusType>    statusProperty      = new SimpleObjectProperty<>();
    private final ObjectProperty<PriorityType>  priorityProperty    = new SimpleObjectProperty<>();
    private final IntegerProperty               employeeProperty    = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDateTime> createdProperty     = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> lastViewedTimestamp = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate>     resolveBy           = new SimpleObjectProperty<>();

    public TicketModel(int id, String title, StatusType statusType, PriorityType priorityType, int employeeId, LocalDateTime created, LocalDateTime lastViewedTimestamp, LocalDate resolveBy) {
        super(id);
        this.titleProperty.setValue(title);
        this.statusProperty.setValue(statusType);
        this.priorityProperty.setValue(priorityType);
        this.employeeProperty.setValue(employeeId);
        this.createdProperty.setValue(created);
        this.lastViewedTimestamp.setValue(lastViewedTimestamp);
        this.resolveBy.setValue(resolveBy);
    }

    public TicketModel(final Ticket ticket) {
        this(
                ticket.getId(),
                ticket.getTitle(),
                StatusType.of(ticket.getStatus()),
                PriorityType.of(ticket.getPriority()),
                ticket.getEmployeeId(), // need to update from user -> employee eventually.
                ticket.getCreatedOn(),
                ticket.getLastOpenedTimestamp(),
                ticket.getResolvedBy()
        );
    }

    public String getTitle() {
        return titleProperty.getValueSafe();
    }

    public LocalDateTime getCreation() {
        return createdProperty.getValue();
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

    public ObjectProperty<LocalDateTime> createdProperty() {
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

    public ObjectProperty<LocalDateTime> lastViewedProperty() {
        return lastViewedTimestamp;
    }

    public LocalDateTime getLastViewedTimestamp() {
        return lastViewedTimestamp.getValue();
    }

    public LocalDate getResolveBy() {
        return resolveBy.get();
    }

    public ObjectProperty<LocalDate> resolveByProperty() {
        return resolveBy;
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
                getEmployeeId(),
                getLastViewedTimestamp(),
                getResolveBy()
        );
    }
}
