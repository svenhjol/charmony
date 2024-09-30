package svenhjol.charmony.scaffold.nano.fabric;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charmony.scaffold.nano.client.ClientEvents;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEvents.init();
    }
}
