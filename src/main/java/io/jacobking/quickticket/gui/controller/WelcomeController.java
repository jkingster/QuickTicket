package io.jacobking.quickticket.gui.controller;

import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.gui.Controller;
import io.jacobking.quickticket.core.utility.MiscUtil;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;


public class WelcomeController extends Controller {
    private static final String HOW_TO_LINK       = "https://github.com/jkingster/QuickTicket/blob/master/docs/how-to/index.md";
    private static final String TROUBLESHOOT_LINK = "https://github.com/jkingster/QuickTicket/blob/master/docs/troubleshooting/index.md";


    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        QuickTicket.getInstance()
                .getSystemConfig()
                .setPropertyAndStore("first_launch", "false");
    }

    @FXML private void openHowTo() {
        MiscUtil.openLink(HOW_TO_LINK);
    }

    @FXML private void openTroubleshoot() {
        MiscUtil.openLink(TROUBLESHOOT_LINK);
    }
}
