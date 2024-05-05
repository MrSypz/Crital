package sypztep.crital.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import sypztep.crital.client.packets2c.SyncCritPayload;
import sypztep.crital.common.init.ModConfig;
import sypztep.crital.common.init.ModEntityAttributes;

public class CritalMod implements ModInitializer {
    public static final String MODID = "crital";
    public static Identifier id (String path) {
        return new Identifier(MODID,path);
    }
    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(SyncCritPayload.ID, SyncCritPayload.CODEC);
        ModConfig.init();
    }
}
