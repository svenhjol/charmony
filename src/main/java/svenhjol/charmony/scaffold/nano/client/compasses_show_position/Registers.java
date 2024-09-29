package svenhjol.charmony.scaffold.nano.client.compasses_show_position;

import svenhjol.charmony.scaffold.nano.Setup;
import svenhjol.charmony.scaffold.nano.events.HudRenderEvent;

public class Registers extends Setup<CompassesShowPosition> {
    public Registers(CompassesShowPosition feature) {
        super(feature);
    }

    @Override
    public Runnable register() {
        return () -> HudRenderEvent.INSTANCE.handle(feature().handlers::hudRender);
    }
}
