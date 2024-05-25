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

public record QualityGrinderPayloadS2C(boolean flag) implements CustomPayload {
    public static final Id<QualityGrinderPayloadS2C> ID = CustomPayload.id(CritalMod.MODID +"can_grind_quality");
    public static final PacketCodec<PacketByteBuf, QualityGrinderPayloadS2C> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL,
            QualityGrinderPayloadS2C::flag,
            QualityGrinderPayloadS2C::new
    );
    public static void send(ServerPlayerEntity player, CustomPayload payload) {
        ServerPlayNetworking.send(player,payload);
    }
    public static void receiver(QualityGrinderPayloadS2C grinderPayload, NetworkClientUtil handle) {
        handle.context(() -> {
            if (handle.client().currentScreen instanceof GrinderScreen)
                ((GrinderScreen) handle.client().currentScreen).qualityButton.setDisabled(grinderPayload.flag());
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
