package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.core.Config;
import io.jacobking.quickticket.core.database.repository.RepoCrud;

import java.sql.SQLException;

public class Database {

    private static final Database instance = new Database();

    private final SQLiteConnector sqLiteConnector;
    private final RepoCrud        repoCrud;

    private Database() {
        this.sqLiteConnector = new SQLiteConnector(Config.getInstance());
        SQLLoader.process(sqLiteConnector.getConnection());
        new DatabaseMigrator(sqLiteConnector.getConnection()).migrate();

        final JOOQConnector jooqConnector = new JOOQConnector(sqLiteConnector);
        this.repoCrud = new RepoCrud(jooqConnector.getContext());
    }

    public static Database getInstance() {
        return instance;
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
