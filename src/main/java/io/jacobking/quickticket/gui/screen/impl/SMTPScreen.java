package io.jacobking.quickticket.gui.screen.impl;

import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.gui.controller.impl.SMTPController;
import io.jacobking.quickticket.gui.screen.Route;
import io.jacobking.quickticket.gui.screen.Screen;
import javafx.stage.Modality;

public class SMTPScreen extends Screen {
    public SMTPScreen() {
        super(Route.SMTP, Modality.APPLICATION_MODAL, new SMTPController());
    }
}
