package sypztep.crital.common.packetc2s;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.screen.GrinderScreenHandler;

public record GrindQualityPayloadC2S(boolean flag) implements CustomPayload {
    public static final Id<GrindQualityPayloadC2S> ID = CustomPayload.id(CritalMod.MODID +"quality_grinded");
    public static final PacketCodec<PacketByteBuf, GrindQualityPayloadC2S> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL,
            GrindQualityPayloadC2S::flag,
            GrindQualityPayloadC2S::new
    );
    public static void send(CustomPayload payload) {
        ClientPlayNetworking.send(payload);
    }
    public static void receiver(ServerPlayNetworking.Context context) {
        if (context.player().currentScreenHandler instanceof GrinderScreenHandler)
            ((GrinderScreenHandler) context.player().currentScreenHandler).quality_grinder();
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
