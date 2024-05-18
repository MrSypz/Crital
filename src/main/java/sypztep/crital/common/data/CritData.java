package sypztep.crital.common.data;

import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import sypztep.crital.common.CritalMod;

import java.util.Random;

public class CritData {
    public static final String TIER_FLAG = CritalMod.MODID + "Tier";
    public static final String CRITCHANCE_FLAG = CritalMod.MODID + "CritChance_Flag";
    public static final String CRITDAMAGE_FLAG = CritalMod.MODID + "CritDamage_Flag";
    private static final Random random = new Random();
    public record CritResult(float critChance, CritTier tier) {}
    private static CritTier getRandomTier() {
        double roll = random.nextDouble();
        if (roll < 0.4) return CritTier.COMMON;
        if (roll < 0.7) return CritTier.UNCOMMON;
        if (roll < 0.85) return CritTier.RARE;
        if (roll < 0.95) return CritTier.EPIC;
        if (roll < 0.99) return CritTier.LEGENDARY;
        if (roll < 0.999) return CritTier.MYTHIC;
        return CritTier.CELESTIAL;
    }
    private static float getToolCritChance(ToolMaterial toolMaterial) {
        return switch (toolMaterial) {
            case ToolMaterials.WOOD -> 2.0f;
            case ToolMaterials.STONE -> 2.5f;
            case ToolMaterials.IRON -> 3.0f;
            case ToolMaterials.GOLD -> 3.5f;
            case ToolMaterials.DIAMOND -> 4.0f;
            case ToolMaterials.NETHERITE -> 4.5f;
            default -> 0f;
        };
    }
    public static CritResult calculateCritChance(ToolMaterial toolMaterial) {
        CritTier tier = getRandomTier();
        float baseCritChance = getToolCritChance(toolMaterial);
        double multiplier = tier.getMultiplier();
        float critChance = (float) ((baseCritChance * multiplier) * 1.5);
        return new CritResult(critChance, tier);
    }

}
