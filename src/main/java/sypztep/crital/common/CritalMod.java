package sypztep.crital.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import sypztep.crital.client.packets2c.CritSyncPayload;
import sypztep.crital.common.init.ModConfig;
import sypztep.crital.common.screen.GrinderScreenHandler;

import java.util.logging.Logger;

public class CritalMod implements ModInitializer {
    public static final String MODID = "crital";
    public static final Logger LOGGER = Logger.getLogger(MODID);
    public static final ScreenHandlerType<GrinderScreenHandler> GRINDER_SCREEN_HANDLER_TYPE =
            new ScreenHandlerType<>(GrinderScreenHandler::new, FeatureFlags.VANILLA_FEATURES);

    public static Identifier id (String path) {
        return new Identifier(MODID,path);
    }
    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(CritSyncPayload.ID, CritSyncPayload.CODEC); // Server to Client
        ModConfig.init();

        Registry.register(Registries.SCREEN_HANDLER, id("grinder"), GRINDER_SCREEN_HANDLER_TYPE);
    }
}
