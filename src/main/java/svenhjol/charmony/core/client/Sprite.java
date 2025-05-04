package svenhjol.charmony.core.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import svenhjol.charmony.core.helpers.ColorHelper;

public interface Sprite {
    ResourceLocation sprite();

    int width();

    int height();

    default ColorHelper.Color tint() {
        return new ColorHelper.Color(DyeColor.WHITE);
    }
}
