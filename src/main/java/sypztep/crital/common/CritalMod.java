package sypztep.crital.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import sypztep.crital.client.packets2c.CritSyncPayload;
import sypztep.crital.common.data.CritOverhaulConfig;
import sypztep.crital.common.init.ModConfig;
import sypztep.crital.common.init.ModCritalData;

import java.util.logging.Logger;

public class CritalMod implements ModInitializer {
    public static final String MODID = "crital";
    public static final Logger LOGGER = Logger.getLogger(MODID);
    public static Identifier id (String path) {
        return new Identifier(MODID,path);
    }
    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(CritSyncPayload.ID, CritSyncPayload.CODEC); // Server to Client
        ModConfig.init();
        ModCritalData.initItemData();
    }
}
