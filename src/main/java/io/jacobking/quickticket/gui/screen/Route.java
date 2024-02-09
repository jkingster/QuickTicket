package io.jacobking.quickticket.gui.screen;

public enum Route {

    DASHBOARD("Dashboard"),
    VIEWER("Viewer");

    private static final Route[] VALUES = values();
    private static final String BASE_PATH = "fxml/%s";
    private final String name;

    Route(final String name) {
        this.name = name;
    }

    public static Route[] getValues() {
        return VALUES;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return BASE_PATH.formatted(name.concat(".fxml").toLowerCase());
    }
}
