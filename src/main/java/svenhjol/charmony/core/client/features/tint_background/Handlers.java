package svenhjol.charmony.core.client.features.tint_background;

import svenhjol.charmony.api.core.Color;
import svenhjol.charmony.core.base.Setup;

public class Handlers extends Setup<TintBackground> {
    public Color tint = null;

    public Handlers(TintBackground feature) {
        super(feature);
    }
}
