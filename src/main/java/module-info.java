module io.jacobking.quickticket {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires java.sql;
    requires org.controlsfx.controls;
    requires org.jooq;
    requires org.apache.commons.io;
    requires org.xerial.sqlitejdbc;
    requires java.desktop;
    requires com.google.gson;
    requires org.slf4j;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.material2;
    requires org.kordamp.ikonli.materialdesign;
    requires flyway.core;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires zip4j;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;

    opens io.jacobking.quickticket to javafx.fxml;
    exports io.jacobking.quickticket;

    opens io.jacobking.quickticket.core.utility to com.fasterxml.jackson.databind, com.fasterxml.jackson.core;
    exports io.jacobking.quickticket.core.utility;

    opens io.jacobking.quickticket.tables.records to org.jooq;
    exports io.jacobking.quickticket.tables.records;

    opens io.jacobking.quickticket.tables.pojos to org.jooq;
    exports io.jacobking.quickticket.tables.pojos;

    opens io.jacobking.quickticket.gui.controller to javafx.fxml;
    exports io.jacobking.quickticket.gui.controller;

    opens io.jacobking.quickticket.gui.custom to javafx.fxml;
    exports io.jacobking.quickticket.gui.custom;

}