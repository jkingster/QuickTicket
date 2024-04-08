package io.jacobking.quickticket.core.config.impl;

import io.jacobking.quickticket.core.config.Config;
import io.jacobking.quickticket.core.utility.FileIO;

public class SystemConfig extends Config {

    public SystemConfig() {
        super(FileIO.TARGET_PROPERTIES);
        checkForConfig();
        checkFileSystem();
    }

    @Override public void putDefaults() {
        putProperty("db_url", FileIO.TARGET_DATABASE);
        putProperty("auto_migrate", "true");
    }

    private void checkFileSystem() {
        if (!FileIO.directoryExists(FileIO.TARGET_DIRECTORY)) {
            FileIO.createDirectory(FileIO.TARGET_DIRECTORY);
        }

        if (!FileIO.directoryExists(FileIO.TARGET_BACKUP_FOLDER)) {
            FileIO.createDirectory(FileIO.TARGET_BACKUP_FOLDER);
        }

        if (!FileIO.directoryExists(FileIO.TARGET_SQL_FOLDER)) {
            FileIO.createDirectory(FileIO.TARGET_SQL_FOLDER);
        }

        if (!FileIO.fileExists(FileIO.TARGET_DATABASE)) {
            FileIO.createFile(FileIO.TARGET_DATABASE);
        }
    }
}
