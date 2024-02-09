module io.jacobking.quickticket {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.h2database;

    opens io.jacobking.quickticket to javafx.fxml;
    exports io.jacobking.quickticket;

    opens io.jacobking.quickticket.gui.controller.impl to javafx.fxml;
    exports io.jacobking.quickticket.gui.controller.impl;
}