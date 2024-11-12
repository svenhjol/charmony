package svenhjol.charmony.core.helper;

import net.minecraft.server.level.ServerPlayer;
import svenhjol.charmony.core.common.features.advancements.Advancements;

@SuppressWarnings("unused")
public final class AdvancementHelper {
    /**
     * Helper method to trigger a charmony advancement.
     */
    public static void trigger(String id, ServerPlayer player) {
        Advancements.trigger(id, player);
    }
}
