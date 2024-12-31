package svenhjol.charmony.core.integration;

import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.base.Mod;

@SuppressWarnings("unused")
public final class ModMenuPlugin extends BaseModMenuPlugin {
    @Override
    public Mod mod() {
        return Charmony.instance();
    }
}
