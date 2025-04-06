package svenhjol.charmony.core.client.features.test_feature;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Client, canBeDisabledInConfig = false)
public class TestFeature extends SidedFeature {
    public TestFeature(Mod mod) {
        super(mod);
    }

    public static TestFeature feature() {
        return Mod.getSidedFeature(TestFeature.class);
    }
}
