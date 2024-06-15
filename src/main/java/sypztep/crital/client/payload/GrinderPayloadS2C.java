package sypztep.crital.client.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import sypztep.crital.client.gui.GrinderScreen;

public record GrinderPayloadS2C(boolean flag) implements CustomPayload {
    public static final Id<GrinderPayloadS2C> ID = CustomPayload.id("can_grind");
    public static final PacketCodec<PacketByteBuf, GrinderPayloadS2C> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL,
            GrinderPayloadS2C::flag,
            GrinderPayloadS2C::new
    );

    public static void send(ServerPlayerEntity player, boolean flag) {
        ServerPlayNetworking.send(player, new GrinderPayloadS2C(flag));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<GrinderPayloadS2C> {
        @Override
        public void receive(GrinderPayloadS2C payload, ClientPlayNetworking.Context context) {
            if (context.client().currentScreen instanceof GrinderScreen)
                ((GrinderScreen) context.client().currentScreen).grindButton.setDisabled(payload.flag());
        }
    }
}
