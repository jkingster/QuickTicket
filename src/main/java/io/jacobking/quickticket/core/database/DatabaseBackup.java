package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.core.utility.FileIO;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class DatabaseBackup {

    private static final String BACKUP_PATH        = FileIO.TARGET_BACKUP_FOLDER;
    private static final String BACKUP_NAME_FORMAT = "%s\\%s-backup-%s.db";

    private final String databasePath;

    private boolean wasBackedUp = false;

    private DatabaseBackup(final String dbUrl) {
        this.databasePath = dbUrl;
    }

    public static DatabaseBackup init(final String dbUrl) {
        return new DatabaseBackup(dbUrl);
    }

    public void backup() {
        if (FileIO.fileExists(databasePath)) {
            final File sourceFile = new File(databasePath);
            final File destinationFile = getDestinationFile(sourceFile.getName().replaceAll(".db", ""));
            createBackup(sourceFile, destinationFile);
        }
    }

    private void createBackup(final File sourceFile, final File destinationFile) {
        try {
            FileUtils.copyFile(sourceFile, destinationFile);
            this.wasBackedUp = true;
        } catch (IOException e) {
            this.wasBackedUp = false;
        }
    }

    private File getDestinationFile(final String sourceName) {
        final String newName = BACKUP_NAME_FORMAT.formatted(
                BACKUP_PATH,
                sourceName,
                DateUtil.nowAsString(DateUtil.DateFormat.DATE)
        );
        return new File(newName);
    }

    public boolean isBackedUp() {
        return wasBackedUp;
    }
}
