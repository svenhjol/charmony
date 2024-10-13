package svenhjol.charmony.core.mixins;

import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.base.Environment;

public class MixinConfig extends BaseMixinConfig {
    @Override
    protected String modId() {
        return Charmony.ID;
    }

    @Override
    protected String rootClassPath() {
        return "svenhjol.charmony.core";
    }

    @Override
    protected boolean allowBaseName(String baseName, String mixinClassName) {
        return !Environment.isModLoaded("charmony");
    }
}
