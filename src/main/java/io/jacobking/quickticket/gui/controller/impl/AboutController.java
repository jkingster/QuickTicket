package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.Version;
import io.jacobking.quickticket.gui.alert.Notify;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.screen.Display;
import io.jacobking.quickticket.gui.screen.Route;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController extends Controller {

    private static final String GITHUB_URL     = "https://github.com/jkingster/QuickTicket";
    private static final String SUGGESTION_URL = "https://github.com/jkingster/QuickTicket/issues/new?title=Suggestion:";
    private static final String BUG_REPORT_URL = "https://github.com/jkingster/QuickTicket/issues/new?title=Bug%20Report:";

    @FXML private Label versionLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        versionLabel.setText(Version.current());
    }

    @FXML
    private void onGitHub() {
        openWebpage(GITHUB_URL);
    }

    @FXML
    private void onSuggestion() {
        openWebpage(SUGGESTION_URL);
    }

    @FXML
    private void onBugReport() {
        openWebpage(BUG_REPORT_URL);
    }

    @FXML
    private void onChangelog() {
        Display.show(Route.CHANGELOG);
    }

    private void openWebpage(final String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (URISyntaxException | IOException e) {
            Notify.showException("Failed to open web browser.", e.fillInStackTrace());
        }
    }

}
