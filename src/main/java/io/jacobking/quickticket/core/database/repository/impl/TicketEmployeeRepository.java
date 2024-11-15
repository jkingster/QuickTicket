package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.TicketEmployees;
import io.jacobking.quickticket.tables.records.TicketEmployeesRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.Tables.TICKET_EMPLOYEES;

public class TicketEmployeeRepository implements Repository<TicketEmployees> {
    @Override public TicketEmployees getById(DSLContext context, int id) {
        return context.selectFrom(TICKET_EMPLOYEES)
                .where(TICKET_EMPLOYEES.TICKET_ID.eq(id))
                .fetchOneInto(TicketEmployees.class);
    }

    @Override public TicketEmployees save(DSLContext context, TicketEmployees ticketEmployees) {
        return context.insertInto(TICKET_EMPLOYEES)
                .set(new TicketEmployeesRecord(ticketEmployees))
                .onConflictDoNothing()
                .returning()
                .fetchOneInto(TicketEmployees.class);
    }

    @Override public List<TicketEmployees> getAll(DSLContext context) {
        return context.selectFrom(TICKET_EMPLOYEES)
                .fetchInto(TicketEmployees.class);
    }

    @Override public List<TicketEmployees> getAll(DSLContext context, Condition condition) {
        return context.selectFrom(TICKET_EMPLOYEES)
                .where(condition)
                .fetchInto(TicketEmployees.class);
    }

    @Override public boolean delete(DSLContext context, int id) {
        return context.delete(TICKET_EMPLOYEES)
                .where(TICKET_EMPLOYEES.TICKET_ID.eq(id))
                .execute() == SUCCESS;
    }

    @Override public boolean deleteWhere(DSLContext context, Condition condition) {
        return context.deleteFrom(TICKET_EMPLOYEES)
                .where(condition)
                .execute() >= SUCCESS;
    }

    @Override public boolean update(DSLContext context, TicketEmployees ticketEmployees) {
        throw new UnsupportedOperationException();
    }
}
