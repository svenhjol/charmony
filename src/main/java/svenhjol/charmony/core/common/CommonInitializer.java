package svenhjol.charmony.core.common;

import net.fabricmc.api.ModInitializer;
import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.common.diagnostics.Diagnostics;
import svenhjol.charmony.core.enums.Side;

public class CommonInitializer implements ModInitializer {
    private static boolean initialized = false;

    @Override
    public void onInitialize() {
        init();
    }

    public static void init() {
        if (initialized) return;

        var charmony = Charmony.instance();
        charmony.addFeature(Diagnostics.class);
        charmony.run(Side.Common);

        initialized = true;
    }
}
