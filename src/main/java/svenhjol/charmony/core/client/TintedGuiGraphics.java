package svenhjol.charmony.core.client;

import net.minecraft.client.gui.GuiGraphics;
import svenhjol.charmony.core.helpers.ColorHelper;

@SuppressWarnings("unused")
public interface TintedGuiGraphics {
    GuiGraphics tint(ColorHelper.Color color);
}
