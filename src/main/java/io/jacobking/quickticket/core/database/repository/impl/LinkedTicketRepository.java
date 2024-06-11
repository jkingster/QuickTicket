package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.LinkedTicket;
import io.jacobking.quickticket.tables.records.LinkedTicketRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.Tables.LINKED_TICKET;

public class LinkedTicketRepository implements Repository<LinkedTicket> {
    @Override public LinkedTicket getById(DSLContext context, int id) {
        return context.selectFrom(LINKED_TICKET)
                .where(LINKED_TICKET.ID.eq(id))
                .fetchOneInto(LinkedTicket.class);
    }

    @Override public LinkedTicket save(DSLContext context, LinkedTicket linkedTicket) {
        return context.insertInto(LINKED_TICKET)
                .set(new LinkedTicketRecord(linkedTicket))
                .onConflictDoNothing()
                .returning()
                .fetchOneInto(LinkedTicket.class);
    }

    @Override public List<LinkedTicket> getAll(DSLContext context) {
        return context.selectFrom(LINKED_TICKET)
                .fetchInto(LinkedTicket.class);
    }

    @Override public List<LinkedTicket> getAll(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean delete(DSLContext context, int id) {
        return context.deleteFrom(LINKED_TICKET)
                .where(LINKED_TICKET.ID.eq(id))
                .execute() >= SUCCESS;
    }

    @Override public boolean deleteWhere(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean update(DSLContext context, LinkedTicket linkedTicket) {
        throw new UnsupportedOperationException();
    }
}
