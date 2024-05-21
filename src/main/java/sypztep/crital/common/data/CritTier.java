package sypztep.crital.common.data;

import sypztep.crital.common.CritalMod;
import sypztep.crital.common.api.BorderTemplate;

public enum CritTier {
    COMMON("Common", -0.9f),//dif 0
    UNCOMMON("Uncommon", 1.1f), // dif 0.2
    RARE("Rare", 1.75f),//dif 0.65
    EPIC("Epic", 2.5f),//dif 0.75
    LEGENDARY("Legendary", 3.5f),//diff 1
    MYTHIC("Mythic", 5f),//diff 1.5
    CELESTIAL("Celestial", 8f);//diff  3

    private final String name;
    private final float multiplier;
    private BorderTemplate borderTemplate;

    CritTier(String name, float multiplier) {
        this.name = name;
        this.multiplier = multiplier;
    }

    public String getName() {
        return name;
    }

    public float getMultiplier() {
        return multiplier;
    }

    public BorderTemplate getBorderTemplate() {
        return borderTemplate;
    }
    static final int bg = -267386864;
    public static void initializeTemplates() {
        COMMON.borderTemplate = new BorderTemplate(bg, 0xBABABA, 0x565656, CritalMod.id("textures/gui/border/border.png"));
        UNCOMMON.borderTemplate = new BorderTemplate(bg, 0x3FC43A, 0x133F2A, CritalMod.id("textures/gui/border/border.png"));
        RARE.borderTemplate = new BorderTemplate(bg, 0x163F54, 0x163F54, CritalMod.id("textures/gui/border/border.png"));
        EPIC.borderTemplate = new BorderTemplate(bg, 0x380F54, 0x380F54, CritalMod.id("textures/gui/border/border.png"));
        LEGENDARY.borderTemplate = new BorderTemplate(bg, 0x6D3C24, 0x6D3C24, CritalMod.id("textures/gui/border/border.png"));
        MYTHIC.borderTemplate = new BorderTemplate(bg, 0x5C4C2D, 0x5C4C2D, CritalMod.id("textures/gui/border/border.png"));
        CELESTIAL.borderTemplate = new BorderTemplate(bg, 0xA591B6, 0xA591B6, CritalMod.id("textures/gui/border/border.png"));
    }

    public static CritTier fromName(String name) {
        for (CritTier tier : values()) {
            if (tier.name.equals(name)) {
                return tier;
            }
        }
        throw new IllegalArgumentException("Unknown CritTier name: " + name);
    }

    static {
        initializeTemplates();
    }
}


