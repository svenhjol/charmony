package svenhjol.charmony.core.common.mixins.anvil;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charmony.core.common.features.core.Core;
import svenhjol.charmony.api.events.AnvilEvents;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuOnTakeMixin extends ItemCombinerMenu {
    public AnvilMenuOnTakeMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess access,
                                ItemCombinerMenuSlotDefinition definition) {
        super(menuType, i, inventory, access, definition);
    }

    @Inject(
        method = "onTake",
        at = @At("HEAD")
    )
    private void hookOnTake(Player player, ItemStack taken, CallbackInfo ci) {
        AnvilEvents.ON_TAKE.invoke(player, inputSlots.getItem(0).copy(), inputSlots.getItem(1).copy(), taken);
    }

    @Redirect(
        method = "onTake",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V"
        )
    )
    private void hookOnTakeSetItem(Container inputSlots, int slot, ItemStack stack) {
        if (Core.feature().customAnvilOnTakeBehavior() && stack == ItemStack.EMPTY) {
            var original = inputSlots.getItem(slot);
            original.shrink(1);
            inputSlots.setItem(slot, original);
        }
    }
}
