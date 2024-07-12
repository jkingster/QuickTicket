package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.core.utility.DateUtil;
import io.jacobking.quickticket.core.utility.FileIO;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DatabaseBackup {

    private final File source;
    private final File destination;

    public DatabaseBackup(final File source) {
        this.source = source;
        this.destination = new File(getBackupName(source.getName().replace(".db", "").toUpperCase()));
    }

    public boolean backup() {
        final Path sourcePath = Paths.get(source.getPath());
        final Path destinationPath = Paths.get(destination.getPath());
        try {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            final long sourceSize = FileUtils.sizeOf(source);
            final long destinationSize = FileUtils.sizeOf(destination);
            return sourceSize == destinationSize;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getBackupName(final String fileName) {
        return String.format("%s\\DB-%s-BACKUP_%s.db",
                FileIO.getPath("backup"), fileName, DateUtil.nowAsString(DateUtil.DateFormat.DATE_TIME_TWO));
    }

}
