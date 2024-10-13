package svenhjol.charmony.core.client.control_panel;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Feature;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Client, canBeDisabled = false, description = """
    In-game control panel for Charmony mods.""")
public class ControlPanel extends Feature {
    public final Registers registers;
    public final Handlers handlers;

    public ControlPanel(Mod mod) {
        super(mod);
        registers = new Registers(this);
        handlers = new Handlers(this);
    }
}
