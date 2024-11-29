package svenhjol.charmony.core.client.mixins.player_tick_callback;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charmony.core.events.PlayerTickCallback;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    /**
     * Fires the {@link PlayerTickCallback} event on the client side.
     */
    @Inject(
        method = "tick",
        at = @At("TAIL")
    )
    private void hookTick(CallbackInfo ci) {
        PlayerTickCallback.EVENT.invoker().interact((Player)(Object)this);
    }
}