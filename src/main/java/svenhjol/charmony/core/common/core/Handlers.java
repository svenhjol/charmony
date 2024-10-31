package svenhjol.charmony.core.common.core;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.core.Networking.S2CUsingCharmonyServer;

public class Handlers extends Setup<Core> {
    public Handlers(Core feature) {
        super(feature);
    }

    public void playerJoin(ServerGamePacketListenerImpl packetListener, PacketSender packetSender, MinecraftServer server) {
        var player = packetListener.getPlayer();
        S2CUsingCharmonyServer.send(player);
    }
}