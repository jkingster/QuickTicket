package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.InventoryLog;
import io.jacobking.quickticket.tables.records.InventoryLogRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.Tables.INVENTORY_LOG;

public class InventoryLogRepository implements Repository<InventoryLog> {
    @Override public InventoryLog getById(DSLContext context, int id) {
        return context.selectFrom(INVENTORY_LOG)
                .where(INVENTORY_LOG.ID.eq(id))
                .fetchOneInto(InventoryLog.class);
    }

    @Override public InventoryLog save(DSLContext context, InventoryLog inventoryLog) {
        return context.insertInto(INVENTORY_LOG)
                .set(new InventoryLogRecord(inventoryLog))
                .onConflictDoNothing()
                .returning()
                .fetchOneInto(InventoryLog.class);
    }

    @Override public List<InventoryLog> getAll(DSLContext context) {
        return context.selectFrom(INVENTORY_LOG)
                .fetchInto(InventoryLog.class);
    }

    @Override public List<InventoryLog> getAll(DSLContext context, Condition condition) {
        return context.selectFrom(INVENTORY_LOG)
                .where(condition)
                .fetchInto(InventoryLog.class);
    }

    @Override public boolean delete(DSLContext context, int id) {
        return context.deleteFrom(INVENTORY_LOG)
                .where(INVENTORY_LOG.ID.eq(id))
                .execute() == SUCCESS;
    }

    @Override public boolean deleteWhere(DSLContext context, Condition condition) {
        return context.deleteFrom(INVENTORY_LOG)
                .where(condition)
                .execute() == SUCCESS;
    }

    @Override public boolean update(DSLContext context, InventoryLog inventoryLog) {
        final InventoryLogRecord inventoryLogRecord = new InventoryLogRecord(inventoryLog);
        inventoryLogRecord.changed(INVENTORY_LOG.ID, false);
        return context.update(INVENTORY_LOG)
                .set(inventoryLogRecord)
                .where(INVENTORY_LOG.ID.eq(inventoryLog.getId()))
                .execute() >= SUCCESS;
    }
}
