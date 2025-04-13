package svenhjol.charmony.core.common.features.teleport;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Common, canBeDisabled = false, description = """
    Handles extended teleportation functions.""")
public final class Teleport extends SidedFeature {
    public final Handlers handlers;
    public final Registers registers;

    public Teleport(Mod mod) {
        super(mod);
        handlers = new Handlers(this);
        registers = new Registers(this);
    }

    public static Teleport feature() {
        return Mod.getSidedFeature(Teleport.class);
    }
}
