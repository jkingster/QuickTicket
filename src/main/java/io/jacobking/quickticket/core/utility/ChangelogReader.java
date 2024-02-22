package io.jacobking.quickticket.core.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jacobking.quickticket.App;
import io.jacobking.quickticket.gui.alert.Notify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChangelogReader {

    private static final String CHANGELOG_PATH = "changelog.json";

    private static final ChangelogReader instance = new ChangelogReader();

    private List<Changelog> changes;

    private ChangelogReader() {
        final ObjectMapper mapper = new ObjectMapper();
        readChanges(mapper);
    }

    private void readChanges(final ObjectMapper mapper) {
        try {
            this.changes = mapper.readValue(App.class.getResource(CHANGELOG_PATH), new TypeReference<>(){});
        } catch (IOException e) {
            Notify.showException("Failed to read changelog.json", e.fillInStackTrace());
        }
    }

    public static List<Changelog> getChanges() {
        return instance.getLogs();
    }

    public List<Changelog> getLogs() {
        return changes;
    }

    public static class Changelog {
        private String       version;
        private List<String> changes;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public List<String> getChanges() {
            return changes;
        }

        public void setChanges(List<String> changes) {
            this.changes = changes;
        }
    }

}
