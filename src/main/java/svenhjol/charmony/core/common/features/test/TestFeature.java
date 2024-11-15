package svenhjol.charmony.core.common.features.test;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Common, showInConfig = true)
public class TestFeature extends SidedFeature {
    public TestFeature(Mod mod) {
        super(mod);
    }
}
