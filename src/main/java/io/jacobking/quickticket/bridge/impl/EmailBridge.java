package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.EmailModel;
import io.jacobking.quickticket.tables.pojos.EmailInterface;

public class EmailBridge extends Bridge<EmailInterface, EmailModel> {
    public EmailBridge(final Database database) {
        super(database, RepoType.EMAIL);
    }

    public EmailInterface getEmail() {
        return getModel(0).toEntity();
    }

    public EmailModel getEmailModel() {
        return getModel(0);
    }

    @Override
    public EmailModel convertEntity(EmailInterface entity) {
        return new EmailModel(entity);
    }
}
