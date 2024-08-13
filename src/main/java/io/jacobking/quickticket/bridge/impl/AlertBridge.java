package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.AlertModel;
import io.jacobking.quickticket.tables.pojos.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlertBridge extends Bridge<Alerts, AlertModel> {
    private Map<String, AlertModel> alertMap;

    public AlertBridge(Database database) {
        super(database, RepoType.ALERT);
    }

    @Override protected void loadEntities() {
        super.loadEntities();
        this.alertMap = new HashMap<>();
        getObservableList().forEach(model -> {
            alertMap.putIfAbsent(model.getAlertName(), model);
        });
    }

    public ObservableList<AlertModel> getOriginalAlerts() {
        final List<Alerts> original = getOriginalEntities();
        return original.stream()
                .map(AlertModel::new)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @Override public AlertModel convertEntity(Alerts entity) {
        return new AlertModel(entity);
    }

    public Map<String, AlertModel> getAlertMap() {
        return alertMap;
    }
}
