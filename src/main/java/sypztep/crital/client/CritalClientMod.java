package sypztep.crital.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;
import sypztep.crital.client.gui.StatsScreen;
import sypztep.crital.client.packets2c.SyncCritPayload;
import sypztep.crital.common.util.NewCriticalOverhaul;
import sypztep.crital.common.util.ServerSyncCritUtil;

public class CritalClientMod implements ClientModInitializer {
    public static KeyBinding stats_screen = new KeyBinding("key.crital.stats", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_I, "category.crital.keybind");
    @Override
    public void onInitializeClient() {
        PayloadTypeRegistry.playS2C().register(SyncCritPayload.ID, SyncCritPayload.CODEC);

        ClientPlayNetworking.registerGlobalReceiver(SyncCritPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                ServerSyncCritUtil packet = new ServerSyncCritUtil((PacketByteBuf) SyncCritPayload.CODEC);
                if (context.player().getWorld() != null && context.client().world.getEntityById(packet.getEntityId()) instanceof NewCriticalOverhaul invoker) {
                    invoker.crital$setCritical(packet.setBoolean());
                }
            });
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (stats_screen.wasPressed()) {
                client.setScreen(new StatsScreen());
            }
        });
    }
}
