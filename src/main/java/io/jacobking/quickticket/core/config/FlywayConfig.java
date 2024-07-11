package io.jacobking.quickticket.core.config;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.core.utility.Logs;
import io.jacobking.quickticket.gui.alert.Alerts;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.LocalFileHeader;

import java.io.*;
import java.net.URL;
import java.util.Properties;


public class FlywayConfig extends Config {

    public FlywayConfig() {
        super("flyway.properties");
        copyOverScripts();
    }

    @Override public void setDefaultProperties() {
          putDefaultProperty("flyway.url", "jdbc:sqlite:" + FileIO.getPath("database.db"));
          putDefaultProperty("flyway.locations", "filesystem:" + FileIO.getPath("migrations"));
    }

//    private void copyOverScripts() {
//        final URL zipFileUrl = App.class.getResource("sql/scripts.zip");
//        if (zipFileUrl == null) {
//            Alerts.get().showErrorOverride("scripts.zip not found!", "Please report this.");
//            return;
//        }
//
//        try (final InputStream inputStream = zipFileUrl.openStream();
//             final ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
//            ZipEntry zipEntry;
//            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
//                final String name = zipEntry.getName();
//                Logs.debug("Zip Name: {}", name);
//                copyScriptOver(zipInputStream, name);
//                zipInputStream.closeEntry();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private void copyScriptOver(final ZipInputStream zipInputStream, final String name) {
//        final String targetDirectory = FileIO.getPath("migrations");
//        final File outputFile = new File(targetDirectory + File.separator + name);
//
//        try {
//            FileUtils.copyInputStreamToFile(zipInputStream, outputFile);
//        } catch (IOException e) {
//            Logs.warn("IOException: {}", e.fillInStackTrace());
//        }


    private void copyOverScripts() {
        final URL zipFileUrl = App.class.getResource("sql/scripts.zip");
        if (zipFileUrl == null) {
            Alerts.get().showErrorOverride("scripts.zip not found!", "Please report this.");
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





