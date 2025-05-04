package svenhjol.charmony.core.client.mixins.gui_graphics;

import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import svenhjol.charmony.core.client.TintedGuiGraphics;
import svenhjol.charmony.core.helpers.ColorHelper;

import javax.annotation.Nullable;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin implements TintedGuiGraphics {
    @Unique @Nullable private ColorHelper.Color tint = null;

    @Override
    public GuiGraphics tint(ColorHelper.Color color) {
        this.tint = color;
        return (GuiGraphics)(Object)this;
    }

    @ModifyArg(
        method = "innerBlit",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            ordinal = 0
        )
    )
    private int hook0(int defaultColor) {
        return alterColor(defaultColor);
    }

    @ModifyArg(
        method = "innerBlit",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            ordinal = 1
        )
    )
    private int hook1(int defaultColor) {
        return alterColor(defaultColor);
    }

    @ModifyArg(
        method = "innerBlit",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            ordinal = 2
        )
    )
    private int hook2(int defaultColor) {
        return alterColor(defaultColor);
    }

    @ModifyArg(
        method = "innerBlit",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            ordinal = 3
        )
    )
    private int hook3(int defaultColor) {
        var result = alterColor(defaultColor);
        tint = null; // Last instruction - set tint to null
        return result;
    }

    @Unique
    private int alterColor(int defaultColor) {
        if (tint == null) return defaultColor;
        return tint.getColor();
    }
}
