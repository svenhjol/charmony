package svenhjol.charmony.core.client.mixins.hud_item_scaling;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charmony.core.client.features.hud_item_scaling.HudItemScaling;

@Mixin(ItemStackRenderState.class)
public class ItemStackRenderStateMixin {
    /**
     * Capture the currently rendering itemStackRenderState.
     */
    @Inject(
        method = "render",
        at = @At("HEAD")
    )
    private void hookRender(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, CallbackInfo ci) {
        HudItemScaling.feature().handlers.setStateHolder((ItemStackRenderState)(Object)this);
    }
}
