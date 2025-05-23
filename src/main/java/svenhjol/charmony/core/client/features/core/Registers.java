package svenhjol.charmony.core.client.features.core;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.client.ClientRegistry;
import svenhjol.charmony.core.common.features.core.Networking.S2CUsingCharmonyServer;
import svenhjol.charmony.api.events.ClientLoginPlayerCallback;

public class Registers extends Setup<Core> {
    public Registers(Core feature) {
        super(feature);
    }

    public void registerDeferredParticles(Minecraft minecraft) {
        for (var particle : ClientRegistry.PARTICLES) {
            var type = particle.type();
            var registration = particle.registration();
            minecraft.particleEngine.register(type, registration);
        }
    }

    @Override
    public Runnable boot() {
        return () -> {
            ClientTickEvents.END_CLIENT_TICK.register(feature().handlers::clientTick);
            ClientLoginPlayerCallback.EVENT.register(feature().handlers::clientLogin);
            ClientLifecycleEvents.CLIENT_STARTED.register(feature().handlers::clientStart);
            ClientPlayNetworking.registerGlobalReceiver(S2CUsingCharmonyServer.TYPE, feature().handlers::usingCharmonyServer);
        };
    }
}
