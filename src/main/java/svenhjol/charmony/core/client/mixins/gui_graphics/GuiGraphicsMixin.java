package svenhjol.charmony.core.client.mixins.gui_graphics;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
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

    @Redirect(
        method = "innerBlit",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            ordinal = 0
        )
    )
    private VertexConsumer hook0(VertexConsumer instance, int defaultColor) {
        return alterColor(instance, defaultColor);
    }

    @Redirect(
        method = "innerBlit",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            ordinal = 1
        )
    )
    private VertexConsumer hook1(VertexConsumer instance, int defaultColor) {
        return alterColor(instance, defaultColor);
    }

    @Redirect(
        method = "innerBlit",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            ordinal = 2
        )
    )
    private VertexConsumer hook2(VertexConsumer instance, int defaultColor) {
        return alterColor(instance, defaultColor);
    }

    @Redirect(
        method = "innerBlit",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            ordinal = 3
        )
    )
    private VertexConsumer hook3(VertexConsumer instance, int defaultColor) {
        var result = alterColor(instance, defaultColor);
        tint = null; // Last instruction - set tint to null
        return result;
    }

    @Unique
    private VertexConsumer alterColor(VertexConsumer instance, int defaultColor) {
        if (tint == null) return instance.setColor(defaultColor);
        return instance.setColor(tint.getRed(), tint.getGreen(), tint.getBlue(), 1.0f);
    }
}
