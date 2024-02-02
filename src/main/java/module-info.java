module io.jacobking.quickticket {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens io.jacobking.quickticket to javafx.fxml;
    exports io.jacobking.quickticket;

    opens io.jacobking.quickticket.gui.controller.impl to javafx.fxml;
    exports io.jacobking.quickticket.gui.controller.impl;
}