package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.gui.model.TicketModel;
import io.jacobking.quickticket.tables.pojos.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.Comparator;

public class TicketBridge extends Bridge<Ticket, TicketModel> {

    private final FilteredList<TicketModel> openTickets;
    private final IntegerBinding openTicketCount;

    private final FilteredList<TicketModel> activeTickets;
    private final IntegerBinding activeTicketCount;

    private final FilteredList<TicketModel> pausedTickets;
    private final IntegerBinding pausedTicketCount;

    private final FilteredList<TicketModel> resolvedTickets;
    private final IntegerBinding resolvedTicketCount;


    public TicketBridge(Database database) {
        super(database, RepoType.TICKET);
        this.openTickets = new FilteredList<>(getObservableList(), ticket -> ticket.statusProperty().getValue() == StatusType.OPEN);
        this.openTicketCount = Bindings.size(openTickets);

        this.activeTickets = new FilteredList<>(getObservableList(), ticket -> ticket.statusProperty().getValue() == StatusType.ACTIVE);
        this.activeTicketCount = Bindings.size(activeTickets);

        this.pausedTickets = new FilteredList<>(getObservableList(), ticket -> ticket.statusProperty().getValue() == StatusType.PAUSED);
        this.pausedTicketCount = Bindings.size(pausedTickets);

        this.resolvedTickets = new FilteredList<>(getObservableList(), ticket -> ticket.statusProperty().getValue() == StatusType.RESOLVED);
        this.resolvedTicketCount = Bindings.size(resolvedTickets);
    }

    @Override public TicketModel convertEntity(Ticket entity) {
        return new TicketModel(entity);
    }

    public FilteredList<TicketModel> getOpenTickets() {
        return openTickets;
    }

    public Number getOpenTicketCount() {
        return openTicketCount.get();
    }

    public IntegerBinding openTicketCountProperty() {
        return openTicketCount;
    }

    public FilteredList<TicketModel> getActiveTickets() {
        return activeTickets;
    }

    public Number getActiveTicketCount() {
        return activeTicketCount.get();
    }

    public IntegerBinding activeTicketCountProperty() {
        return activeTicketCount;
    }

    public FilteredList<TicketModel> getPausedTickets() {
        return pausedTickets;
    }

    public Number getPausedTicketCount() {
        return pausedTicketCount.get();
    }

    public IntegerBinding pausedTicketCountProperty() {
        return pausedTicketCount;
    }

    public FilteredList<TicketModel> getResolvedTickets() {
        return resolvedTickets;
    }

    public Number getResolvedTicketCount() {
        return resolvedTicketCount.get();
    }

    public IntegerBinding resolvedTicketCountProperty() {
        return resolvedTicketCount;
    }
}


