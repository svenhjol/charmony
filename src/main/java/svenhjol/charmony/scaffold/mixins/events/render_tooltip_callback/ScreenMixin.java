package svenhjol.charmony.scaffold.mixins.events.render_tooltip_callback;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charmony.scaffold.helper.TooltipHelper;

import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin {
    /**
     * Caches the ItemStack passed to getTooltipFromItem.
     * This is then available for the RenderTooltipEvent event.
     */
    @Inject(
        method = "getTooltipFromItem",
        at = @At("HEAD")
    )
    private static void hookGetTooltipFromItem(Minecraft minecraft, ItemStack stack, CallbackInfoReturnable<List<Component>> cir) {
        TooltipHelper.setTooltipItemStack(stack);
    }
}
