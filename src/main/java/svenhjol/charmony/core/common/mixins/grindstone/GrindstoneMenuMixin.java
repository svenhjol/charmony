package svenhjol.charmony.core.common.mixins.grindstone;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.GrindstoneMenu;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charmony.core.events.GrindstoneEvents;

import javax.annotation.Nullable;

@SuppressWarnings({"ConstantConditions", "UnreachableCode"})
@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuMixin {
    @Unique
    private @Nullable Player player;

    @Mutable
    @Shadow @Final Container repairSlots;

    @Mutable
    @Shadow @Final private Container resultSlots;

    @Inject(
        method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",
        at = @At("TAIL")
    )
    private void hookInit(int syncId, Inventory inventory, ContainerLevelAccess access, CallbackInfo ci) {
        if (inventory.player != null) {
            GrindstoneEvents.create((GrindstoneMenu)(Object)this, inventory.player, inventory, repairSlots, resultSlots, access);
            player = inventory.player;
        }
    }

    @Inject(
        method = "createResult",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookCreateResult(CallbackInfo ci) {
        var instance = GrindstoneEvents.instance(player);
        if (instance == null) return;

        if (GrindstoneEvents.CALCULATE_OUTPUT.invoke(instance)) {
            ci.cancel();
        }
    }

    @Inject(
        method = "removed",
        at = @At("TAIL")
    )
    private void hookRemoved(Player player, CallbackInfo ci) {
        GrindstoneEvents.remove(player);
    }
}
