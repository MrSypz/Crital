package sypztep.crital.common.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.screen.GrinderScreenHandler;

public record GrindQualityPayloadC2S() implements CustomPayload {
    public static final Id<GrindQualityPayloadC2S> ID = CustomPayload.id(CritalMod.MODID + "quality_grinded");
    public static final PacketCodec<PacketByteBuf, GrindQualityPayloadC2S> CODEC = PacketCodec.unit(new GrindQualityPayloadC2S());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void send() {
        ClientPlayNetworking.send(new GrindQualityPayloadC2S());
    }
    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<GrindQualityPayloadC2S> {
        @Override
        public void receive(GrindQualityPayloadC2S payload, ServerPlayNetworking.Context context) {
            if (context.player().currentScreenHandler instanceof GrinderScreenHandler)
                ((GrinderScreenHandler) context.player().currentScreenHandler).quality_grinder();
        }
    }
}
