package svenhjol.charmony.core.common.mixins.player_killed_drop_callback;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charmony.api.events.PlayerKilledDropCallback;

@SuppressWarnings("UnreachableCode")
@Mixin(Player.class)
public abstract class PlayerMixin {
    @Shadow @Final
    Inventory inventory;

    /**
     * Fires the {@link svenhjol.charmony.api.events.PlayerKilledDropCallback} event.
     * Cancellable with ActionResult == SUCCESS.
     */
    @Inject(
        method = "dropEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Inventory;dropAll()V"
        ),
        cancellable = true
    )
    private void hookDropInventory(CallbackInfo ci) {
        InteractionResult result = PlayerKilledDropCallback.EVENT.invoker().interact((Player) (Object) this, this.inventory);
        if (result == InteractionResult.SUCCESS) {
            ci.cancel();
        }
    }
}
