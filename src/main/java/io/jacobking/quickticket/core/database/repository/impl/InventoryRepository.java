package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.Inventory;
import io.jacobking.quickticket.tables.records.InventoryRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.Tables.INVENTORY;

public class InventoryRepository implements Repository<Inventory> {
    @Override public Inventory getById(DSLContext context, int id) {
        return context.selectFrom(INVENTORY)
                .where(INVENTORY.ID.eq(id))
                .fetchOneInto(Inventory.class);
    }

    @Override public Inventory save(DSLContext context, Inventory inventory) {
        return context.insertInto(INVENTORY)
                .set(new InventoryRecord(inventory))
                .onConflictDoNothing()
                .returning()
                .fetchOneInto(Inventory.class);
    }

    @Override public List<Inventory> getAll(DSLContext context) {
        return context.selectFrom(INVENTORY)
                .fetchInto(Inventory.class);
    }

    @Override public List<Inventory> getAll(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean delete(DSLContext context, int id) {
        return context.deleteFrom(INVENTORY)
                .where(INVENTORY.ID.eq(id))
                .execute() == SUCCESS;
    }

    @Override public boolean deleteWhere(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean update(DSLContext context, Inventory inventory) {
        final InventoryRecord record = new InventoryRecord(inventory);
        record.changed(INVENTORY.ID, false);
        return context.update(INVENTORY)
                .set(record)
                .where(INVENTORY.ID.eq(inventory.getId()))
                .execute() >= SUCCESS;
    }
}
