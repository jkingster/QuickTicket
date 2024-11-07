package io.jacobking.quickticket.gui.utility;

import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public class IconLoader {

    private static final GlyphFont GLYPH              = GlyphFontRegistry.font("FontAwesome");
    private static final int       DEFAULT_ICON_SIZE  = 17;
    private static final Color     DEFAULT_ICON_COLOR = Color.WHITE;

    private IconLoader() {

    }

    public static Glyph create(final FontAwesome.Glyph glyph, final Color color) {
        if (color == null)
            return GLYPH.create(glyph);
        return GLYPH.create(glyph).color(color);
    }

    public static Glyph createDefault(final FontAwesome.Glyph glyph) {
        return create(glyph, Color.WHITE);
    }

    public static FontIcon getMaterialIcon(final Ikon icon, final Color color, final int iconSize) {
        final var nodeIcon = FontIcon.of(icon);
        nodeIcon.setIconColor(color);
        nodeIcon.setIconSize(iconSize);
        return nodeIcon;
    }

    public static FontIcon getMaterialIcon(final Ikon icon) {
        return getMaterialIcon(icon, DEFAULT_ICON_COLOR, DEFAULT_ICON_SIZE);
    }

    public static FontIcon getMaterialIcon(final FontIcon fontIcon) {
        fontIcon.setIconColor(DEFAULT_ICON_COLOR);
        fontIcon.setIconSize(DEFAULT_ICON_SIZE);
        return fontIcon;
    }
}
