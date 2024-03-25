package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.AlertSettingsModel;
import io.jacobking.quickticket.tables.pojos.AlertSettings;

public class AlertSettingsBridge extends Bridge<AlertSettings, AlertSettingsModel> {
    public AlertSettingsBridge() {
        super(RepoType.ALERT);
    }

    @Override protected void loadEntities() {
        final AlertSettings settings = Database.call().getById(RepoType.ALERT, 0);
        if (settings == null)
            return;
        getObservableList().add(0, convertEntity(settings));
    }

    @Override public void addModel(AlertSettingsModel model) {
        throw new UnsupportedOperationException();
    }

    @Override public AlertSettingsModel createModel(AlertSettings entity) {
        throw new UnsupportedOperationException();
    }

    @Override public void remove(int id) {
        throw new UnsupportedOperationException();
    }

    @Override public AlertSettingsModel convertEntity(AlertSettings entity) {
        return new AlertSettingsModel(entity);
    }
}
