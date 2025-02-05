package io.jacobking.quickticket.core.config;


import io.jacobking.quickticket.App;
import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.core.utility.Logs;
import io.jacobking.quickticket.gui.alert.Announcements;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.LocalFileHeader;

import java.io.*;
import java.net.URL;

public class SystemConfig extends Config {

    public SystemConfig() {
        super("system.properties");
        copyOverScripts();
    }

    @Override public void setDefaultProperties() {
        putDefaultProperty("database_url", FileIO.getPath("database.db"));
        putDefaultProperty("auto_migration", "true");
        putDefaultProperty("first_launch", "true");
        putDefaultProperty("disable_file_locking", "false");
        putDefaultProperty("developer_mode", "false");
        putDefaultProperty("flyway.url", "jdbc:sqlite:" + FileIO.getPath("database.db"));
        putDefaultProperty("flyway.locations", "filesystem:" + FileIO.getPath("migrations"));
    }

    private void copyOverScripts() {
        final URL zipFileUrl = App.class.getResource("sql/scripts.zip");
        if (zipFileUrl == null) {
            Announcements.get().showErrorOverride("scripts.zip not found!", "Please report this.");
            return;
        }

        LocalFileHeader localFileHeader;
        int readLen;
        byte[] readBuffer = new byte[4096];
        try (final InputStream inputStream = zipFileUrl.openStream();
             final ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            while ((localFileHeader = zipInputStream.getNextEntry()) != null) {
                final File extractedFile = new File(getMigrationPath(localFileHeader.getFileName()));
                try (final OutputStream outputStream = new FileOutputStream(extractedFile)) {
                    while ((readLen = zipInputStream.read(readBuffer)) != -1) {
                        outputStream.write(readBuffer, 0, readLen);
                    }
                }
            }
        } catch (IOException e) {
            Logs.debug(e.fillInStackTrace().getMessage());
        }
    }

    private String getMigrationPath(final String fileName) {
        final String targetDirectory = FileIO.getPath("migrations");
        final File outputFile = new File(targetDirectory + File.separator + fileName);
        return outputFile.getPath();
    }
}
