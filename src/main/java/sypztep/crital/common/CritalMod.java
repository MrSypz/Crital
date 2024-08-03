package sypztep.crital.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sypztep.crital.common.command.GrinderCommand;
import sypztep.crital.common.init.*;
import sypztep.crital.common.payload.GrindQualityPayloadC2S;
import sypztep.crital.common.payload.GrinderPayloadC2S;
import sypztep.crital.common.reload.CritalItemReloadListener;
import sypztep.crital.common.screen.GrinderScreenHandler;

public class CritalMod implements ModInitializer {
    public static final String MODID = "crital";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final String VERSION = FabricLoader.getInstance().getModContainer(MODID).get().getMetadata().getVersion().toString();
    public static ScreenHandlerType<GrinderScreenHandler> GRINDER_SCREEN_HANDLER_TYPE;

    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Crital Initialize");
        LOGGER.info("Crital Version : {}", VERSION);
        ModPayload.init();
        ModBlockItem.init();
        ModItem.init();
        ModItemGroup.init();
        ModDataComponent.init();

        ServerPlayNetworking.registerGlobalReceiver(GrinderPayloadC2S.ID, new GrinderPayloadC2S.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(GrindQualityPayloadC2S.ID, new GrindQualityPayloadC2S.Receiver());

        CommandRegistrationCallback.EVENT.register(new GrinderCommand());

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new CritalItemReloadListener());

        GRINDER_SCREEN_HANDLER_TYPE = Registry.register(Registries.SCREEN_HANDLER, "grinder", new ScreenHandlerType<>((syncId, inventory) -> new GrinderScreenHandler(syncId, inventory, ScreenHandlerContext.EMPTY), FeatureFlags.VANILLA_FEATURES));

    }
}
