package sypztep.crital.common.init;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import sypztep.crital.common.CritalConfig;

public class ModConfig {
    public static CritalConfig CONFIG = new CritalConfig();
    public static void init() {
        //Registering Cloth Config (GSON)
        AutoConfig.register(CritalConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(CritalConfig.class).getConfig();
    }
}
