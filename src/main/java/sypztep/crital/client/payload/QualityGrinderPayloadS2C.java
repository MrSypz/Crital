package sypztep.crital.client.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import sypztep.crital.client.gui.GrinderScreen;
import sypztep.crital.common.CritalMod;

public record QualityGrinderPayloadS2C(boolean flag) implements CustomPayload {
    public static final Id<QualityGrinderPayloadS2C> ID = CustomPayload.id("can_grind_quality");
    public static final PacketCodec<PacketByteBuf, QualityGrinderPayloadS2C> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL,
            QualityGrinderPayloadS2C::flag,
            QualityGrinderPayloadS2C::new
    );

    public static void send(ServerPlayerEntity player, boolean flag) {
        ServerPlayNetworking.send(player, new QualityGrinderPayloadS2C(flag));
    }
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<QualityGrinderPayloadS2C> {
        @Override
        public void receive(QualityGrinderPayloadS2C payload, ClientPlayNetworking.Context context) {
            if (context.client().currentScreen instanceof GrinderScreen)
                ((GrinderScreen) context.client().currentScreen).qualityButton.setDisabled(payload.flag());
        }
    }
}
