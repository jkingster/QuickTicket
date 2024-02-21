package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.EmailModel;
import io.jacobking.quickticket.tables.pojos.Email;

public class EmailBridge extends Bridge<Email, EmailModel> {
    public EmailBridge() {
        super(RepoType.EMAIL);
    }

    public Email getEmail() {
        return getModel(0).toEntity();
    }

    @Override
    public EmailModel convertEntity(Email entity) {
        return new EmailModel(entity);
    }
}
