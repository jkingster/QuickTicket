package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.TicketEmployee;
import io.jacobking.quickticket.tables.records.TicketEmployeeRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.Tables.TICKET_EMPLOYEE;

public class TicketEmployeeRepository implements Repository<TicketEmployee> {
    @Override public TicketEmployee getById(DSLContext context, int id) {
        return context.selectFrom(TICKET_EMPLOYEE)
                .where(TICKET_EMPLOYEE.TICKET_ID.eq(id))
                .fetchOneInto(TicketEmployee.class);
    }

    @Override public TicketEmployee save(DSLContext context, TicketEmployee TicketEmployee) {
        return context.insertInto(TICKET_EMPLOYEE)
                .set(new TicketEmployeeRecord(TicketEmployee))
                .onConflictDoNothing()
                .returning()
                .fetchOneInto(TicketEmployee.class);
    }

    @Override public List<TicketEmployee> getAll(DSLContext context) {
        return context.selectFrom(TICKET_EMPLOYEE)
                .fetchInto(TicketEmployee.class);
    }

    @Override public List<TicketEmployee> getAll(DSLContext context, Condition condition) {
        return context.selectFrom(TICKET_EMPLOYEE)
                .where(condition)
                .fetchInto(TicketEmployee.class);
    }

    @Override public boolean delete(DSLContext context, int id) {
        return context.delete(TICKET_EMPLOYEE)
                .where(TICKET_EMPLOYEE.TICKET_ID.eq(id))
                .execute() == SUCCESS;
    }

    @Override public boolean deleteWhere(DSLContext context, Condition condition) {
        return context.deleteFrom(TICKET_EMPLOYEE)
                .where(condition)
                .execute() >= SUCCESS;
    }

    @Override public boolean update(DSLContext context, TicketEmployee TicketEmployee) {
        throw new UnsupportedOperationException();
    }
}
