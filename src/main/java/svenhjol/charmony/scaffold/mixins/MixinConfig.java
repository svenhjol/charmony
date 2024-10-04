package svenhjol.charmony.scaffold.mixins;

import svenhjol.charmony.scaffold.Charmony;

public class MixinConfig extends BaseMixinConfig {
    @Override
    protected String modId() {
        return Charmony.ID;
    }

    @Override
    protected String rootClassPath() {
        return "svenhjol.charmony.scaffold";
    }
}
