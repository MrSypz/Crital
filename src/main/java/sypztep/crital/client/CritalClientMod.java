package sypztep.crital.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import sypztep.crital.client.gui.GrinderScreen;
import sypztep.crital.client.gui.StatsScreen;
import sypztep.crital.client.packets2c.CritSyncPayload;
import sypztep.crital.client.packets2c.GrinderPayloadS2C;
import sypztep.crital.common.CritalMod;

public class CritalClientMod implements ClientModInitializer {
    public static KeyBinding stats_screen = new KeyBinding("key.crital.stats", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_I, "category.crital.keybind");
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(CritSyncPayload.ID, (payload, context) -> CritSyncPayload.receiver(payload,context::client));
        ClientPlayNetworking.registerGlobalReceiver(GrinderPayloadS2C.ID, ((payload, context) -> GrinderPayloadS2C.receiver(payload,context::client)));

        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> TooltipItem.onTooltipRender(stack,lines,context));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (stats_screen.wasPressed()) {
                client.setScreen(new StatsScreen());
            }
        });
        FabricLoader.getInstance().getModContainer(CritalMod.MODID).ifPresent(modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(CritalMod.id("alternate_border"), modContainer, ResourcePackActivationType.NORMAL));
        HandledScreens.register(CritalMod.GRINDER_SCREEN_HANDLER_TYPE, GrinderScreen::new);
    }
}
