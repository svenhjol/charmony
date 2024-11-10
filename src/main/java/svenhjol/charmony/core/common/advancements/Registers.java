package svenhjol.charmony.core.common.advancements;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.impl.event.interaction.FakePlayerNetworkHandler;
import net.minecraft.advancements.CriteriaTriggers;
import svenhjol.charmony.core.base.Setup;

public class Registers extends Setup<Advancements> {
    public final ActionPerformed actionPerformed;

    public Registers(Advancements feature) {
        super(feature);
        actionPerformed = CriteriaTriggers.register("charmony_action_performed", new ActionPerformed());
    }

    @Override
    public Runnable boot() {
        return () -> {
            ServerPlayConnectionEvents.JOIN.register(feature().handlers::playerJoin);
        };
    }
}
