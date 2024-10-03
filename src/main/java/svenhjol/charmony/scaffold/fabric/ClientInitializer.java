package svenhjol.charmony.scaffold.fabric;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charmony.scaffold.Charmony;
import svenhjol.charmony.scaffold.client.ClientEvents;
import svenhjol.charmony.scaffold.client.diagnostics.Diagnostics;
import svenhjol.charmony.scaffold.enums.Side;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEvents.init();

        var charmony = Charmony.instance();

        // Add core charmony features.
        charmony.addFeature(Diagnostics.class);

        charmony.run(Side.Client);
    }
}
