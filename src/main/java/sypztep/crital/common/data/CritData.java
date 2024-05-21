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

public class CritData {
    public static final String TIER_FLAG = CritalMod.MODID + "Tier";
    public static final String CRITCHANCE_FLAG = CritalMod.MODID + "CritChance_Flag";
    public static final String CRITDAMAGE_FLAG = CritalMod.MODID + "CritDamage_Flag";
    //---------
    public static final float CRIT_CHANCE_MIN = 0.2f; // Minimum multiplier increase
    public static final float CRIT_CHANCE_MAX = 1.75f; // Maximum multiplier increase
    public static final float CRIT_DAMAGE_MIN = 0.7f; // Minimum multiplier increase
    public static final float CRIT_DAMAGE_MAX = 3.0f; // Maximum multiplier increase
    private static final Random random = new Random();
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
            case ToolMaterials.IRON -> 3.5f;
            case ToolMaterials.GOLD -> 3.0f;
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
        return 0f;
    }
    public static <T> CritResult calculateCritValues(T material, MaterialCritChanceProvider<T> critChanceProvider) {
        CritTier tier = getRandomTier();
        float baseCritChance = critChanceProvider.getCritChance(material);
        float tierMultiplier = tier.getMultiplier();

        // Generate random increases within the specified ranges
        float critChanceIncrease = CRIT_CHANCE_MIN + random.nextFloat() * (CRIT_CHANCE_MAX - CRIT_CHANCE_MIN);
        float critDamageIncrease = CRIT_DAMAGE_MIN + random.nextFloat() * (CRIT_DAMAGE_MAX - CRIT_DAMAGE_MIN);

        // Apply the base calculations with the random increases
        float critChance = (baseCritChance * tierMultiplier) * critChanceIncrease;
        float critDamage = (baseCritChance * tierMultiplier) * critDamageIncrease;

        // Define the minimum and maximum possible results
        float critChanceResultMin = baseCritChance * tierMultiplier * CRIT_CHANCE_MIN;
        float critChanceResultMax = baseCritChance * tierMultiplier * CRIT_CHANCE_MAX;
        float critDamageResultMin = baseCritChance * tierMultiplier * CRIT_DAMAGE_MIN;
        float critDamageResultMax = baseCritChance * tierMultiplier * CRIT_DAMAGE_MAX;

        // Calculate the quality percentage
        float critChanceQuality = ((critChance - critChanceResultMin) / (critChanceResultMax - critChanceResultMin)) * 100;
        float critDamageQuality = ((critDamage - critDamageResultMin) / (critDamageResultMax - critDamageResultMin)) * 100;

        // Optional: Print or log the quality percentage
        System.out.printf("Crit Chance Quality: %.2f%%,\n Crit Damage Quality: %.2f%%%n", critChanceQuality, critDamageQuality);

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
