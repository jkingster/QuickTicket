package io.jacobking.quickticket.core;

public class Version {

    private static final Version CURRENT = new Version(2, 1, 0, "beta");

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

    public static String current() {
        return CURRENT.asString();
    }

    public String asString() {
        return String.format("v%d.%d.%d-%s", major, minor, patch, suffix);
    }

}
