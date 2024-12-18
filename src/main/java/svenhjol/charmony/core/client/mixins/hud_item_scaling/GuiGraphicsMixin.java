package svenhjol.charmony.core.client.mixins.hud_item_scaling;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charmony.core.client.BaseHudRenderer;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    @Shadow @Final private PoseStack pose;

    /**
     * Adds programmatic scaling to rendered items.
     * This is used for example by the chiseled bookshelf hover effect to make the item icon pinch zoom in and out.
     */
    @Inject(
        method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V",
            shift = At.Shift.AFTER
        )
    )
    private void hookAddPoseScale(LivingEntity livingEntity, Level level, ItemStack stack, int i, int j, int k, int l, CallbackInfo ci) {
        BaseHudRenderer.scaleItemsCallback(stack, this.pose);
    }
}
