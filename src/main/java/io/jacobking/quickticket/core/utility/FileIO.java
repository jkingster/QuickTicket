package io.jacobking.quickticket.core.utility;

import java.io.File;
import java.io.IOException;

public class FileIO {

    private static final String DIRECTORY_NAME    = "QuickTicket";
    private static final String APP_DATA          = System.getenv("APPDATA");
    private static final String FILE_SEPARATOR    = File.separator;
    public static final  String TARGET_DIRECTORY  = String.format("%s%s%s", APP_DATA, FILE_SEPARATOR, DIRECTORY_NAME);
    public static final  String TARGET_PROPERTIES = String.format("%s%s%s", TARGET_DIRECTORY, FILE_SEPARATOR, "sysconfig.properties");

    public static final String TARGET_DATABASE = String.format("%s%s%s", TARGET_DIRECTORY, FILE_SEPARATOR, "database.db");

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
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createDirectory(final String path) {
        Checks.notEmpty(path, "Directory Path");
        return new File(path).mkdir();
    }

}
