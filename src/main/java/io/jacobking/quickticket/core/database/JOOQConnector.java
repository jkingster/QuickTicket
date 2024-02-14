package io.jacobking.quickticket.core.database;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class JOOQConnector {

    private final DSLContext dslContext;

    public JOOQConnector(final SQLiteConnector connector) {
        this.dslContext = DSL.using(connector.getConnection(), SQLDialect.SQLITE);
    }

    public DSLContext getContext() {
        return dslContext;
    }

}
