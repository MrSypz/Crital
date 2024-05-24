package sypztep.crital.common;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = CritalMod.MODID)
@Config.Gui.Background("minecraft:textures/block/dirt.png")

public class CritalConfig implements ConfigData {
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    @ConfigEntry.Category("gameplay")
    @Comment("New Crit feature overhaul. It have 3 Mode " +
            "\n1. New Overhaul : complete pure luck" +
            "\n2. Keep JumpCrit : still able to do jump crit but the crit is not apply with critdamage" +
            "\n3. Disable : Disable this feature")
    public CritOptional critOptional = CritOptional.NEW_OVERHAUL;
    @ConfigEntry.Category("visual")
    @Comment("Enable Info when press shift to see a percentage of quality data on item. ")
    public boolean itemInfo = true;
    @ConfigEntry.Category("visual")
    public int xoffset = 0;
    @ConfigEntry.Category("visual")
    public int yoffset = 0;
    @ConfigEntry.Category("gameplay")
    @Comment("Get anyslot but not for offhand.")
    public boolean exceptoffhandslot = true;
    @ConfigEntry.Category("gameplay")
    @Comment("Generate a data on item in world.")
    public boolean genCritData = true;


    public boolean shouldDoCrit() {
        return critOptional == CritOptional.NEW_OVERHAUL || critOptional == CritOptional.KEEP_JUMPCRIT || critOptional != CritOptional.DISABLE;
    }
    public enum CritOptional {
        NEW_OVERHAUL,KEEP_JUMPCRIT, DISABLE
    }
}
