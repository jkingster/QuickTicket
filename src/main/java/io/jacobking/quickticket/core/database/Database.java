package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.core.Config;
import org.jooq.DSLContext;

public class Database {
    private final JOOQConnector jooqConnector;

    public Database() {
        final SQLiteConnector sqLiteConnector = new SQLiteConnector(Config.getInstance());
        SQLLoader.process(sqLiteConnector.getConnection());

        this.jooqConnector = new JOOQConnector(sqLiteConnector);
    }

    public DSLContext getContext() {
        return jooqConnector.getContext();
    }
}
