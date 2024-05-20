package sypztep.crital.common.data;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Formatting;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.api.MaterialCritChanceProvider;

import java.util.Random;

import static net.minecraft.item.ArmorMaterials.*;
import static net.minecraft.item.ArmorMaterials.NETHERITE;

public class CritData {
    public static final String TIER_FLAG = CritalMod.MODID + "Tier";
    public static final String CRITCHANCE_FLAG = CritalMod.MODID + "CritChance_Flag";
    public static final String CRITDAMAGE_FLAG = CritalMod.MODID + "CritDamage_Flag";
    private static final Random random = new Random();
    public record CritResult(float critChance,float critDamage, CritTier tier) {}
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
    public static float getToolCritChance(ToolMaterial toolMaterial) {
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
    public static float getArmorCritChance(RegistryEntry<ArmorMaterial> armorMaterial) {
        if (armorMaterial.equals(IRON)) {
            return 3f;
        } else if (armorMaterial.equals(GOLD)) {
            return 2f;
        } else if (armorMaterial.equals(CHAIN)) {
            return 2.5f;
        } else if (armorMaterial.equals(DIAMOND)) {
            return 4f;
        } else if (armorMaterial.equals(NETHERITE)) {
            return 5f;
        }
        return 0f;  // Set a default value for other armor materials
    }
    public static <T> CritResult calculateCritValues(T material, MaterialCritChanceProvider<T> critChanceProvider, float critChanceMultiplier, float critDamageMultiplier) {
        CritTier tier = getRandomTier();
        float baseCritChance = critChanceProvider.getCritChance(material);
        double tierMultiplier = tier.getMultiplier();
        float critChance = (float) ((baseCritChance * tierMultiplier) * critChanceMultiplier);
        float critDamage = (float) ((baseCritChance * tierMultiplier) * critDamageMultiplier);
        return new CritResult(critChance, critDamage, tier);
    }
    public static Formatting getTierFormatting(String tier) {
        return switch (tier) {
            case "Uncommon" -> Formatting.GREEN;
            case "Rare" -> Formatting.DARK_AQUA;
            case "Epic" -> Formatting.DARK_PURPLE;
            case "Legendary" -> Formatting.GOLD;
            case "Mythic" -> Formatting.LIGHT_PURPLE;
            case "Celestial" -> Formatting.RED;
            default -> Formatting.WHITE;
        };
    }

}
