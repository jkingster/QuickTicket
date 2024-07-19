package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.EmailInterface;
import io.jacobking.quickticket.tables.records.EmailInterfaceRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.tables.EmailInterface.EMAIL_INTERFACE;

public class EmailInterfaceRepository implements Repository<EmailInterface> {

    /**
     * This is going to ignore the passed ID field no matter what.
     * There is a trigger in place that only allows one row (this is to prevent multiple settings).
     *
     * @param context
     * @param id
     * @return
     */
    @Override
    public EmailInterface getById(DSLContext context, int id) {
        return context.selectFrom(EMAIL_INTERFACE)
                .where(EMAIL_INTERFACE.ID.eq(0))
                .fetchOneInto(EmailInterface.class);
    }

    @Override
    public EmailInterface save(DSLContext context, EmailInterface email) {
        throw new UnsupportedOperationException("EmailInterface#save(Context, EmailInterface) not supported!");
    }

    @Override
    public List<EmailInterface> getAll(DSLContext context) {
        return context.selectFrom(EMAIL_INTERFACE)
                .fetchInto(EmailInterface.class);
    }

    @Override
    public List<EmailInterface> getAll(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException("EmailInterface#getAll(Context, Condition) not supported!");
    }

    @Override
    public boolean delete(DSLContext context, int id) {
        throw new UnsupportedOperationException("EmailInterface#delete(Context, Integer) not supported!");
    }

    @Override public boolean deleteWhere(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean update(DSLContext context, EmailInterface email) {
        final EmailInterfaceRecord record = new EmailInterfaceRecord(email);
        record.changed(EMAIL_INTERFACE.ID, false);
        return context.update(EMAIL_INTERFACE)
                .set(record)
                .execute() >= SUCCESS;
    }
}
