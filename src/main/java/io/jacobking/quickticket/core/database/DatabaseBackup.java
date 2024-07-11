package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.core.utility.FileIO;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class DatabaseBackup {

    private static final String BACKUP_PATH        = FileIO.getPath("backup");
    private static final String BACKUP_NAME_FORMAT = BACKUP_PATH + File.separator + "%s-backup-%s.db";

    private final File    source;
    private       File    destination;
    private       boolean successful;

    public DatabaseBackup(final String databaseUrl) {
        this.source = new File(databaseUrl);
    }

    public DatabaseBackup setDestination() {
        this.destination = new File(BACKUP_NAME_FORMAT.formatted(
                source.getName().replaceAll(".db", ""),
                DateUtil.nowAsString(DateUtil.DateFormat.DATE_TIME_TWO)
        ));
        return this;
    }

    public DatabaseBackup buildBackup() {
        try {
            FileUtils.copyFile(source, destination);
        } catch (IOException e) {
            this.successful = false;
        }
        return this;
    }

    public boolean isSuccessful() {
        final long sourceSize = FileUtils.sizeOf(source);
        final long destinationSize = FileUtils.sizeOf(destination);
        if (sourceSize != destinationSize)
            return false;

        if (!successful)
            return false;

        return FileIO.fileExists(destination.getPath(), true);
    }
}
