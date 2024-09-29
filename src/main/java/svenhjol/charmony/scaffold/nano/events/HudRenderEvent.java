package svenhjol.charmony.scaffold.nano.events;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;

@SuppressWarnings("unused")
public class HudRenderEvent extends CharmonyEvent<HudRenderEvent.Handler> {
    public static final HudRenderEvent INSTANCE = new HudRenderEvent();

    private HudRenderEvent() {}

    @FunctionalInterface
    public interface Handler {
        void run(GuiGraphics guiGraphics, DeltaTracker deltaTracker);
    }
}
