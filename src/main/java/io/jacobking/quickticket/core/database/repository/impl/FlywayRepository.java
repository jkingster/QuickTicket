package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.FlywaySchemaHistory;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.Tables.FLYWAY_SCHEMA_HISTORY;

public class FlywayRepository implements Repository<FlywaySchemaHistory> {
    @Override public FlywaySchemaHistory getById(DSLContext context, int id) {
        throw new UnsupportedOperationException();
    }

    @Override public FlywaySchemaHistory save(DSLContext context, FlywaySchemaHistory flywaySchemaHistory) {
        throw new UnsupportedOperationException();

    }

    @Override public List<FlywaySchemaHistory> getAll(DSLContext context) {
        return context.selectFrom(FLYWAY_SCHEMA_HISTORY)
                .fetchInto(FlywaySchemaHistory.class);
    }

    @Override public List<FlywaySchemaHistory> getAll(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();

    }

    @Override public boolean delete(DSLContext context, int id) {
        throw new UnsupportedOperationException();

    }

    @Override public boolean deleteWhere(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();

    }

    @Override public boolean update(DSLContext context, FlywaySchemaHistory flywaySchemaHistory) {
             throw new UnsupportedOperationException();
    }
}
