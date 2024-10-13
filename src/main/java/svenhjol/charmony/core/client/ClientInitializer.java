package svenhjol.charmony.core.client;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.client.control_panel.ControlPanel;
import svenhjol.charmony.core.client.diagnostics.Diagnostics;
import svenhjol.charmony.core.enums.Side;

public class ClientInitializer implements ClientModInitializer {
    private static boolean initialized = false;

    @Override
    public void onInitializeClient() {
        init();
    }

    /**
     * We expose init() so that child mods can ensure that Charmony gets launched first.
     */
    public static void init() {
        if (initialized) return;

        // Setup and run the mod.
        var charmony = Charmony.instance();
        charmony.addFeature(ControlPanel.class);
        charmony.addFeature(Diagnostics.class);
        charmony.run(Side.Client);

        initialized = true;
    }
}
