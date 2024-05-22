package sypztep.crital.client.packets2c;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import sypztep.crital.client.gui.GrinderScreen;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.api.network.NetworkClientUtil;

public record GrinderPayloadS2C(boolean flag) implements CustomPayload {
    public static final Id<GrinderPayloadS2C> ID = CustomPayload.id(CritalMod.MODID +"can_grind");
    public static final PacketCodec<PacketByteBuf, GrinderPayloadS2C> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL,
            GrinderPayloadS2C::flag,
            GrinderPayloadS2C::new
    );
    public static void send(ServerPlayerEntity player, CustomPayload payload) {
        ServerPlayNetworking.send(player,payload);
    }
    public static void receiver(GrinderPayloadS2C grinderPayload, NetworkClientUtil handle) {
        handle.context(() -> {
            if (handle.client().currentScreen instanceof GrinderScreen)
                ((GrinderScreen) handle.client().currentScreen).grindButton.setDisabled(grinderPayload.flag());
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
