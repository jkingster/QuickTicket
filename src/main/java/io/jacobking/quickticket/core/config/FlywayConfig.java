package io.jacobking.quickticket.core.config;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.core.utility.Logs;
import io.jacobking.quickticket.gui.alert.Alerts;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class FlywayConfig extends Config {

    public FlywayConfig() {
        super("flyway.properties");
        copyOverScripts();
    }

    @Override public void putDefaultProperties() {
        setProperty("flyway.url", "jdbc:sqlite:" + FileIO.getPath("database.db"));
        setProperty("flyway.locations", "filesystem:" + FileIO.getPath("migrations"));
    }

    private void copyOverScripts() {
        final URL zipFileUrl = App.class.getResource("sql/scripts.zip");
        if (zipFileUrl == null) {
            Alerts.showErrorOverride("scripts.zip not found!", "Please report this.");
            return;
        }

        try (final InputStream inputStream = zipFileUrl.openStream()) {
            if (inputStream == null) {
                Alerts.showErrorOverride("Could not open scripts.zip stream!", "Please report this.");
                return;
            }
            final ZipInputStream zipInputStream = new ZipInputStream(inputStream);

            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                final String name = zipEntry.getName();
                copyScriptOver(zipInputStream, name);
            }

        } catch (IOException e) {
            Logs.warn("IOException: {}", e.fillInStackTrace());
        }
    }

    private void copyScriptOver(final ZipInputStream zipInputStream, final String name) {
        final String targetDirectory = FileIO.getPath("migrations");
        final File outputFile = new File(targetDirectory + File.separator + name);

        try {
            FileUtils.copyInputStreamToFile(zipInputStream, outputFile);
        } catch (IOException e) {
            Logs.warn("IOException: {}", e.fillInStackTrace());
        }
    }

}
