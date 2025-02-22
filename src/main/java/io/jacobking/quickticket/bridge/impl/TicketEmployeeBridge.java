package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.bridge.BridgeContext;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.EmployeeModel;
import io.jacobking.quickticket.gui.model.TicketEmployeeModel;
import io.jacobking.quickticket.gui.model.TicketModel;
import io.jacobking.quickticket.tables.pojos.TicketEmployee;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.Objects;

import static io.jacobking.quickticket.Tables.TICKET_EMPLOYEE;

public class TicketEmployeeBridge extends Bridge<TicketEmployee, TicketEmployeeModel> {

    private final BridgeContext context;

    public TicketEmployeeBridge(Database database, BridgeContext bridgeContext) {
        super(database, RepoType.TICKET_EMPLOYEES);
        this.context = bridgeContext;
    }

    public FilteredList<TicketEmployeeModel> getEmployeesForTicket(final int ticketId) {
        return getObservableList().filtered(em -> em.getTicketId() == ticketId);
    }

    public boolean removeByEmployeeId(final int employeeId) {
        return getObservableList().removeIf(__ -> __.getEmployeeId() == employeeId);
    }

    public boolean removeByTicketAndEmployeeId(final int ticketId, final int employeeId) {
        return getObservableList().removeIf(__ -> __.getTicketId() == ticketId
                && __.getEmployeeId() == employeeId);
    }

    public ObservableList<TicketModel> getTicketsForEmployee(final int employeeId) {
        return FXCollections.observableArrayList(
                crud.getAll(RepoType.TICKET_EMPLOYEES, TICKET_EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
                        .stream()
                        .map(ticketEmployee -> (TicketEmployee) ticketEmployee)
                        .map(employee -> context.getTicket().getModel(employee.getTicketId()))
                        .filter(Objects::nonNull)
                        .toList()
        );
    }

    public ObservableList<EmployeeModel> getEmployeeModelsForTicket(final int ticketId) {
        return FXCollections.observableArrayList(getEmployeesForTicket(ticketId)
                .stream()
                .map(TicketEmployeeModel::getEmployeeId)
                .map(id -> context.getEmployee().getModel(id))
                .filter(Objects::nonNull)
                .toList()
        );
    }

    public boolean isEmployeeAssignedToTicket(final int ticketId, final int employeeId) {
        return getObservableList()
                .stream()
                .anyMatch(model -> model.getTicketId() == ticketId
                        && model.getEmployeeId() == employeeId);
    }

    @Override public TicketEmployeeModel convertEntity(TicketEmployee entity) {
        return new TicketEmployeeModel(entity);
    }

    @Override public void removalListener() {
        getObservableList().addListener((ListChangeListener<TicketEmployeeModel>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    final var subList = change.getRemoved();
                    for (final TicketEmployeeModel model : subList) {
                        final boolean deleted = crud.deleteWhere(RepoType.TICKET_EMPLOYEES,
                                TICKET_EMPLOYEE.EMPLOYEE_ID.eq(model.getEmployeeId()));
                        if (!deleted) ; // TODO: do something
                    }
                }
            }
        });
    }
}
