package sypztep.crital.client.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import sypztep.crital.common.data.CritalItemDataSerializer;

public record CritalConfigPayloadS2C(int encode) implements CustomPayload {
    public static final Id<CritalConfigPayloadS2C> ID = CustomPayload.id("crital_config_matcher");
    public static final PacketCodec<PacketByteBuf, CritalConfigPayloadS2C> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, CritalConfigPayloadS2C::encode, CritalConfigPayloadS2C::new);

    private static final Text MISMATCH_TEXT =
            Text.literal("Your Crital Config Data seem to mismatch to server ")
            .append(Text.literal("Don't worry it seem server modify config data.\n").formatted(Formatting.WHITE))
            .append(Text.literal("Please Contact to host to get a config file from server.\n").formatted(Formatting.RED))
            .append(Text.literal("Your configuration file is located at \n").formatted(Formatting.WHITE))
            .append(Text.literal("\n\n"))
            .append(Text.literal(CritalItemDataSerializer.getConfigFilePath().toString()).formatted(Formatting.RED));

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void send(ServerPlayerEntity player, int encoding) {
        ServerPlayNetworking.send(player, new CritalConfigPayloadS2C(encoding));
    }

    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<CritalConfigPayloadS2C> {
        @Override
        public void receive(CritalConfigPayloadS2C payload, ClientPlayNetworking.Context context) {
            if (CritalItemDataSerializer.encodeConfig() != payload.encode()) {
                context.player().networkHandler.getConnection().disconnect(MISMATCH_TEXT);
            }
        }
    }
}
