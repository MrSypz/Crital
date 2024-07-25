package sypztep.crital.common.data;

import sypztep.crital.common.CritalMod;
import sypztep.crital.common.api.crital.BorderTemplate;

import java.util.HashMap;
import java.util.Map;

public enum CritTier {
    COMMON("Common", -0.9f),
    UNCOMMON("Uncommon", 1.1f),
    RARE("Rare", 1.75f),
    EPIC("Epic", 2.5f),
    LEGENDARY("Legendary", 3.5f),
    MYTHIC("Mythic", 5f),
    CELESTIAL("Celestial", 8f);

    private final String name;
    private final float multiplier;
    private static final Map<CritTier, BorderTemplate> borderTemplates = new HashMap<>();

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
        borderTemplates.put(COMMON, new BorderTemplate(BG_COLOR,0xF01F2542, 0xF4C2C2CD, 0xF04f4f53, CritalMod.id(BORDER_TEXTURE_PATH)));
        borderTemplates.put(UNCOMMON, new BorderTemplate(BG_COLOR, 0xF01F4225, 0xF43FC43A, 0xF0133F2A, CritalMod.id(BORDER_TEXTURE_PATH)));
        borderTemplates.put(RARE, new BorderTemplate(BG_COLOR,0xF01F3942 ,0xF43589B6, 0xF0123141, CritalMod.id(BORDER_TEXTURE_PATH)));
        borderTemplates.put(EPIC, new BorderTemplate(BG_COLOR,0xF0620039 ,0xF4C90076, 0xF064003B, CritalMod.id(BORDER_TEXTURE_PATH)));
        borderTemplates.put(LEGENDARY, new BorderTemplate(BG_COLOR,0xF05C5426 ,0xF4DFB433, 0xF09C7D23, CritalMod.id(BORDER_TEXTURE_PATH)));
        borderTemplates.put(MYTHIC, new BorderTemplate(BG_COLOR, 0xF0802A5C,0xF4C217B5, 0xF0610B5A, CritalMod.id(BORDER_TEXTURE_PATH)));
        borderTemplates.put(CELESTIAL, new BorderTemplate(0xF4291523,0xF0651626,0xF4f44336, 0xF0a60a0a, CritalMod.id(BORDER_TEXTURE_PATH)));
    }
}


