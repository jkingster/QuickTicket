package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.InventoryLogModel;
import io.jacobking.quickticket.tables.pojos.InventoryLog;

public class InventoryLogBridge extends Bridge<InventoryLog, InventoryLogModel> {
    public InventoryLogBridge(final Database database) {
        super(database, RepoType.INVENTORY_LOG);
    }

    @Override public InventoryLogModel convertEntity(InventoryLog entity) {
        return new InventoryLogModel(entity);
    }
}
