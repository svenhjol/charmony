package svenhjol.charmony.core.common.mixins.anvil_update;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charmony.core.common.features.core.Core;
import svenhjol.charmony.core.events.AnvilUpdateEvent;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    @Shadow @Final private DataSlot cost;

    @Shadow private int repairItemCountCost;

    public AnvilMenuMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess access,
                          ItemCombinerMenuSlotDefinition definition) {
        super(menuType, i, inventory, access, definition);
    }

    @Inject(
        method = "createResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z",
            ordinal = 1,
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private void hookCreateResult(CallbackInfo ci,
                                  @Local(ordinal = 0) ItemStack input,
                                  @Local long baseCost,
                                  @Local(ordinal = 2) ItemStack material) {
        var result = AnvilUpdateEvent.INSTANCE.invoke(player, input, material, baseCost);
        if (result.isPresent()) {
            var recipe = result.get();
            resultSlots.setItem(0, recipe.output);
            cost.set(recipe.experienceCost);
            this.repairItemCountCost = recipe.materialCost;
            ci.cancel();
        }
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
