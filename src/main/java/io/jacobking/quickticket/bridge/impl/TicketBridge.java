package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.tables.pojos.Ticket;

public class TicketBridge extends Bridge<Ticket, TicketModel> {
    public TicketBridge() {
        super(RepoType.TICKET);
    }

    @Override
    public TicketModel convertEntity(Ticket entity) {
        return new TicketModel(entity);
    }
}
