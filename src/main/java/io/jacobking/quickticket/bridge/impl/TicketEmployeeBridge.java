package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
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

    public TicketEmployeeBridge(Database database) {
        super(database, RepoType.TICKET_EMPLOYEES);
    }

    public FilteredList<TicketEmployeeModel> getEmployeesForTicket(final int ticketId) {
        return getObservableList().filtered(em -> em.getTicketId() == ticketId);
    }

    public boolean removeByEmployeeId(final int employeeId) {
        return getObservableList().removeIf(__ -> __.getEmployeeId() == employeeId);
    }

    public ObservableList<TicketModel> getTicketsForEmployee(final int employeeId) {
        return FXCollections.observableArrayList(
                crud.getAll(RepoType.TICKET_EMPLOYEES, TICKET_EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
                        .stream()
                        .map(ticketEmployee -> (TicketEmployee) ticketEmployee)
                        .map(employee -> getBridgeContext().getTicket().getModel(employee.getTicketId()))
                        .filter(Objects::nonNull)
                        .toList()
        );
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
