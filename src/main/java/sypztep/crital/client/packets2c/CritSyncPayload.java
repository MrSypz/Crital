package sypztep.crital.client.packets2c;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.api.crital.NewCriticalOverhaul;
import sypztep.crital.common.api.network.NetworkClientUtil;

import java.util.Objects;

public record CritSyncPayload(int entityId, boolean flag) implements CustomPayload {
    public static final Id<CritSyncPayload> ID = CustomPayload.id(CritalMod.MODID +"sync_crit");
    public static final PacketCodec<PacketByteBuf, CritSyncPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            CritSyncPayload::entityId,
            PacketCodecs.BOOL,
            CritSyncPayload::flag,
            CritSyncPayload::new
    );
    public static void send(ServerPlayerEntity player, CustomPayload payload) {
        ServerPlayNetworking.send(player,payload);
    }
    public static void receiver(CritSyncPayload syncPayload, NetworkClientUtil util) {
        util.context(() -> {
            if (util.client().world != null && Objects.requireNonNull(util.client().world).getEntityById(syncPayload.entityId()) instanceof NewCriticalOverhaul invoker) {
                invoker.setCritical(syncPayload.flag());
            }
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
