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
    public boolean shouldDoCrit() {
        return critOptional == CritOptional.NEW_OVERHAUL || critOptional == CritOptional.KEEP_JUMPCRIT || critOptional != CritOptional.DISABLE;
    }
    public enum CritOptional {
        NEW_OVERHAUL,KEEP_JUMPCRIT, DISABLE
    }
}
