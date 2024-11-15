package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.TicketEmployeeModel;
import io.jacobking.quickticket.gui.model.TicketModel;
import io.jacobking.quickticket.tables.pojos.TicketEmployees;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.Objects;

import static io.jacobking.quickticket.Tables.TICKET_EMPLOYEES;

public class TicketEmployeeBridge extends Bridge<TicketEmployees, TicketEmployeeModel> {

    private final ObservableMap<Integer, ObservableList<TicketEmployeeModel>> mappedEmployees = FXCollections.observableHashMap();

    public TicketEmployeeBridge(Database database, BridgeContext bridgeContext) {
        super(database, RepoType.TICKET_EMPLOYEES, bridgeContext);
        populateMap();
        configureRemovalListener();
    }


    @Override public TicketEmployeeModel convertEntity(TicketEmployees entity) {
        return new TicketEmployeeModel(entity);
    }

    public ObservableList<TicketModel> getTicketsForEmployee(final int id) {
        return FXCollections.observableArrayList(
                crud.getAll(RepoType.TICKET_EMPLOYEES, TICKET_EMPLOYEES.EMPLOYEE_ID.eq(id))
                        .stream()
                        .map(entity -> (TicketEmployees) entity)
                        .map(ticketEmployee -> getBridgeContext().getTicket().getModel(ticketEmployee.getTicketId()))
                        .filter(Objects::nonNull)
                        .toList()
        );
    }

    private void populateMap() {
        for (final TicketEmployeeModel model : getObservableList()) {
            final int ticketId = model.getTicketId();
            if (mappedEmployees.containsKey(ticketId)) {
                addEmployeeToList(ticketId, model);
                continue;
            }

            mappedEmployees.putIfAbsent(ticketId, FXCollections.observableArrayList(model));
        }
    }

    private void addEmployeeToList(final int ticketId, final TicketEmployeeModel model) {
        final ObservableList<TicketEmployeeModel> employeeList = mappedEmployees.get(ticketId);
        employeeList.add(model);
        mappedEmployees.put(ticketId, employeeList);
    }

    private void configureRemovalListener() {
        getObservableList().addListener((ListChangeListener<TicketEmployeeModel>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    final var subList = change.getRemoved();
                    for (final TicketEmployeeModel model : subList) {
                        final int ticketId = model.getTicketId();
                        mappedEmployees.remove(ticketId);
                    }
                }
            }
        });
    }

}
