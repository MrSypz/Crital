package sypztep.crital.common;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import sypztep.crital.common.init.ModConfig;
import sypztep.crital.common.init.ModEntityAttributes;

public class CritalMod implements ModInitializer {
    public static final String MODID = "crital";
    public static Identifier id (String path) {
        return new Identifier(MODID,path);
    }
    @Override
    public void onInitialize() {
        ModConfig.init();
    }
}
