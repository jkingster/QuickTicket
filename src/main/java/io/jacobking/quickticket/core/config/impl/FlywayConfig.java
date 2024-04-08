package io.jacobking.quickticket.core.config.impl;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.core.config.Config;
import io.jacobking.quickticket.core.utility.FileIO;
import io.jacobking.quickticket.gui.alert.Alerts;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FlywayConfig extends Config {
    private static final String SOURCE_PATH = FileIO.TARGET_SQL_FOLDER;

    public FlywayConfig() {
        super(FileIO.TARGET_FLYWAY_PROPERTIES);
        checkForConfig();
        copyOverScripts();
    }

    @Override public void putDefaults() {
        putProperty("flyway.url", String.format("jdbc:sqlite:%s", FileIO.TARGET_DATABASE));
        putProperty("flyway.locations", "filesystem:" + SOURCE_PATH);
    }

    private void copyOverScripts() {
        try {
            final URL resource = App.class.getResource("sql/migration");
            if (resource == null) {
                return;
            }

            final URI uri = resource.toURI();
            final Path path = Paths.get(uri);
            walkPath(path);
        } catch (URISyntaxException e) {
            Alerts.showException("Failed to copy over migration scripts.", e.fillInStackTrace());
        }
    }

    private void walkPath(final Path path) {
        try (final Stream<Path> walker = Files.walk(path)) {
            walker.filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        copyFile(filePath.toFile());
                    });
        } catch (IOException e) {
            Alerts.showException("Failed to walk migration path.", e.fillInStackTrace());
        }
    }

    private void copyFile(final File file) {
        final String fileName = file.getName();
        final File source = new File(String.format("%s/%s", SOURCE_PATH, fileName));
        try {
            FileUtils.copyFile(file, source);
        } catch (IOException e) {
            Alerts.showException("Failed to copy file over.", e.fillInStackTrace());
        }
    }
}
