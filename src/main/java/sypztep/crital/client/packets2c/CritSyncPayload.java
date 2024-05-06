package sypztep.crital.client.packets2c;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

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
