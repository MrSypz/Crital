package sypztep.crital.common.data;

import sypztep.crital.common.CritalMod;
import sypztep.crital.common.api.crital.BorderTemplate;

import java.util.HashMap;
import java.util.Map;

public enum CritTier {
    COMMON("Common", -0.9f,-2.5f),
    UNCOMMON("Uncommon", 1.1f,8.25f),
    RARE("Rare", 1.75f,12.5f),
    EPIC("Epic", 2.5f,15.5f),
    LEGENDARY("Legendary", 3.5f,25),
    MYTHIC("Mythic", 5f,32.5f),
    CELESTIAL("Celestial", 8f,70.5f);

    private final String name;
    private final float multiplier;
    private final float health;
    private static final Map<CritTier, BorderTemplate> borderTemplates = new HashMap<>();

    CritTier(String name, float multiplier,float health) {
        this.name = name;
        this.multiplier = multiplier;
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public float getMultiplier() {
        return multiplier;
    }

    public float getHealth() {
        return health;
    }

    public BorderTemplate getBorderTemplate() {
        return borderTemplates.get(this);
    }

    private static final int BG_COLOR = -267386864;
    private static final String BORDER_TEXTURE_PATH = "textures/gui/border/borders.png";

    public static CritTier fromName(String name) {
        for (CritTier tier : values()) {
            if (tier.name.equals(name)) {
                return tier;
            }
        }
        throw new IllegalArgumentException("Unknown CritTier name: " + name);
    }
    static {
        borderTemplates.put(COMMON, new BorderTemplate(BG_COLOR, 0xBABABA, 0x565656, CritalMod.id(BORDER_TEXTURE_PATH)));
        borderTemplates.put(UNCOMMON, new BorderTemplate(BG_COLOR, 0x3FC43A, 0x133F2A, CritalMod.id(BORDER_TEXTURE_PATH)));
        borderTemplates.put(RARE, new BorderTemplate(BG_COLOR, 0x163F54, 0x163F54, CritalMod.id(BORDER_TEXTURE_PATH)));
        borderTemplates.put(EPIC, new BorderTemplate(BG_COLOR, 0x380F54, 0x380F54, CritalMod.id(BORDER_TEXTURE_PATH)));
        borderTemplates.put(LEGENDARY, new BorderTemplate(BG_COLOR, 0x6D3C24, 0x6D3C24, CritalMod.id(BORDER_TEXTURE_PATH)));
        borderTemplates.put(MYTHIC, new BorderTemplate(BG_COLOR, 0x5C4C2D, 0x5C4C2D, CritalMod.id(BORDER_TEXTURE_PATH)));
        borderTemplates.put(CELESTIAL, new BorderTemplate(BG_COLOR, 0xA591B6, 0xA591B6, CritalMod.id(BORDER_TEXTURE_PATH)));
    }
}


