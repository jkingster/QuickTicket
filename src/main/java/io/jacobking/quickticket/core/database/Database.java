package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.core.Config;
import io.jacobking.quickticket.core.database.repository.RepoCrud;

public class Database {

    private static final Database instance = new Database();
    private final RepoCrud repoCrud;

    private Database() {
        final SQLiteConnector sqLiteConnector = new SQLiteConnector(Config.getInstance());
        SQLLoader.process(sqLiteConnector.getConnection());

        final JOOQConnector jooqConnector = new JOOQConnector(sqLiteConnector);
        this.repoCrud = new RepoCrud(jooqConnector.getContext());
    }

    public static Database getInstance() {
        return instance;
    }

    public static RepoCrud call() {
        return getInstance().repoCrud;
    }
}
