package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.gui.model.TicketModel;
import io.jacobking.quickticket.tables.pojos.Ticket;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.Comparator;

public class TicketBridge extends Bridge<Ticket, TicketModel> {


    public TicketBridge(Database database) {
        super(database, RepoType.TICKET);
    }

    @Override public TicketModel convertEntity(Ticket entity) {
        return new TicketModel(entity);
    }


}


