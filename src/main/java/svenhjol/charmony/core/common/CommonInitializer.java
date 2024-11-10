package svenhjol.charmony.core.common;

import net.fabricmc.api.ModInitializer;
import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.common.advancements.Advancements;
import svenhjol.charmony.core.common.core.Core;
import svenhjol.charmony.core.enums.Side;

public class CommonInitializer implements ModInitializer {
    private static boolean initialized = false;

    @Override
    public void onInitialize() {
        init();
    }

    /**
     * We expose init() so that child mods can ensure that Charmony gets launched first.
     */
    public static void init() {
        if (initialized) return;

        var charmony = Charmony.instance();
        charmony.addFeature(Core.class);
        charmony.addFeature(Advancements.class);
        charmony.run(Side.Common);

        initialized = true;
    }
}
