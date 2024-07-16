package sypztep.crital.client.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import sypztep.penomior.common.util.ParticleUtil;

import java.awt.*;

public record AddCritParticlesPayload(int entityId) implements CustomPayload {
    public static final Id<AddCritParticlesPayload> ID = CustomPayload.id("add_critparticle");
    public static final PacketCodec<PacketByteBuf, AddCritParticlesPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            AddCritParticlesPayload::entityId,
            AddCritParticlesPayload::new
    );

    public static void send(ServerPlayerEntity player, int entityId) {
        ServerPlayNetworking.send(player, new AddCritParticlesPayload(entityId));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<AddCritParticlesPayload> {
        @Override
        public void receive(AddCritParticlesPayload payload, ClientPlayNetworking.Context context) {
            Entity entity = context.player().getWorld().getEntityById(payload.entityId());
            if (entity != null)
                ParticleUtil.spawnTextParticle(entity, Text.translatable("crital.text.crit"),new Color(1.0f, 0.310f, 0.0f), -0.055f); //this one can't active cuz world is server
        }
    }
}
