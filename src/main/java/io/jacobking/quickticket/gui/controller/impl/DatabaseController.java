package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.Config;
import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.database.Database;
import io.jacobking.quickticket.core.database.DatabaseMigrator;
import io.jacobking.quickticket.core.database.DatabaseSchemaCheck;
import io.jacobking.quickticket.core.lock.InstanceLock;
import io.jacobking.quickticket.core.restart.RestartLatch;
import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.gui.alert.Notify;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.utility.FALoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.controlsfx.glyphfont.FontAwesome;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class DatabaseController extends Controller {

    @FXML private Label     databaseVersionLabel;
    @FXML private TextField databaseUrl;
    @FXML private TextField configFileUrl;
    @FXML private Button    copyDatabaseUrl;
    @FXML private Button    copyConfigUrl;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseUrl.setText(Config.getInstance().readProperty("db_url"));
        configFileUrl.setText(FileIO.TARGET_PROPERTIES);
        copyDatabaseUrl.setGraphic(FALoader.createDefault(FontAwesome.Glyph.COPY));
        copyConfigUrl.setGraphic(FALoader.createDefault(FontAwesome.Glyph.COPY));
        databaseVersionLabel.setText(String.format("Database Version: %s", DatabaseMigrator.getCurrentDatabaseVersion()));
    }

    @FXML private void onUpdate() {

    }

    @FXML private void onImport() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a SQLite database file.");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Database", "*.db"),
                new FileChooser.ExtensionFilter("SQLite Database", "*.sqlite"),
                new FileChooser.ExtensionFilter("All Databases", "*.db", "*.sqlite")
        );
        fileChooser.setInitialDirectory(new File(FileIO.TARGET_DIRECTORY));

        final File file = fileChooser.showOpenDialog(copyConfigUrl.getScene().getWindow());
        if (file == null || !file.exists()) {
            Notify.showError(
                    "Failed to import database.",
                    "No valid database was selected.",
                    "Try again."
            );
            return;
        }

        final DatabaseSchemaCheck databaseSchemaCheck = new DatabaseSchemaCheck(file);
        if (!databaseSchemaCheck.isValidDatabase()) {
            Notify.showError(
                    "Invalid Database",
                    "The selected database did not match the required criteria.",
                    "Please select a valid one."
            );
            return;
        }

        Notify.showWarningConfirmation("Are you sure you want to import this database?", "This can have negative consequences.")
                .ifPresent(type -> {
                    if (type == ButtonType.YES) {
                        updateDatabaseLocation(file);
                    }
                });
    }

    private void updateDatabaseLocation(final File file) {
        final String path = file.getAbsolutePath();
        Config.getInstance().setProperty("db_url", path);
        RestartLatch.restartApp(QuickTicket::gracefulClose, null);
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
