package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.utility.MiscUtil;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;


public class WelcomeController extends Controller {
    private static final String HOW_TO_LINK = "";
    private static final String TROUBLESHOOT_LINK = "";


    @Override public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML private void openHowTo() {
        MiscUtil.openLink(HOW_TO_LINK);
    }

    @FXML private void openTroubleshoot() {
        MiscUtil.openLink(TROUBLESHOOT_LINK);
    }
}
