package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.gui.model.ViewModel;
import io.jacobking.quickticket.tables.pojos.LinkedTicket;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class LinkedTicketModel extends ViewModel<LinkedTicket> {

    private final IntegerProperty ticketIdProperty       = new SimpleIntegerProperty();
    private final IntegerProperty linkedTicketIdProperty = new SimpleIntegerProperty();

    public LinkedTicketModel(int id, int ticketId, int linkedTicketId) {
        super(id);
        ticketIdProperty.setValue(ticketId);
        linkedTicketIdProperty.setValue(linkedTicketId);
    }

    public LinkedTicketModel(final LinkedTicket linkedTicket) {
        this(
                linkedTicket.getId(),
                linkedTicket.getTicketId(),
                linkedTicket.getLinkedId()
        );
    }

    public int getTicketIdProperty() {
        return ticketIdProperty.get();
    }

    public IntegerProperty ticketIdProperty() {
        return ticketIdProperty;
    }

    public void setTicketIdProperty(int ticketIdProperty) {
        this.ticketIdProperty.set(ticketIdProperty);
    }

    public int getLinkedTicketIdProperty() {
        return linkedTicketIdProperty.get();
    }

    public IntegerProperty linkedTicketIdProperty() {
        return linkedTicketIdProperty;
    }

    public void setLinkedTicketIdProperty(int linkedTicketIdProperty) {
        this.linkedTicketIdProperty.set(linkedTicketIdProperty);
    }

    @Override public LinkedTicket toEntity() {
        return new LinkedTicket()
                .setId(getId())
                .setTicketId(getTicketIdProperty())
                .setLinkedId(getLinkedTicketIdProperty());
    }
}
