package svenhjol.charmony.core.client.core;

import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Client, canBeDisabled = false, description = """
    Core functionality and settings.""")
public final class Core extends SidedFeature {
    public final Registers registers;
    public final Handlers handlers;

    public Core(Mod mod) {
        super(mod);
        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    public static Core feature() {
        return Charmony.instance().feature(Core.class);
    }
}
