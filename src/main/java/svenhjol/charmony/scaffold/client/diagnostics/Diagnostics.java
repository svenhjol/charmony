package svenhjol.charmony.scaffold.client.diagnostics;

import svenhjol.charmony.scaffold.annotations.Configurable;
import svenhjol.charmony.scaffold.annotations.FeatureDefinition;
import svenhjol.charmony.scaffold.base.Mod;
import svenhjol.charmony.scaffold.base.Feature;
import svenhjol.charmony.scaffold.enums.Side;

@FeatureDefinition(side = Side.Client, canBeDisabled = false, description = """
    Diagnostic tools for Charmony client.""")
public class Diagnostics extends Feature {
    @Configurable(
        name = "Test",
        description = "Trying to avoid config being overwritten."
    )
    private static boolean test = false;

    public Diagnostics(Mod mod) {
        super(mod);
    }
}
