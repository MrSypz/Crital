package sypztep.crital.client.packets2c;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import sypztep.crital.common.CritalMod;

public record SyncCritPayload(int entid, boolean flag) implements CustomPayload {
    public static final Id<SyncCritPayload> ID = new Id<>(CritalMod.id("sync_crit"));
    public static final PacketCodec<PacketByteBuf, SyncCritPayload> CODEC =
            PacketCodec.of((value, buf) -> buf.writeVarInt(value.entid),PacketCodec.of((value, buf) -> buf.writeBoolean(value.flag), buf -> new SyncCritPayload(buf.readVarInt(),buf.readBoolean())));

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}
