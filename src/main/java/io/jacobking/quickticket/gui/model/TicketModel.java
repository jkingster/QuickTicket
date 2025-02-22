package io.jacobking.quickticket.gui.model;

import io.jacobking.quickticket.core.type.PriorityType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.gui.Model;
import io.jacobking.quickticket.tables.pojos.Ticket;
import javafx.beans.property.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class TicketModel extends Model<Ticket> implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    private final StringProperty                titleProperty     = new SimpleStringProperty();
    private final ObjectProperty<StatusType>    statusProperty    = new SimpleObjectProperty<>();
    private final ObjectProperty<PriorityType>  priorityProperty  = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> createdProperty   = new SimpleObjectProperty<>();
    private final IntegerProperty               categoryProperty  = new SimpleIntegerProperty();
    private final ObjectProperty<StatusType>    oldStatusProperty = new SimpleObjectProperty<>();

    public TicketModel(int id, String title, StatusType statusType, PriorityType priorityType, LocalDateTime created, int categoryId) {
        super(id);
        this.titleProperty.setValue(title);
        this.statusProperty.setValue(statusType);
        this.priorityProperty.setValue(priorityType);
        this.createdProperty.setValue(created);
        this.categoryProperty.setValue(categoryId);
    }

    public TicketModel(final Ticket ticket) {
        this(
                ticket.getId(),
                ticket.getTitle(),
                StatusType.of(ticket.getStatus()),
                PriorityType.of(ticket.getPriority()),// need to update from user -> employee eventually.
                ticket.getCreatedOn(),
                ticket.getCategoryId()
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


    public ObjectProperty<LocalDateTime> createdProperty() {
        return createdProperty;
    }

    public String getStatus() {
        return statusProperty.getValue().name();
    }

    public String getPriority() {
        return priorityProperty.getValue().name();
    }


    public int getCategory() {
        return categoryProperty.get();
    }

    public IntegerProperty categoryProperty() {
        return categoryProperty;
    }

    public StatusType getOldStatusProperty() {
        return oldStatusProperty.get();
    }

    public ObjectProperty<StatusType> oldStatusProperty() {
        return oldStatusProperty;
    }

    public void setOldStatusProperty(StatusType oldStatusProperty) {
        this.oldStatusProperty.set(oldStatusProperty);
    }

    @Override
    public Ticket toEntity() {
        return new Ticket(
                getId(),
                getTitle(),
                getStatus(),
                getPriority(),
                getCreation(),
                getCategory()
        );
    }
}
