package svenhjol.charmony.core.client.mixins.hud_item_scaling;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charmony.core.client.BaseHudRenderer;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    @Shadow @Final private Matrix3x2fStack pose;

    /**
     * Adds programmatic scaling to rendered items.
     * This is used for example by the chiseled bookshelf hover effect to make the item icon pinch zoom in and out.
     */
    @Inject(
        method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V",
        at = @At("HEAD")
    )
    private void hookRenderItemHead(LivingEntity livingEntity, Level level, ItemStack stack, int i, int j, int k, CallbackInfo ci) {
        BaseHudRenderer.scaleItemsCallback(stack, this.pose);
    }
}
