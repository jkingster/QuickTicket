package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.config.impl.SystemConfig;
import io.jacobking.quickticket.core.database.DatabaseSchemaCheck;
import io.jacobking.quickticket.core.reload.ReloadMechanism;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.gui.alert.Alerts;
import io.jacobking.quickticket.gui.alert.Notifications;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.utility.FALoader;
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


public class ConfigurationController extends Controller {

    @FXML private Label     databaseVersionLabel;
    @FXML private TextField databaseUrl;
    @FXML private TextField configFileUrl;
    @FXML private Button    copyDatabaseUrl;
    @FXML private Button    copyConfigUrl;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseUrl.setText(SystemConfig.getInstance().getProperty("db_url"));
        configFileUrl.setText(FileIO.TARGET_PROPERTIES);
        copyDatabaseUrl.setGraphic(FALoader.createDefault(FontAwesome.Glyph.COPY));
        copyConfigUrl.setGraphic(FALoader.createDefault(FontAwesome.Glyph.COPY));
        databaseVersionLabel.setText(String.format("Undefined")); // TODO: DO IT.
    }

    @FXML private void onBackup() {
        final File source = new File(SystemConfig.getInstance().getProperty("db_url"));
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(FileIO.TARGET_BACKUP_FOLDER));

        final String initialName = String.format("%s-backup_%s.db",
                source.getName().replace(".db", ""),
                DateUtil.nowAsString(DateUtil.DateFormat.DATE_TIME_ONE)
        );

        fileChooser.setInitialFileName(initialName);
        fileChooser.setTitle("Save a backup of current database!");
        final File file = fileChooser.showSaveDialog(copyConfigUrl.getScene().getWindow());
        if (file != null && FileIO.copyFile(source, file)) {
            Notifications.showInfo("Backup Created Successfully", "Location: " + file.getPath());
        }
    }

    private void openFile(final File file) {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browseFileDirectory(file);
        } else {
            Notifications.showError("Failed to open backup.", "Desktop is not supported. Report this!");
        }
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
            Alerts.showError(
                    "Failed to import database.",
                    "No valid database was selected.",
                    "Try again."
            );
            return;
        }

        final DatabaseSchemaCheck databaseSchemaCheck = new DatabaseSchemaCheck(file);
        if (!databaseSchemaCheck.isValidDatabase()) {
            Alerts.showError(
                    "Invalid Database",
                    "The selected database did not match the required criteria.",
                    "Please select a valid one."
            );
            return;
        }

        Alerts.showWarningConfirmation("Are you sure you want to import this database?", "This can have negative consequences.")
                .ifPresent(type -> {
                    if (type == ButtonType.YES) {
                        Notifications.showWarning("Importing Database", "Please do not exit QuickTicket...");
                        updateDatabaseLocation(file);
                    }
                });
    }

    private void updateDatabaseLocation(final File file) {
        final String path = file.getAbsolutePath();
        final Object object = SystemConfig.getInstance().putProperty("db_url", path);
        if (object != null) {
            Notifications.showWarning("Config Updated", "The database url was successfully updated.. reloading data.");
            ReloadMechanism.reload();
        }
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
        Notifications.showInfo("Success!", "Path copied to clipboard.");
    }
}
