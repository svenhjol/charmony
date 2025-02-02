package svenhjol.charmony.core.client.features.test;

import svenhjol.charmony.core.annotations.Configurable;
import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Client, canBeDisabledInConfig = false)
public class TestFeature extends SidedFeature {
    @Configurable(
        name = "Client-side value",
        description = "A client-side value",
        requireRestart = false
    )
    private static int clientValue = 123;

    public TestFeature(Mod mod) {
        super(mod);
    }
}
