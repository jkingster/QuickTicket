package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.TicketCategoryModel;
import io.jacobking.quickticket.tables.pojos.TicketCategories;

public class CategoryBridge extends Bridge<TicketCategories, TicketCategoryModel> {
    public CategoryBridge(final Database database) {
        super(database, RepoType.CATEGORY);
    }

    @Override public TicketCategoryModel convertEntity(TicketCategories entity) {
        return new TicketCategoryModel(entity);
    }
}
