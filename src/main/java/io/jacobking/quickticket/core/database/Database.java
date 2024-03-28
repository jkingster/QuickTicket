package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.core.config.impl.SystemConfig;
import io.jacobking.quickticket.core.database.repository.RepoCrud;

import java.sql.SQLException;

public class Database {

    private static Database instance = null;

    private final SQLiteConnector sqLiteConnector;
    private final RepoCrud        repoCrud;

    private Database() {
        this.sqLiteConnector = new SQLiteConnector(SystemConfig.getInstance());
        SQLLoader.process(sqLiteConnector.getConnection());
        FlywayMigrator.migrate();

        final JOOQConnector jooqConnector = new JOOQConnector(sqLiteConnector);
        this.repoCrud = new RepoCrud(jooqConnector.getContext());
    }

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    public static void rebuildInstance() {
        instance = null;
        getInstance();
    }

    public static RepoCrud call() {
        return getInstance().repoCrud;
    }

    public void close() {
        try {
            sqLiteConnector.getConnection().close();
        } catch (SQLException e) {
            // TODO: alert
        }
    }
}
