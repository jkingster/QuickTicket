package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.InventoryModel;
import io.jacobking.quickticket.tables.pojos.Inventory;

public class InventoryBridge extends Bridge<Inventory, InventoryModel> {
    public InventoryBridge(Database database) {
        super(database, RepoType.INVENTORY);
    }

    @Override public InventoryModel convertEntity(Inventory entity) {
        return null;
    }
}
