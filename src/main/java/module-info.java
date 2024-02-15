module io.jacobking.quickticket {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires org.jooq;
    requires org.apache.commons.io;
    requires org.xerial.sqlitejdbc;

    opens io.jacobking.quickticket to javafx.fxml;
    exports io.jacobking.quickticket;

    opens io.jacobking.quickticket.tables.records to org.jooq;
    exports io.jacobking.quickticket.tables.records;

    opens io.jacobking.quickticket.tables.pojos to org.jooq;
    exports io.jacobking.quickticket.tables.pojos;

    opens io.jacobking.quickticket.gui.controller.impl.ticket to javafx.fxml;
    exports io.jacobking.quickticket.gui.controller.impl.ticket;

    opens io.jacobking.quickticket.gui.controller.impl to javafx.fxml;
    exports io.jacobking.quickticket.gui.controller.impl;
}