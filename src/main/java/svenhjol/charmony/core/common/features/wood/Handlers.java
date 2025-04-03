package svenhjol.charmony.core.common.features.wood;

import net.minecraft.server.MinecraftServer;
import svenhjol.charmony.core.base.Setup;

@SuppressWarnings({"deprecation", "unused"})
public class Handlers extends Setup<Wood> {
    public Handlers(Wood feature) {
        super(feature);
    }

    public void serverStarting(MinecraftServer server) {
        // Set each sign's block and item.
        WoodRegistry.SIGN_ITEMS.forEach(supplier -> {
            var sign = supplier.get();
            sign.wallBlock = sign.getWallSignBlock().get();
            sign.wallBlock.item = sign;
            sign.block = sign.getSignBlock().get();
            sign.block.item = sign;
        });

        // Set each hanging sign's block and item.
        WoodRegistry.HANGING_SIGN_ITEMS.forEach(supplier -> {
            var sign = supplier.get();
            sign.wallBlock = sign.getWallSignBlock().get();
            sign.wallBlock.item = sign;
            sign.block = sign.getHangingBlock().get();
            sign.block.item = sign;
        });
    }
}
