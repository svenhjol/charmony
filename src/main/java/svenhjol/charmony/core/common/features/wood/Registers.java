package svenhjol.charmony.core.common.features.wood;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import svenhjol.charmony.core.base.Setup;

public class Registers extends Setup<Wood> {
    public Registers(Wood feature) {
        super(feature);
    }

    @Override
    public Runnable boot() {
        return () -> {
            ServerLifecycleEvents.SERVER_STARTING.register(feature().handlers::serverStarting);
        };
    }
}
