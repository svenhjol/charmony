package svenhjol.charmony.core.common.features.wood;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Common, canBeDisabled = false, description = """
    Allows registration of custom wood types.""")
public final class Wood extends SidedFeature {
    public final Registers registers;
    public final Handlers handlers;

    public Wood(Mod mod) {
        super(mod);
        registers = new Registers(this);
        handlers = new Handlers(this);
    }
}
