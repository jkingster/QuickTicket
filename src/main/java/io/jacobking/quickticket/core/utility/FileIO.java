package io.jacobking.quickticket.core.utility;

import io.jacobking.quickticket.gui.alert.Alerts;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileIO {

    private static final String DIRECTORY_NAME           = "QuickTicket";
    private static final String APP_DATA                 = System.getenv("APPDATA");
    private static final String FILE_SEPARATOR           = File.separator;
    public static final  String TARGET_DIRECTORY         = String.format("%s%s%s", APP_DATA, FILE_SEPARATOR, DIRECTORY_NAME);
    public static final  String TARGET_PROPERTIES        = String.format("%s%s%s", TARGET_DIRECTORY, FILE_SEPARATOR, "sysconfig.properties");
    public static final  String TARGET_FLYWAY_PROPERTIES = String.format("%s%s%s", TARGET_DIRECTORY, FILE_SEPARATOR, "flyway.properties");
    public static final  String TARGET_LOCK              = String.format("%s%s%s", TARGET_DIRECTORY, FILE_SEPARATOR, ".lock");
    public static final  String TARGET_DATABASE          = String.format("%s%s%s", TARGET_DIRECTORY, FILE_SEPARATOR, "database.db");
    public static final  String TARGET_BACKUP_FOLDER     = String.format("%s%s%s", TARGET_DIRECTORY, FILE_SEPARATOR, "backup");
    public static final  String TARGET_SQL_FOLDER        = String.format("%s%s%s", TARGET_DIRECTORY, FILE_SEPARATOR, "SQL");
    public static final  String TARGET_COPY_PATH         = String.format("%s%s", TARGET_BACKUP_FOLDER, FILE_SEPARATOR);

    private FileIO() {

    }

    public static boolean fileExists(final String path) {
        Checks.notEmpty(path, "File Path");
        final File file = new File(path);
        return file.exists() && file.isFile();
    }

    public static boolean directoryExists(final String path) {
        Checks.notEmpty(path, "Directory Path");
        final File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    public static boolean createFile(final String path) {
        Checks.notEmpty(path, "File Path");
        final File file = new File(path);
        try {
            return file.createNewFile();
        } catch (IOException e) {
            Alerts.showException("Failed to create file.", e.fillInStackTrace());
            return false;
        }
    }

    public static boolean createFile(final File file) {
        Checks.notNull(file, "File");
        try (final FileWriter ignored = new FileWriter(file)) {
            return true;
        } catch (IOException e) {
            Alerts.showException("Failed to create file.", e.fillInStackTrace());
            return false;
        }
    }

    public static boolean copyFile(final File source, final File destination) {
        try {
            FileUtils.copyFile(source, destination);
            return true;
        } catch (IOException e) {
            Alerts.showException("Failed to copy file.", e.fillInStackTrace());
            return false;
        }
    }

    public static boolean createDirectory(final String path) {
        Checks.notEmpty(path, "Directory Path");
        return new File(path).mkdir();
    }

    public static boolean deleteFile(final String path) {
        Checks.notEmpty(path, "File Path");
        return new File(path).delete();
    }

}
