package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.tables.pojos.LinkedTicket;
import javafx.collections.ObservableList;

public class LinkedTicketBridge extends Bridge<LinkedTicket, LinkedTicketModel> {

    public LinkedTicketBridge(Database database) {
        super(database, RepoType.LINKED_TICKET);
    }

    @Override public LinkedTicketModel convertEntity(LinkedTicket entity) {
        return new LinkedTicketModel(entity);
    }

    public ObservableList<LinkedTicketModel> getLinkedTickets(final int ticketId) {
        return getObservableList().filtered(pm -> pm.getTicketIdProperty() == ticketId);
    }
}
