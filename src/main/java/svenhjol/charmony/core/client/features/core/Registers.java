package svenhjol.charmony.core.client.features.core;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.features.core.Networking.S2CUsingCharmonyServer;
import svenhjol.charmony.core.events.ClientLoginPlayerCallback;

public class Registers extends Setup<Core> {
    public Registers(Core feature) {
        super(feature);
    }

    @Override
    public Runnable boot() {
        return () -> {
            ClientTickEvents.END_CLIENT_TICK.register(feature().handlers::clientTick);
            ClientLoginPlayerCallback.EVENT.register(feature().handlers::clientLogin);
            ClientPlayNetworking.registerGlobalReceiver(S2CUsingCharmonyServer.TYPE, feature().handlers::usingCharmonyServer);
        };
    }
}
