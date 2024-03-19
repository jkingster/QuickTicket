package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.JournalModel;
import io.jacobking.quickticket.tables.pojos.Journal;

public class JournalBridge extends Bridge<Journal, JournalModel> {
    public JournalBridge() {
        super(RepoType.JOURNAL);
    }

    @Override public JournalModel convertEntity(Journal entity) {
        return new JournalModel(entity);
    }
}
