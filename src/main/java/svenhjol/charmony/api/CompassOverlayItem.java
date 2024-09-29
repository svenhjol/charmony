package svenhjol.charmony.api;

import net.minecraft.world.entity.player.Player;

public interface CompassOverlayItem {
    boolean shouldShow(Player player);

    String text();

    int color();
}
