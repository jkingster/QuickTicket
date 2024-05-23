package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.tables.pojos.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.Map;
import java.util.function.Predicate;

import static io.jacobking.quickticket.Tables.TICKET;

public class TicketBridge extends Bridge<Ticket, TicketModel> {

    private final Map<StatusType, ObservableList<TicketModel>> ticketMap;

    public TicketBridge(final Database database) {
        super(database, RepoType.TICKET);
        this.ticketMap = FXCollections.observableHashMap();

        ticketMap.put(StatusType.OPEN, FXCollections.observableArrayList(
                getObservableList().filtered(tm -> tm.statusProperty().get() == StatusType.OPEN)
        ));

        ticketMap.put(StatusType.ACTIVE, FXCollections.observableArrayList(
                getObservableList().filtered(tm -> tm.statusProperty().get() == StatusType.ACTIVE)
        ));

        ticketMap.put(StatusType.RESOLVED, FXCollections.observableArrayList(
                getObservableList().filtered(tm -> tm.statusProperty().get() == StatusType.RESOLVED)
        ));

        ticketMap.put(StatusType.PAUSED, FXCollections.observableArrayList(
                getObservableList().filtered(tm -> tm.statusProperty().get() == StatusType.PAUSED)
        ));

    }


    @Override
    public TicketModel convertEntity(Ticket entity) {
        return new TicketModel(entity);
    }

    public TicketModel getLastViewed() {
        final Ticket ticket = crud.getContext()
                .selectFrom(TICKET)
                .orderBy(TICKET.LAST_OPENED_TIMESTAMP.desc())
                .limit(1)
                .fetchOneInto(Ticket.class);
        return ticket == null ? null : new TicketModel(ticket);
    }

    @Override public TicketModel createModel(Ticket entity) {
        final TicketModel model = super.createModel(entity);
        final StatusType type = model.statusProperty().getValue();
        final ObservableList<TicketModel> targetList = getListByStatus(type);
        targetList.add(0, model);
        return model;
    }

    @Override public void remove(int id) {
        final TicketModel model = super.getModel(id);
        if (model != null) {
            super.remove(id);
            final StatusType type = model.statusProperty().getValue();
            getListByStatus(type).remove(model);
        }
    }

    public boolean update(final TicketModel model, final StatusType originalStatus) {
        if (super.update(model)) {
            getListByStatus(originalStatus).remove(model);
            final StatusType newStatus = model.statusProperty().get();
            return getListByStatus(newStatus).add(model);
        }
        return false;
    }

    public ObservableList<TicketModel> getListByStatus(final StatusType statusType) {
        return ticketMap.get(statusType);
    }

    public FilteredList<TicketModel> getFilteredList(final Predicate<TicketModel> predicate) {
        return new FilteredList<>(getObservableList(), predicate);
    }
}


