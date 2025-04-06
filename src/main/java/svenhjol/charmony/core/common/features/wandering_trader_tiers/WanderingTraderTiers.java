package svenhjol.charmony.core.common.features.wandering_trader_tiers;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Common, description = """
    Enables a Charmony mod to register a whole tier of trades with a Wandering Trader.
    The trader selects a tier at random when it is spawned.
    If this feature is disabled, mods that register tiers will be ignored.""")
public final class WanderingTraderTiers extends SidedFeature {
    public final Handlers handlers;

    public WanderingTraderTiers(Mod mod) {
        super(mod);
        handlers = new Handlers(this);
    }

    public static WanderingTraderTiers feature() {
        return Mod.getSidedFeature(WanderingTraderTiers.class);
    }
}
