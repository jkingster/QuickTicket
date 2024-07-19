package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.AlertModel;
import io.jacobking.quickticket.tables.pojos.Alerts;

public class AlertBridge extends Bridge<Alerts, AlertModel> {
    public AlertBridge(Database database) {
        super(database, RepoType.ALERT);
    }

    @Override public AlertModel convertEntity(Alerts entity) {
        return new AlertModel(entity);
    }
}
