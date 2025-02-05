package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.Alerts;
import io.jacobking.quickticket.tables.records.AlertsRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.Tables.ALERTS;

public class AlertRepository implements Repository<Alerts> {
    @Override public Alerts getById(DSLContext context, int id) {
        return context.selectFrom(ALERTS)
                .where(ALERTS.ALERT_ID.eq(id))
                .fetchOneInto(Alerts.class);
    }

    @Override public Alerts save(DSLContext context, Alerts alerts) {
        throw new UnsupportedOperationException();
    }

    @Override public List<Alerts> getAll(DSLContext context) {
        return context.selectFrom(ALERTS)
                .fetchInto(Alerts.class);
    }

    @Override public List<Alerts> getAll(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean delete(DSLContext context, int id) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean deleteWhere(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean update(DSLContext context, Alerts alerts) {
        final AlertsRecord record = new AlertsRecord(alerts);
        record.changed(ALERTS.ALERT_ID, false);
        return context.update(ALERTS)
                .set(record)
                .where(ALERTS.ALERT_ID.eq(alerts.getAlertId()))
                .execute() >= SUCCESS;
    }
}
