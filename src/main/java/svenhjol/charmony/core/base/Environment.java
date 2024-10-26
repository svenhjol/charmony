package svenhjol.charmony.core.base;

import net.fabricmc.loader.api.FabricLoader;
import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.common.core.Core;

public final class Environment {
    public static final String CLIENT_MODE = "Client mode";
    public static final String DEBUG_MODE = "Debug mode";
    public static final String MIXIN_DISABLE_MODE = "Mixin disable mode";

    public static boolean isDevEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static boolean isModLoaded(String id) {
        FabricLoader instance = FabricLoader.getInstance();
        return instance.isModLoaded(id);
    }

    public static boolean isClientMode() {
        return Charmony.instance().tryFeature(Core.class)
            .map(Core::clientMode).orElse(false);
    }

    public static boolean isDebugMode() {
        return isDevEnvironment() || Charmony.instance().tryFeature(Core.class)
            .map(Core::debugMode).orElse(false);
    }

    public static boolean isMixinDisableMode() {
        return Charmony.instance().tryFeature(Core.class)
            .map(Core::mixinsDisabled).orElse(false);
    }
}
