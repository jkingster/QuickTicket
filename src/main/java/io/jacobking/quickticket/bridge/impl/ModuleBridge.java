package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.ModuleModel;
import io.jacobking.quickticket.tables.pojos.Module;

public class ModuleBridge extends Bridge<Module, ModuleModel> {
    public ModuleBridge(Database database) {
        super(database, RepoType.MODULE);
    }

    @Override public ModuleModel convertEntity(Module entity) {
        return new ModuleModel(entity);
    }
}
