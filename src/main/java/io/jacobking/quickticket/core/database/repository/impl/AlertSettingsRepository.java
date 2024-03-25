package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.AlertSettings;
import io.jacobking.quickticket.tables.records.AlertSettingsRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.Tables.ALERT_SETTINGS;

public class AlertSettingsRepository implements Repository<AlertSettings> {
    @Override public AlertSettings getById(DSLContext context, int id) {
        return context.selectFrom(ALERT_SETTINGS)
                .where(ALERT_SETTINGS.ID.eq(0))
                .fetchOneInto(AlertSettings.class);
    }

    @Override public AlertSettings save(DSLContext context, AlertSettings alertSettings) {
        throw new UnsupportedOperationException();
    }

    @Override public List<AlertSettings> getAll(DSLContext context) {
        throw new UnsupportedOperationException();
    }

    @Override public List<AlertSettings> getAll(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean delete(DSLContext context, int id) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean update(DSLContext context, AlertSettings alertSettings) {
        final AlertSettingsRecord record = new AlertSettingsRecord(alertSettings);
        record.changed(ALERT_SETTINGS.ID, false);
        return context.update(ALERT_SETTINGS)
                .set(record)
                .where(ALERT_SETTINGS.ID.eq(0))
                .execute() >= SUCCESS;
    }
}
