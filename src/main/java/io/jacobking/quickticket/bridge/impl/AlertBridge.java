package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.AlertModel;
import io.jacobking.quickticket.tables.pojos.Alerts;

import java.util.HashMap;
import java.util.Map;

public class AlertBridge extends Bridge<Alerts, AlertModel> {
    private final Map<String, AlertModel> alertMap = new HashMap<>();

    public AlertBridge(Database database) {
        super(database, RepoType.ALERT);
    }

    @Override protected void loadEntities() {
        super.loadEntities();
        getObservableList().forEach(model -> alertMap.putIfAbsent(model.getAlertName(), model));
    }

    public AlertModel getAlertByName(final String name) {
        return alertMap.get(name);
    }

    @Override public AlertModel convertEntity(Alerts entity) {
        return new AlertModel(entity);
    }

    public Map<String, AlertModel> getAlertMap() {
        return alertMap;
    }
}
