package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.Email;
import io.jacobking.quickticket.tables.records.EmailRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.tables.Email.EMAIL;

public class EmailRepository implements Repository<Email> {

    /**
     * This is going to ignore the passed ID field no matter what.
     * There is a trigger in place that only allows one row (this is to prevent multiple settings).
     *
     * @param context
     * @param id
     * @return
     */
    @Override
    public Email getById(DSLContext context, int id) {
        return context.selectFrom(EMAIL)
                .where(EMAIL.ID.eq(0))
                .fetchOneInto(Email.class);
    }

    @Override
    public Email save(DSLContext context, Email email) {
        throw new UnsupportedOperationException("Email#save(Context, Email) not supported!");
    }

    @Override
    public List<Email> getAll(DSLContext context) {
        return context.selectFrom(EMAIL)
                .fetchInto(Email.class);
    }

    @Override
    public List<Email> getAll(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException("Email#getAll(Context, Condition) not supported!");
    }

    @Override
    public boolean delete(DSLContext context, int id) {
        throw new UnsupportedOperationException("Email#delete(Context, Integer) not supported!");
    }

    @Override
    public boolean update(DSLContext context, Email email) {
        final EmailRecord record = new EmailRecord(email);
        record.changed(EMAIL.ID, false);
        return context.update(EMAIL)
                .set(record)
                .execute() >= SUCCESS;
    }
}
