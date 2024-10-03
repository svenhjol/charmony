package svenhjol.charmony.scaffold.mixins.events.play_sound_callback;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charmony.scaffold.events.PlaySoundCallback;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {
    @Inject(
        method = "play",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/sounds/SoundBufferLibrary;getCompleteBuffer(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/concurrent/CompletableFuture;"
        )
    )
    private void hookPlayStatic(SoundInstance soundInstance, CallbackInfo ci) {
        PlaySoundCallback.EVENT.invoker().interact((SoundEngine)(Object)this, soundInstance);
    }

    @Inject(
        method = "play",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/sounds/SoundBufferLibrary;getStream(Lnet/minecraft/resources/ResourceLocation;Z)Ljava/util/concurrent/CompletableFuture;"
        )
    )
    private void hookPlayStreamed(SoundInstance soundInstance, CallbackInfo ci) {
        PlaySoundCallback.EVENT.invoker().interact((SoundEngine)(Object)this, soundInstance);
    }
}
