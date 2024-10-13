package svenhjol.charmony.core.client.diagnostics;

import svenhjol.charmony.core.annotations.Configurable;
import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Client, canBeDisabled = false, description = """
    Diagnostic tools for Charmony client.""")
public class Diagnostics extends SidedFeature {
    @Configurable(
        name = "Test",
        description = "Trying to avoid config being overwritten."
    )
    private static boolean test = false;

    public Diagnostics(Mod mod) {
        super(mod);
    }
}
