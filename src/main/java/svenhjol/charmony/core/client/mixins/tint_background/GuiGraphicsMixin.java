package svenhjol.charmony.core.client.mixins.tint_background;

import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import svenhjol.charmony.api.core.Color;
import svenhjol.charmony.api.tint_background.TintedGuiGraphics;
import svenhjol.charmony.core.client.features.tint_background.TintBackground;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin implements TintedGuiGraphics {
    @Override
    public GuiGraphics tint(Color color) {
        TintBackground.feature().handlers.tint = color;
        return (GuiGraphics)(Object)this;
    }
}

