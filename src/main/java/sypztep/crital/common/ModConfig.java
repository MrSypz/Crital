package sypztep.crital.common;

import eu.midnightdust.lib.config.MidnightConfig;


public class ModConfig extends MidnightConfig {
    @Entry
    public static CritOptional critOptional = CritOptional.NEW_OVERHAUL;
    @Entry
    public static float critChanceMin = 0.2f; // Minimum multiplier increase
    @Entry
    public static float critChanceMax = 1.25f; // Maximum multiplier increase
    @Entry
    public static float critDamageMin = 0.7f; // Minimum multiplier increase
    @Entry
    public static float critDamageMax = 2.5f; // Maximum multiplier increase
    @Entry(category = "client")
    public static boolean itemInfo = true;
    @Entry(category = "client")
    public static int xoffset = 0;
    @Entry(category = "client")
    public static int yoffset = 0;
    @Entry
    public static boolean exceptoffhandslot = true;
    @Entry
    public static boolean genCritData = true;
    @Entry
    public static boolean offhandExtraStats = true;
    @Entry
    public static boolean sweepCrit = true;

    public static boolean shouldDoCrit() {
        return critOptional == CritOptional.NEW_OVERHAUL || critOptional == CritOptional.KEEP_JUMPCRIT || critOptional != CritOptional.DISABLE;
    }
    public enum CritOptional {
        NEW_OVERHAUL,KEEP_JUMPCRIT, DISABLE
    }
    static {
        MidnightConfig.init(CritalMod.MODID, ModConfig.class);
    }
}
