package svenhjol.charmony.core.client;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.client.features.control_panel.ControlPanel;
import svenhjol.charmony.core.client.features.core.Core;
import svenhjol.charmony.core.client.features.test.TestFeature;
import svenhjol.charmony.core.enums.Side;

public final class ClientInitializer implements ClientModInitializer {
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
        charmony.addFeature(Core.class);
        charmony.addFeature(ControlPanel.class);
        charmony.addFeature(TestFeature.class);
        charmony.run(Side.Client);

        initialized = true;
    }
}
