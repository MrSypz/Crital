package sypztep.crital.common;

import eu.midnightdust.lib.config.MidnightConfig;


public class ModConfig extends MidnightConfig {
    @Entry
    public static CritOptional critOptional = CritOptional.NEW_OVERHAUL;
    @Entry
    public static TierTypes tierTypes = TierTypes.STAR;
    @Entry(category = "client")
    public static boolean NewToolTip = true;
    @Entry(category = "client")
    public static boolean itemInfo = true;
    @Entry(category = "client")
    public static int xoffset = 0;
    @Entry(category = "client")
    public static int yoffset = 0;

    @Entry(category = "client")
    public static boolean useNewCritParticle = true;
    @Entry(category = "client")
    public static boolean mobCritParticle = true;
    @Entry
    public static boolean exceptoffhandslot = false;
    @Entry
    public static boolean genCritData = true;
    @Entry
    public static boolean modifyOnCraftbyPlayer = true;
    @Entry
    public static boolean modifyOnCraft = true;
    @Entry
    public static boolean uniqueStats = true;
    @Entry
    public static boolean randomUnique = true;
    @Entry
    public static boolean sweepCrit = true;
    @Entry
    public static boolean mobApplyCrit = true;
    @Entry
    public static boolean applyUnique = false;
    @Entry
    public static boolean entityItemModifier = true;

    public static boolean shouldDoCrit() {
        return critOptional == CritOptional.NEW_OVERHAUL || critOptional == CritOptional.KEEP_JUMPCRIT || critOptional != CritOptional.DISABLE;
    }
    public static boolean tierTypes() {
        return tierTypes == TierTypes.STAR || tierTypes == TierTypes.TEXT;
    }
    public enum CritOptional {
        NEW_OVERHAUL, KEEP_JUMPCRIT, DISABLE
    }
    public enum TierTypes {
        STAR, TEXT
    }
    static {
        MidnightConfig.init(CritalMod.MODID, ModConfig.class);
    }
}
