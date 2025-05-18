package svenhjol.charmony.core.client.features.test_feature;

import svenhjol.charmony.api.core.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.api.core.Side;

@FeatureDefinition(side = Side.Client, canBeDisabledInConfig = false)
public final class TestFeature extends SidedFeature {
    public TestFeature(Mod mod) {
        super(mod);
    }

    public static TestFeature feature() {
        return Mod.getSidedFeature(TestFeature.class);
    }
}
