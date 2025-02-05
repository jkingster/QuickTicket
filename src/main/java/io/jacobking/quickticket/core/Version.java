package io.jacobking.quickticket.core;

public class Version {

    private static final Version CURRENT = new Version(1, 0, 0, "beta");

    private final int    major;
    private final int    minor;
    private final int    patch;
    private final String suffix;

    private Version(final int major, final int minor, final int patch, final String suffix) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.suffix = suffix;
    }

    private static Version VERSION() {
        return CURRENT;
    }

    public static String current() {
        final Version current = VERSION();
        if (current.suffix.isEmpty()) {
            return String.format("v%d.%d.%d", current.major, current.minor, current.patch);
        }
        return current.asString();
    }

    public String asString() {
        return String.format("v%d.%d.%d-%s", major, minor, patch, suffix);
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public String getSuffix() {
        return suffix;
    }
}
