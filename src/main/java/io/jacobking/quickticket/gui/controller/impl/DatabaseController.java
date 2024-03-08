package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.Config;
import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.utility.FALoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.controlsfx.glyphfont.FontAwesome;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.ResourceBundle;


public class DatabaseController extends Controller {

    @FXML private Label     databaseVersionLabel;
    @FXML private TextField databaseUrl;
    @FXML private TextField configFileUrl;
    @FXML private CheckBox  journalModeCheckBox;
    @FXML private CheckBox  synchronousCheckBox;
    @FXML private CheckBox  journalSizeCheckBox;
    @FXML private Button    copyDatabaseUrl;
    @FXML private Button    copyConfigUrl;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseUrl.setText(Config.getInstance().readProperty("db_url"));
        configFileUrl.setText(FileIO.TARGET_PROPERTIES);
        copyDatabaseUrl.setGraphic(FALoader.createDefault(FontAwesome.Glyph.COPY));
        copyConfigUrl.setGraphic(FALoader.createDefault(FontAwesome.Glyph.COPY));
    }

    @FXML private void onUpdate() {

    }

    @FXML
    private void onCopyDatabaseUrl() {
        copyToClipboard(databaseUrl.getText());
    }

    @FXML private void onCopyConfigUrl() {
        copyToClipboard(configFileUrl.getText());
    }

    private void copyToClipboard(final String stringToCopy) {
        final StringSelection stringSelection = new StringSelection(stringToCopy);
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}
