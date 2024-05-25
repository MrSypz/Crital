package sypztep.crital.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import sypztep.crital.common.init.*;
import sypztep.crital.common.packetc2s.GrindQualityPayloadC2S;
import sypztep.crital.common.packetc2s.GrinderPayloadC2S;
import sypztep.crital.common.screen.GrinderScreenHandler;

import java.util.logging.Logger;

public class CritalMod implements ModInitializer {
    public static final String MODID = "crital";
    public static final Logger LOGGER = Logger.getLogger(MODID);
    public static ScreenHandlerType<GrinderScreenHandler> GRINDER_SCREEN_HANDLER_TYPE;

    public static Identifier id (String path) {
        return new Identifier(MODID,path);
    }
    @Override
    public void onInitialize() {
        ModPayload.init();
        ModConfig.init();
        ModBlockItem.init();
        ModItem.init();
        ModItemGroup.init();

        ServerPlayNetworking.registerGlobalReceiver(GrinderPayloadC2S.ID, (payload, context) -> GrinderPayloadC2S.receiver(context));
        ServerPlayNetworking.registerGlobalReceiver(GrindQualityPayloadC2S.ID, (payload, context) -> GrindQualityPayloadC2S.receiver(context));

        GRINDER_SCREEN_HANDLER_TYPE = Registry.register(Registries.SCREEN_HANDLER, "grinder",
                new ScreenHandlerType<>((syncId, inventory) -> new GrinderScreenHandler(syncId, inventory, ScreenHandlerContext.EMPTY), FeatureFlags.VANILLA_FEATURES));
    }

}
