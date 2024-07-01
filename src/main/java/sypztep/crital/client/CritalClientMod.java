package sypztep.crital.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import sypztep.crital.client.event.CritalTooltipRender;
import sypztep.crital.client.gui.GrinderScreen;
import sypztep.crital.client.gui.StatsScreen;
import sypztep.crital.client.payload.CritSyncPayload;
import sypztep.crital.client.payload.GrinderPayloadS2C;
import sypztep.crital.client.payload.QualityGrinderPayloadS2C;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.init.ModParticles;

public class CritalClientMod implements ClientModInitializer {
    public static KeyBinding stats_screen = new KeyBinding("key.crital.stats", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_I, "category.crital.keybind");
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(CritSyncPayload.ID, new CritSyncPayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(GrinderPayloadS2C.ID, new GrinderPayloadS2C.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(QualityGrinderPayloadS2C.ID, new QualityGrinderPayloadS2C.Receiver());

        ModParticles.registerFactor();
        ItemTooltipCallback.EVENT.register(new CritalTooltipRender());
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (stats_screen.wasPressed()) {
                client.setScreen(new StatsScreen());
            }
        });
        HandledScreens.register(CritalMod.GRINDER_SCREEN_HANDLER_TYPE, GrinderScreen::new);
    }
}
