package svenhjol.charmony.scaffold.nano.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import svenhjol.charmony.scaffold.nano.Charmony;
import svenhjol.charmony.scaffold.nano.Log;
import svenhjol.charmony.scaffold.nano.events.HudRenderEvent;

public class ClientEvents {
    private static final Log LOGGER = new Log(Charmony.ID, "ClientEvents");
    private static boolean initialized = false;

    private ClientEvents() {}

    public static void init() {
        if (initialized) {
            throw new RuntimeException("Trying to initialize client events more than once");
        }

        HudRenderCallback.EVENT.register(ClientEvents::hudRender);

        LOGGER.info("Finished initialising client events.");
        initialized = true;
    }

    private static void hudRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        HudRenderEvent.INSTANCE.getHandlers().forEach(handler -> handler.run(guiGraphics, deltaTracker));
    }
}
