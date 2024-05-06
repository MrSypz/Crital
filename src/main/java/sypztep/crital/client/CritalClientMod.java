package sypztep.crital.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import sypztep.crital.client.gui.StatsScreen;
import sypztep.crital.client.packets2c.CritSyncPayload;
import sypztep.crital.common.util.NewCriticalOverhaul;

public class CritalClientMod implements ClientModInitializer {
    public static KeyBinding stats_screen = new KeyBinding("key.crital.stats", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_I, "category.crital.keybind");
    @Override
    public void onInitializeClient() {

        ClientPlayNetworking.registerGlobalReceiver(CritSyncPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                if (context.client().world != null && context.client().world.getEntityById(payload.entityId()) instanceof NewCriticalOverhaul invoker) {
                    invoker.crital$setCritical(payload.flag());
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
