package svenhjol.charmony.scaffold.nano.mixins;

import svenhjol.charmony.scaffold.nano.Charmony;

public class MixinConfig extends BaseMixinConfig {
    @Override
    protected String modId() {
        return Charmony.ID;
    }

    @Override
    protected String rootClassPath() {
        return "svenhjol.charmony.scaffold.nano";
    }
}
