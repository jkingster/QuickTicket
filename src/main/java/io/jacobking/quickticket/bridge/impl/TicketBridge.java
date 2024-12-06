package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.core.type.StatusType;
import io.jacobking.quickticket.gui.model.TicketModel;
import io.jacobking.quickticket.tables.pojos.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.Comparator;
import java.util.Map;

public class TicketBridge extends Bridge<Ticket, TicketModel> {

    private final Map<StatusType, ObservableList<TicketModel>> ticketMapByStatus;

    public TicketBridge(Database database) {
        super(database, RepoType.TICKET);
        this.ticketMapByStatus = FXCollections.observableHashMap();

        preloadMap();
    }

    @Override public TicketModel convertEntity(Ticket entity) {
        return new TicketModel(entity);
    }

    @Override public ObservableList<TicketModel> getObservableList() {
        return super.getObservableList().sorted(Comparator.comparing(TicketModel::getCreation).reversed());
    }

    public ObservableList<TicketModel> getListByStatus(final StatusType statusType) {
        return ticketMapByStatus.getOrDefault(statusType, FXCollections.observableArrayList());
    }

    @Override public TicketModel createModel(Ticket entity) {
        final TicketModel ticketModel = super.createModel(entity);
        final StatusType ticketStatus = ticketModel.statusProperty().getValue();

        final var listByStatus = getListByStatus(ticketStatus);
        listByStatus.add(DEFAULT_INDEX, ticketModel);
        return ticketModel;
    }

    @Override public boolean remove(int id) {
        final TicketModel model = super.getModel(id);
        if (model == null) {
            return false;
        }

        final boolean removed = super.remove(id);
        if (removed) {
            final StatusType status = model.statusProperty().getValue();
            final ObservableList<TicketModel> listByStatus = getListByStatus(status);
            listByStatus.remove(model);
        }

        return removed;
    }

    public boolean update(TicketModel model, StatusType originalStatus) {
        final boolean updated = super.update(model);
        if (updated && originalStatus != StatusType.UNDEFINED) {
            final StatusType newStatus = model.statusProperty().getValue();
            final ObservableList<TicketModel> newList = getListByStatus(newStatus);
            newList.add(DEFAULT_INDEX, model);

            final ObservableList<TicketModel> oldList = getListByStatus(originalStatus);
            oldList.remove(model);
        }
        return updated;
    }

    public FilteredList<TicketModel> getFilteredListByStatus(final StatusType statusType) {
        return getObservableList().filtered(ticket -> StatusType.of(ticket.getStatus()) == statusType);
    }

    private void preloadMap() {
        for (final StatusType status : StatusType.values()) {
            if (status == StatusType.UNDEFINED)
                continue;

            final var filteredList = getFilteredListByStatus(status);
            ticketMapByStatus.putIfAbsent(status, FXCollections.observableArrayList(filteredList));
        }
    }
}


