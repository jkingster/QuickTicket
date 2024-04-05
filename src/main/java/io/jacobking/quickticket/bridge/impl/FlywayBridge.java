package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.FlywayModel;
import io.jacobking.quickticket.tables.pojos.FlywaySchemaHistory;

public class FlywayBridge extends Bridge<FlywaySchemaHistory, FlywayModel> {
    public FlywayBridge() {
        super(RepoType.FLYWAY);
    }

    @Override public FlywayModel convertEntity(FlywaySchemaHistory entity) {
        return new FlywayModel(entity);
    }
}
