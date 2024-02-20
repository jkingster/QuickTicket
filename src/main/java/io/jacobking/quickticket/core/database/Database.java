package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.core.Config;
import io.jacobking.quickticket.core.database.repository.RepoCrud;

import java.sql.SQLException;
import java.sql.SQLInput;

public class Database {

    private static final Database instance = new Database();

    private final SQLiteConnector sqLiteConnector;
    private final JOOQConnector jooqConnector;
    private final RepoCrud repoCrud;

    private Database() {
        this.sqLiteConnector = new SQLiteConnector(Config.getInstance());
        SQLLoader.process(sqLiteConnector.getConnection());

        this.jooqConnector = new JOOQConnector(sqLiteConnector);
        this.repoCrud = new RepoCrud(jooqConnector.getContext());
    }

    public void close() {
        try {
            sqLiteConnector.getConnection().close();
        } catch (SQLException e) {
            // TODO: alert
        }
    }
    public static Database getInstance() {
        return instance;
    }

    public static RepoCrud call() {
        return getInstance().repoCrud;
    }
}
