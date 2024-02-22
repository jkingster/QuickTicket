package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.TicketModel;
import io.jacobking.quickticket.tables.pojos.Ticket;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.function.Predicate;

public class TicketBridge extends Bridge<Ticket, TicketModel> {

    private ObservableList<TicketModel> baseUnfilteredList;

    public TicketBridge() {
        super(RepoType.TICKET);
    }

    @Override protected void loadEntities() {
        super.loadEntities();
        this.baseUnfilteredList = FXCollections.observableArrayList(item -> new Observable[]{item.statusProperty()});
        this.baseUnfilteredList.addAll(getObservableList());

        getObservableList().addListener((ListChangeListener<? super TicketModel>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    baseUnfilteredList.addAll(change.getAddedSubList());
                }
            }
        });
    }



    @Override
    public TicketModel convertEntity(Ticket entity) {
        return new TicketModel(entity);
    }

    public FilteredList<TicketModel> getFilteredList(final Predicate<TicketModel> predicate) {
        return new FilteredList<>(baseUnfilteredList, predicate);
    }
}
