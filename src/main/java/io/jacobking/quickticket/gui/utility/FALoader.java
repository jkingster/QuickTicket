package io.jacobking.quickticket.gui.utility;

import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

public class FALoader {

    private static final GlyphFont GLYPH = GlyphFontRegistry.font("FontAwesome");

    private FALoader() {

    }

    public static Glyph create(final FontAwesome.Glyph glyph, final Color color) {
        if (color == null)
            return GLYPH.create(glyph);
        return GLYPH.create(glyph).color(color);
    }

    public static Glyph createDefault(final FontAwesome.Glyph glyph) {
        return create(glyph, Color.WHITE);
    }


}
