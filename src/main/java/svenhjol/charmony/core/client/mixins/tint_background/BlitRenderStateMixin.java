package svenhjol.charmony.core.client.mixins.tint_background;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.render.state.BlitRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charmony.api.core.Color;
import svenhjol.charmony.core.client.features.tint_background.TintBackground;

import javax.annotation.Nullable;

@Mixin(BlitRenderState.class)
public class BlitRenderStateMixin {
    @WrapOperation(
        method = "buildVertices",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            ordinal = 0
        )
    )
    private VertexConsumer hook0(VertexConsumer instance, int color, Operation<VertexConsumer> original) {
        if (TintBackground.feature().enabled()) {
            return alterColor(instance, color);
        }
        return original.call(instance, color);
    }

    @WrapOperation(
        method = "buildVertices",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            ordinal = 1
        )
    )
    private VertexConsumer hook1(VertexConsumer instance, int color, Operation<VertexConsumer> original) {
        if (TintBackground.feature().enabled()) {
            return alterColor(instance, color);
        }
        return original.call(instance, color);
    }

    @WrapOperation(
        method = "buildVertices",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            ordinal = 2
        )
    )
    private VertexConsumer hook2(VertexConsumer instance, int color, Operation<VertexConsumer> original) {
        if (TintBackground.feature().enabled()) {
            return alterColor(instance, color);
        }
        return original.call(instance, color);
    }

    @WrapOperation(
        method = "buildVertices",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            ordinal = 3
        )
    )
    private VertexConsumer hook3(VertexConsumer instance, int color, Operation<VertexConsumer> original) {
        if (TintBackground.feature().enabled()) {
            var result = alterColor(instance, color);
            unsetTint(); // Last instruction - set tint to null
            return result;
        }
        return original.call(instance, color);
    }

    @Unique
    private @Nullable Color getTint() {
        return TintBackground.feature().handlers.tint;
    }

    @Unique
    private void unsetTint() {
        TintBackground.feature().handlers.tint = null;
    }

    @Unique
    private VertexConsumer alterColor(VertexConsumer instance, int defaultColor) {
        var tint = getTint();
        if (tint == null) return instance.setColor(defaultColor);
        return instance.setColor(tint.getRed(), tint.getGreen(), tint.getBlue(), 1.0f);
    }
}
