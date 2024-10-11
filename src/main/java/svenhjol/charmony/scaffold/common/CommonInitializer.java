package svenhjol.charmony.scaffold.common;

import net.fabricmc.api.ModInitializer;
import svenhjol.charmony.scaffold.Charmony;
import svenhjol.charmony.scaffold.common.diagnostics.Diagnostics;
import svenhjol.charmony.scaffold.enums.Side;

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
