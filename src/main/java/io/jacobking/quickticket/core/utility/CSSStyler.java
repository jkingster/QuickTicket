package io.jacobking.quickticket.core.utility;

public class CSSStyler {

    private final StringBuilder styles = new StringBuilder();

    private CSSStyler(final String... styles) {
        for (final String style : styles) {
            this.styles.append(style).append(";");
        }
    }

    public static CSSStyler create(final String... styles) {
        return new CSSStyler(styles);
    }

    public CSSStyler appendStyle(final String style) {
        Checks.notEmpty(style, "CSS Style");
        this.styles.append(style).append(";");
        return this;
    }

    public String toStyle() {
        return styles.toString();
    }
}
