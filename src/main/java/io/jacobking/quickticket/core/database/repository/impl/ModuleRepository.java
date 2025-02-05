package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.Module;
import io.jacobking.quickticket.tables.records.ModuleRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.Tables.MODULE;

public class ModuleRepository implements Repository<Module> {
    @Override public Module getById(DSLContext context, int id) {
        return context.selectFrom(MODULE)
                .where(MODULE.ID.eq(id))
                .fetchOneInto(Module.class);
    }

    @Override public Module save(DSLContext context, Module module) {
        throw new UnsupportedOperationException();
    }

    @Override public List<Module> getAll(DSLContext context) {
        return context.selectFrom(MODULE)
                .fetchInto(Module.class);
    }

    @Override public List<Module> getAll(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean delete(DSLContext context, int id) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean deleteWhere(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean update(DSLContext context, Module module) {
        final ModuleRecord record = new ModuleRecord(module);
        record.changed(MODULE.ID, false);
        record.changed(MODULE.NAME, false);
        record.changed(MODULE.DESCRIPTION, false);
        return context.update(MODULE)
                .set(record)
                .where(MODULE.ID.eq(module.getId()))
                .execute() >= SUCCESS;
    }
}
