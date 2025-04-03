package svenhjol.charmony.core.common;

import net.fabricmc.api.ModInitializer;
import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.base.Environment;
import svenhjol.charmony.core.common.features.advancements.Advancements;
import svenhjol.charmony.core.common.features.core.Core;
import svenhjol.charmony.core.common.features.test.TestFeature;
import svenhjol.charmony.core.common.features.wood.Wood;
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
        charmony.addSidedFeature(Core.class);
        charmony.addSidedFeature(Advancements.class);
        charmony.addSidedFeature(Wood.class);

        if (Environment.isDevEnvironment()) {
            charmony.addSidedFeature(TestFeature.class);
        }

        charmony.run(Side.Common);

        initialized = true;
    }
}
