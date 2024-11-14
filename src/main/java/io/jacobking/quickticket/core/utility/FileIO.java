package io.jacobking.quickticket.core.utility;


import io.jacobking.quickticket.core.exception.QuickTicketException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class FileIO {

    public static final String TARGET_LOCK = ".lock";

    private FileIO() {
    }

    public static File getDirectoryLocation() {
        if (isWindows()) {
            return new File(System.getenv("APPDATA"), "QuickTicket");
        } else {
            return new File(System.getenv("user.home"), "QuickTicket");
        }
    }

    @SuppressWarnings("all")
    public static void createAppDirectory() {
        final File directory = getDirectoryLocation();
        if (!directory.exists()) {
            final boolean success = directory.mkdir();
            if (success) {
                Logs.info("QuickTicket App Directory Created.");
            }
        }

        createFile("database.db");
        createFile("system.properties");
        createDirectory("migrations");
        createDirectory("backup");
        createDirectory("logs");
        createDirectory("attachments");
    }

    public static boolean createFile(final String path) {
        final File file = new File(getDirectoryLocation(), path);
        if (fileExists(path, false)) {
            return false;
        }
        try {
            final boolean success = file.createNewFile();
            if (success) {
                Logs.info("File Created: {}", path);
            }
            return success;
        } catch (IOException e) {
            Logs.warn("Could not create file: {}", path);
            throw new QuickTicketException("Could not create file: " + path, e.fillInStackTrace());
        }
    }

    public static boolean copyFile(final File source, final File destination) {
        try {
            FileUtils.copyFile(source, destination);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean deleteFile(final String path) {
        final File file = new File(getDirectoryLocation(), path);
        if (!file.isFile() || !file.exists())
            return false;
        return file.delete();
    }

    public static boolean createDirectory(final String path) {
        final File newDirectory = new File(getDirectoryLocation(), path);
        final boolean success = newDirectory.mkdirs();
        if (success) {
            Logs.info("Directory Created: {}", path);
        }
        return success;
    }

    public static boolean fileExists(final String path, final boolean withDirectory) {
        File file;
        if (withDirectory) {
            file = new File(getDirectoryLocation(), path);
        } else {
            file = new File(path);
        }

        final boolean doesFileExist = file.isFile() && file.exists();
        if (doesFileExist) {
            Logs.info("File Exists Check: {}", path);
        }
        return doesFileExist;
    }


    public static String getPath(final String fileName) {
        final File file = new File(getDirectoryLocation().getPath() + File.separator + fileName);
        return file.getPath();
    }

    private static boolean isWindows() {
        final String os = System.getProperty("os.name")
                .toLowerCase(Locale.ENGLISH);
        return os.contains("win");
    }

}
