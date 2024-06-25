package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.TicketCategories;
import io.jacobking.quickticket.tables.records.TicketCategoriesRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.Tables.TICKET;
import static io.jacobking.quickticket.Tables.TICKET_CATEGORIES;

public class CategoryRepository implements Repository<TicketCategories> {
    @Override public TicketCategories getById(DSLContext context, int id) {
        return context.selectFrom(TICKET_CATEGORIES)
                .where(TICKET_CATEGORIES.ID.eq(id))
                .fetchOneInto(TicketCategories.class);
    }

    @Override public TicketCategories save(DSLContext context, TicketCategories ticketCategories) {
        return context.insertInto(TICKET_CATEGORIES)
                .set(new TicketCategoriesRecord(ticketCategories))
                .onConflictDoNothing()
                .returning()
                .fetchOneInto(TicketCategories.class);
    }

    @Override public List<TicketCategories> getAll(DSLContext context) {
        return context.selectFrom(TICKET_CATEGORIES)
                .fetchInto(TicketCategories.class);
    }

    @Override public List<TicketCategories> getAll(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean delete(DSLContext context, int id) {
        return context.deleteFrom(TICKET_CATEGORIES)
                .where(TICKET.CATEGORY_ID.eq(id))
                .execute() >= SUCCESS;
    }

    @Override public boolean deleteWhere(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean update(DSLContext context, TicketCategories ticketCategories) {
        final TicketCategoriesRecord record = new TicketCategoriesRecord(ticketCategories);
        record.changed(TICKET_CATEGORIES.ID, false);

        return context.update(TICKET_CATEGORIES)
                .set(record)
                .where(TICKET_CATEGORIES.ID.eq(ticketCategories.getId()))
                .execute() >= SUCCESS;
    }
}
