package svenhjol.charmony.scaffold.fabric;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charmony.scaffold.client.ClientEvents;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEvents.init();
    }
}
