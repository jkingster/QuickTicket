package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.Ticket;
import io.jacobking.quickticket.tables.records.TicketRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.Tables.TICKET;

public class TicketRepository implements Repository<Ticket> {

    @Override
    public Ticket getById(DSLContext context, int id) {
        return context.selectFrom(TICKET)
                .where(TICKET.ID.eq(id))
                .fetchOneInto(Ticket.class);
    }

    @Override
    public Ticket save(DSLContext context, Ticket ticket) {
        return context.insertInto(TICKET)
                .set(new TicketRecord(ticket))
                .onConflictDoNothing()
                .returning()
                .fetchOneInto(Ticket.class);
    }

    @Override
    public List<Ticket> getAll(DSLContext context) {
        return context.selectFrom(TICKET)
                .fetchInto(Ticket.class);
    }

    @Override
    public List<Ticket> getAll(DSLContext context, Condition condition) {
        return context.selectFrom(TICKET)
                .where(condition)
                .fetchInto(Ticket.class);
    }

    @Override
    public boolean delete(DSLContext context, int id) {
        return context.delete(TICKET)
                .where(TICKET.ID.eq(id))
                .execute() == SUCCESS;
    }

    @Override
    public boolean update(DSLContext context, Ticket ticket) {
        final TicketRecord updateRecord = new TicketRecord(ticket);
        updateRecord.changed(TICKET.ID, false);

        return context.update(TICKET)
                .set(updateRecord)
                .where(TICKET.ID.eq(ticket.getId()))
                .execute() >= SUCCESS;
    }
}
