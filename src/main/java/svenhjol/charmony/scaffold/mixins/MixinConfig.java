package svenhjol.charmony.scaffold.mixins;

import svenhjol.charmony.scaffold.Charmony;
import svenhjol.charmony.scaffold.base.Environment;

public class MixinConfig extends BaseMixinConfig {
    @Override
    protected String modId() {
        return Charmony.ID;
    }

    @Override
    protected String rootClassPath() {
        return "svenhjol.charmony.scaffold";
    }

    @Override
    protected boolean allowBaseName(String baseName, String mixinClassName) {
        return !Environment.isModLoaded("charmony");
    }
}
