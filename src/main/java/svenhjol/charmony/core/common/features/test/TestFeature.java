package svenhjol.charmony.core.common.features.test;

import svenhjol.charmony.core.annotations.Configurable;
import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

import java.util.ArrayList;
import java.util.List;

@FeatureDefinition(side = Side.Common)
public class TestFeature extends SidedFeature {
    @Configurable(
        name = "Common-side string list",
        description = "A common-side string list",
        requireRestart = false
    )
    private static List<String> strings = new ArrayList<>();

    public TestFeature(Mod mod) {
        super(mod);
    }
}
