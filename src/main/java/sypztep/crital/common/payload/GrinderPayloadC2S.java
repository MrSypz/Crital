package sypztep.crital.common.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import sypztep.crital.common.screen.GrinderScreenHandler;

public record GrinderPayloadC2S() implements CustomPayload {
    public static final Id<GrinderPayloadC2S> ID = CustomPayload.id("grinded");
    public static final PacketCodec<PacketByteBuf, GrinderPayloadC2S> CODEC = PacketCodec.unit(new GrinderPayloadC2S());
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void send() {
        ClientPlayNetworking.send(new GrinderPayloadC2S());
    }
    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<GrinderPayloadC2S> {
        @Override
        public void receive(GrinderPayloadC2S payload, ServerPlayNetworking.Context context) {
            if (context.player().currentScreenHandler instanceof GrinderScreenHandler)
                ((GrinderScreenHandler) context.player().currentScreenHandler).grinder();
        }
    }

}
