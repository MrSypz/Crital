package sypztep.crital.client.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import sypztep.crital.common.api.crital.NewCriticalOverhaul;

import java.util.Objects;

public record CritSyncPayload(int entityId, boolean flag) implements CustomPayload {
    public static final Id<CritSyncPayload> ID = CustomPayload.id("sync_crit");
    public static final PacketCodec<PacketByteBuf, CritSyncPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            CritSyncPayload::entityId,
            PacketCodecs.BOOL,
            CritSyncPayload::flag,
            CritSyncPayload::new
    );

    public static void send(ServerPlayerEntity player, int entityId, boolean flag) {
        ServerPlayNetworking.send(player, new CritSyncPayload(entityId, flag));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<CritSyncPayload> {
        @Override
        public void receive(CritSyncPayload payload, ClientPlayNetworking.Context context) {
            if (context.client().world != null && Objects.requireNonNull(context.client().world).getEntityById(payload.entityId()) instanceof NewCriticalOverhaul invoker) {
                invoker.crital$setCritical(payload.flag());
            }
        }
    }
}
