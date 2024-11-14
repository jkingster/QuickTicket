package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.LinkModel;
import io.jacobking.quickticket.tables.pojos.TicketLink;

public class LinkBridge extends Bridge<TicketLink, LinkModel> {
    public LinkBridge(Database database) {
        super(database, RepoType.TICKET_LINK);
    }

    @Override public LinkModel convertEntity(TicketLink entity) {
        return new LinkModel(entity);
    }
}
