package io.jacobking.quickticket.core.database;

import io.jacobking.quickticket.App;
import io.jacobking.quickticket.gui.alert.Alerts;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLLoader {

    public static final String   DELIMITER = "\\n\\s*\\n";
    private static final String[] PATHS     = {"sql/base.sql"};

    private final Connection connection;

    private SQLLoader(final Connection connection) {
        this.connection = connection;
    }

    public static void process(final Connection connection) {
        if (connection != null) {
            new SQLLoader(connection).execute();
        }
    }

    private void execute() {
        for (final String path : PATHS) {
            processPath(path);
        }
    }

    private void processPath(final String path) {
        try (final InputStream stream = readFile(path)) {
            final String convertedStream = getInputStreamAsString(stream);
            if (convertedStream.isEmpty())
                return;

            tokenize(convertedStream);
        } catch (IOException e) {
            Alerts.showException("Failed to process path.", e.fillInStackTrace());
        }
    }

    private void tokenize(final String convertedStream) {
        final String[] tokens = convertedStream.split(DELIMITER);
        if (tokens.length == 0)
            return;

        executeQueries(tokens);
    }

    private void executeQueries(final String[] tokens) {
        for (final String query : tokens) {
            executeQuery(query.trim());
        }
    }

    private void executeQuery(final String query) {
        try (final Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            Alerts.showException("Failed to execute query.", e.fillInStackTrace());
        }
    }

    private InputStream readFile(final String path) throws IOException {
        final URL url = App.class.getResource(path);
        if (url != null) {
            return url.openStream();
        }
        throw new RuntimeException("Could not read input stream.");
    }

    private String getInputStreamAsString(final InputStream inputStream) {
        try {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

}