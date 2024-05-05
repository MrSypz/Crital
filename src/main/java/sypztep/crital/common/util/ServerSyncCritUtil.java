package sypztep.crital.common.util;

import net.minecraft.network.PacketByteBuf;

public class ServerSyncCritUtil {
    private final int entityId;
    private final boolean setCrit;

    public ServerSyncCritUtil(int entityId, boolean flag) {
        this.entityId = entityId;
        this.setCrit = flag;
    }

    public ServerSyncCritUtil(PacketByteBuf byteBuf) {
        this.entityId = byteBuf.readVarInt();
        this.setCrit = byteBuf.readBoolean();
    }

    public PacketByteBuf write(PacketByteBuf byteBuf) {
        byteBuf.writeVarInt(this.entityId);
        byteBuf.writeBoolean(this.setCrit);
        return byteBuf;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public boolean setBoolean() {
        return this.setCrit;
    }
}
