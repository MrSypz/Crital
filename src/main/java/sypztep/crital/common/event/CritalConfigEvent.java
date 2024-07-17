package sypztep.crital.common.event;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import sypztep.crital.common.data.CritalItemDataSerializer;
import sypztep.crital.client.payload.CritalConfigPayloadS2C;

public class CritalConfigEvent implements ServerPlayConnectionEvents.Join{
    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        CritalConfigPayloadS2C.send(handler.getPlayer(), CritalItemDataSerializer.encodeConfig());
    }
}
