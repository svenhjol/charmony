package svenhjol.charmony.scaffold.base;

import net.fabricmc.loader.api.FabricLoader;
import svenhjol.charmony.scaffold.Charmony;
import svenhjol.charmony.scaffold.common.diagnostics.Diagnostics;

public final class Environment {
    public static final String DEBUG_MODE = "Debug mode";
    public static final String MIXIN_DISABLE_MODE = "Mixin disable mode";

    public static boolean isDevEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static boolean isModLoaded(String id) {
        FabricLoader instance = FabricLoader.getInstance();
        return instance.isModLoaded(id);
    }

    public static boolean isDebugMode() {
        return isDevEnvironment() || Charmony.instance().tryFeature(Diagnostics.class)
            .map(Diagnostics::debugMode).orElse(false);
    }

    public static boolean isMixinDisableMode() {
        return Charmony.instance().tryFeature(Diagnostics.class)
            .map(Diagnostics::mixinsDisabled).orElse(false);
    }
}
