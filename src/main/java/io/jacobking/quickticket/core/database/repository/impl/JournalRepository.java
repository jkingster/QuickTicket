package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.Journal;
import io.jacobking.quickticket.tables.records.JournalRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.tables.Journal.JOURNAL;

public class JournalRepository implements Repository<Journal> {
    @Override public Journal getById(DSLContext context, int id) {
        return context.selectFrom(JOURNAL)
                .where(JOURNAL.ID.eq(id))
                .fetchOneInto(Journal.class);
    }

    @Override public Journal save(DSLContext context, Journal journal) {
        return context.insertInto(JOURNAL)
                .set(new JournalRecord(journal))
                .onConflictDoNothing()
                .returning()
                .fetchOneInto(Journal.class);
    }

    @Override public List<Journal> getAll(DSLContext context) {
        return context.selectFrom(JOURNAL)
                .fetchInto(Journal.class);
    }

    @Override public List<Journal> getAll(DSLContext context, Condition condition) {
        return context.selectFrom(JOURNAL)
                .where(condition)
                .fetchInto(Journal.class);
    }

    @Override public boolean delete(DSLContext context, int id) {
        return context.deleteFrom(JOURNAL)
                .where(JOURNAL.ID.eq(id))
                .execute() == SUCCESS;
    }

    @Override public boolean update(DSLContext context, Journal journal) {
        final JournalRecord journalRecord = new JournalRecord(journal);
        journalRecord.changed(JOURNAL.ID, false);
        return context.update(JOURNAL)
                .set(journalRecord)
                .where(JOURNAL.ID.eq(journal.getId()))
                .execute() >= SUCCESS;
    }
}
