package io.jacobking.quickticket.gui.controller.impl;

import io.jacobking.quickticket.core.QuickTicket;
import io.jacobking.quickticket.core.config.impl.SystemConfig;
import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.gui.alert.Alerts;
import io.jacobking.quickticket.gui.alert.Notifications;
import io.jacobking.quickticket.gui.controller.Controller;
import io.jacobking.quickticket.gui.model.impl.FlywayModel;
import io.jacobking.quickticket.gui.utility.FALoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.controlsfx.glyphfont.FontAwesome;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ConfigurationController extends Controller {

    @FXML private TableView<FlywayModel>           flywayTable;
    @FXML private TableColumn<FlywayModel, String> versionColumn;
    @FXML private TableColumn<FlywayModel, String> descriptionColumn;
    @FXML private TableColumn<FlywayModel, String> typeColumn;
    @FXML private TableColumn<FlywayModel, String> scriptColumn;
    @FXML private TableColumn<FlywayModel, String> checksumColumn;
    @FXML private TableColumn<FlywayModel, String> installedOnColumn;

    @FXML private TextField configurationField;
    @FXML private TextField autoMigrateField;
    @FXML private TextField databaseField;
    @FXML private TextField flywayField;
    @FXML private TextField migrationField;
    @FXML private TextField backupField;

    @FXML private Button copyConfigButton;
    @FXML private Button openConfigButton;
    @FXML private Button copyDatabaseButton;
    @FXML private Button openDatabaseButton;
    @FXML private Button copyFlywayButton;
    @FXML private Button openFlywayButton;
    @FXML private Button copyMigrationButton;
    @FXML private Button openMigrationButton;
    @FXML private Button copyBackupButton;
    @FXML private Button openBackupButton;


    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTable();
        configureButtons();

        configurationField.setText(FileIO.TARGET_PROPERTIES);
        autoMigrateField.setText(core.getSystemConfig().getProperty("db_url"));
        databaseField.setText(core.getSystemConfig().getProperty("auto_migraton"));
        flywayField.setText(FileIO.TARGET_FLYWAY_PROPERTIES);
        migrationField.setText(FileIO.TARGET_SQL_FOLDER);
        backupField.setText(FileIO.TARGET_BACKUP_FOLDER);
    }

    private void configureTable() {
        versionColumn.setCellValueFactory(data -> data.getValue().versionProperty());
        descriptionColumn.setCellValueFactory(data -> data.getValue().descriptionProperty());
        typeColumn.setCellValueFactory(data -> data.getValue().typeProperty());
        scriptColumn.setCellValueFactory(data -> data.getValue().scriptProperty());
        checksumColumn.setCellValueFactory(data -> data.getValue().checksumProperty().asString());
        installedOnColumn.setCellValueFactory(data -> data.getValue().installedOnProperty());
        flywayTable.setItems(flyway.getObservableList());
    }

    @FXML private void onImport() {

    }

    @FXML private void onBackup() {
        final File source = new File(core.getSystemConfig().getProperty("db_url"));
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(FileIO.TARGET_BACKUP_FOLDER));

        final String initialName = String.format("%s-backup_%s.db",
                source.getName().replace(".db", ""),
                DateUtil.nowAsString(DateUtil.DateFormat.DATE_TIME_TWO)
        );

        fileChooser.setInitialFileName(initialName);
        fileChooser.setTitle("Save a backup of current database!");
        final File file = fileChooser.showSaveDialog(copyConfigButton.getScene().getWindow());
        if (file != null && FileIO.copyFile(source, file)) {
            Notifications.showInfo("Backup Created Successfully", "Location: " + file.getPath());
        }
    }

    private void configureButtons() {
        setButtonAsCopyGraphic(copyConfigButton);
        setButtonAsOpenGraphic(openConfigButton);

        setButtonAsCopyGraphic(copyDatabaseButton);
        setButtonAsOpenGraphic(openDatabaseButton);

        setButtonAsCopyGraphic(copyFlywayButton);
        setButtonAsOpenGraphic(openFlywayButton);

        setButtonAsCopyGraphic(copyMigrationButton);
        setButtonAsOpenGraphic(openMigrationButton);

        setButtonAsCopyGraphic(copyBackupButton);
        setButtonAsOpenGraphic(openBackupButton);
    }

    @FXML private void copyConfigPath() {
        copyToClipBoard(configurationField.getText());
    }

    @FXML private void openConfigPath() {
        openPath(configurationField.getText());
    }

    @FXML private void onCopyDatabase() {
        copyToClipBoard(core.getSystemConfig().getProperty("db_url"));
    }

    @FXML private void onOpenDatabase() {
        openPath(core.getSystemConfig().getProperty("db_url"));
    }

    @FXML private void onCopyFlyway() {
        copyToClipBoard(FileIO.TARGET_FLYWAY_PROPERTIES);
    }

    @FXML private void onOpenFlyway() {
        openPath(FileIO.TARGET_FLYWAY_PROPERTIES);
    }

    @FXML private void onCopyMigration() {
        copyToClipBoard(FileIO.TARGET_SQL_FOLDER);
    }

    @FXML private void onOpenMigration() {
        openPath(FileIO.TARGET_SQL_FOLDER);
    }

    @FXML private void onCopyBackup() {
        copyToClipBoard(FileIO.TARGET_BACKUP_FOLDER);
    }

    @FXML private void onOpenBackup() {
        openPath(FileIO.TARGET_BACKUP_FOLDER);
    }

    private void copyToClipBoard(final String text) {
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new StringSelection(text), null);
        Notifications.showInfo("Success!", "Copied to clipboard successfully.");
    }

    private void openPath(final String path) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(new File(path));
            } catch (IOException e) {
                Alerts.showException("Failed to open file. Make sure you have a valid application to read the targeted file type.", e.fillInStackTrace());
            }
        }
    }

    private void setButtonAsCopyGraphic(final Button button) {
        button.setGraphic(FALoader.createDefault(FontAwesome.Glyph.COPY));
    }

    private void setButtonAsOpenGraphic(final Button button) {
        button.setGraphic(FALoader.createDefault(FontAwesome.Glyph.FOLDER_OPEN));
    }
}
