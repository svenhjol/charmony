package svenhjol.charmony.core.client.mixins.hud_item_scaling;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.state.GuiItemRenderState;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charmony.core.client.features.hud_item_scaling.HudItemScaling;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    @Shadow @Final private Matrix3x2fStack pose;

    /**
     * Capture the current itemStackRenderState so we can compare it later in the rendering pipeline.
     */
    @WrapOperation(
        method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/state/GuiRenderState;submitItem(Lnet/minecraft/client/gui/render/state/GuiItemRenderState;)V")
    )
    private void hookRenderItemHead(GuiRenderState instance, GuiItemRenderState guiItemRenderState, Operation<Void> original,
                                    @Local ItemStackRenderState itemStackRenderState, @Local(argsOnly = true) ItemStack itemStack) {
        HudItemScaling.feature().handlers.setTarget(itemStackRenderState, itemStack);
        original.call(instance, guiItemRenderState);
    }
}
