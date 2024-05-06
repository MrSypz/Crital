package sypztep.crital.client.packets2c;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public record CritSyncPayload(int entityId, boolean flag) implements CustomPayload {
    public static final Id<CritSyncPayload> ID = CustomPayload.id("crital:sync_crit");
    public static final PacketCodec<PacketByteBuf, CritSyncPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            CritSyncPayload::entityId,
            PacketCodecs.BOOL,
            CritSyncPayload::flag,
            CritSyncPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
