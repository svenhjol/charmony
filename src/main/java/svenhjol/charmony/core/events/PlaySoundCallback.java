package svenhjol.charmony.core.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

/**
 * Custom Fabric event that fires whenever a sound plays.
 * @see svenhjol.charmony.core.mixins.client.events.play_sound.SoundEngineMixin
 */
@SuppressWarnings("unused")
public interface PlaySoundCallback {
    Event<PlaySoundCallback> EVENT = EventFactory.createArrayBacked(PlaySoundCallback.class,
        listeners -> (soundEngine, soundInstance) -> {
            for (PlaySoundCallback listener : listeners) {
                listener.interact(soundEngine, soundInstance);
            }
        });

    void interact(SoundEngine soundEngine, SoundInstance soundInstance);
}
