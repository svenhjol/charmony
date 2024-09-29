package svenhjol.charmony.scaffold.nano.client.repair_cost_visible;

import svenhjol.charmony.scaffold.nano.Mod;
import svenhjol.charmony.scaffold.nano.ModFeature;
import svenhjol.charmony.scaffold.nano.annotations.Feature;
import svenhjol.charmony.scaffold.nano.enums.Side;

@Feature(side = Side.Client)
public class RepairCostVisible extends ModFeature {
    public Handlers handlers;

    public RepairCostVisible(Mod mod) {
        super(mod);
        handlers = new Handlers(this);
    }
}
