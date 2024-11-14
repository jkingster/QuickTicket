package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.TicketLink;
import io.jacobking.quickticket.tables.records.TicketLinkRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.Tables.TICKET_LINK;

public class LinkRepository implements Repository<TicketLink> {
    @Override public TicketLink getById(DSLContext context, int id) {
        return context.selectFrom(TICKET_LINK)
                .where(TICKET_LINK.ID.eq(id))
                .fetchOneInto(TicketLink.class);
    }

    @Override public TicketLink save(DSLContext context, TicketLink ticketLink) {
        return context.insertInto(TICKET_LINK)
                .set(new TicketLinkRecord(ticketLink))
                .onConflictDoNothing()
                .returning()
                .fetchOneInto(TicketLink.class);
    }

    @Override public List<TicketLink> getAll(DSLContext context) {
        return context.selectFrom(TICKET_LINK)
                .fetchInto(TicketLink.class);
    }

    @Override public List<TicketLink> getAll(DSLContext context, Condition condition) {
        return context.selectFrom(TICKET_LINK)
                .where(condition)
                .fetchInto(TicketLink.class);
    }

    @Override public boolean delete(DSLContext context, int id) {
        return context.delete(TICKET_LINK)
                .where(TICKET_LINK.ID.eq(id))
                .execute() == SUCCESS;
    }

    @Override public boolean deleteWhere(DSLContext context, Condition condition) {
        return context.delete(TICKET_LINK)
                .where(condition)
                .execute() >= SUCCESS;
    }

    @Override public boolean update(DSLContext context, TicketLink ticketLink) {
        final TicketLinkRecord record = new TicketLinkRecord(ticketLink);
        record.changed(TICKET_LINK.ID, false);
        return context.update(TICKET_LINK)
                .set(record)
                .where(TICKET_LINK.ID.eq(ticketLink.getId()))
                .execute() >= SUCCESS;
    }
}
