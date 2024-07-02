package sypztep.crital.common.data;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Formatting;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.ModConfig;
import sypztep.crital.common.api.crital.MaterialCritChanceProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static net.minecraft.item.ArmorMaterials.*;

public class CritData {
    public static final String TIER_FLAG = CritalMod.MODID + "Tier_Flag";
    public static final String CRITCHANCE_FLAG = CritalMod.MODID + "CritChance";
    public static final String CRITDAMAGE_FLAG = CritalMod.MODID + "CritDamage";
    public static final String CRITCHANCE_QUALITY_FLAG = CritalMod.MODID + "CritChanceQuality";
    public static final String CRITDAMAGE_QUALITY_FLAG = CritalMod.MODID + "CritDamageQuality";
    public static final String HEALTH_FLAG = CritalMod.MODID + "Health_Flag";
    //---------
    private static final float CRIT_CHANCE_MIN = ModConfig.critChanceMin; // Minimum multiplier increase
    private static final float CRIT_CHANCE_MAX = ModConfig.critChanceMax; // Maximum multiplier increase
    private static final float CRIT_DAMAGE_MIN = ModConfig.critDamageMin; // Minimum multiplier increase
    private static final float CRIT_DAMAGE_MAX = ModConfig.critDamageMax; // Maximum multiplier increase
    public static Map<RegistryEntry<ArmorMaterial>,Float> ARMORCRITCHANCEMAP = new HashMap<>();
    public static final Random random = new Random();

    public static void init() {
        registerToolCritChanceMap();
        registerArmorCritChanceMap();
    }

    public record CritResult(float critChance, float critDamage, CritTier tier, float critChanceQuality,
                             float critDamageQuality, float health) {
    }
    //TODO: make it hashtable for faster and make it accept from other mod!

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

    public static Map<ToolMaterial,Float> TOOLRITCHANCEMAP = new HashMap<>();

    public static void registerToolCritChanceMap() {
        TOOLRITCHANCEMAP.put(ToolMaterials.WOOD,2.0f);
        TOOLRITCHANCEMAP.put(ToolMaterials.STONE,2.5f);
        TOOLRITCHANCEMAP.put(ToolMaterials.IRON,3.5f);
        TOOLRITCHANCEMAP.put(ToolMaterials.GOLD,3.0f);
        TOOLRITCHANCEMAP.put(ToolMaterials.DIAMOND,4.0f);
        TOOLRITCHANCEMAP.put(ToolMaterials.NETHERITE,4.5f);
        // TODO:remember to add armor material of WARDENITE to ARMORCRITCHANCEMAP in sifu mod value:6.0f
    }

    public static float getToolCritChance(ToolMaterial toolMaterial) {
        return TOOLRITCHANCEMAP.getOrDefault(toolMaterial,1f);
    }


    public static void registerArmorCritChanceMap() {
        ARMORCRITCHANCEMAP.put(LEATHER,1.75f);
        ARMORCRITCHANCEMAP.put(IRON,3f);
        ARMORCRITCHANCEMAP.put(GOLD,2f);
        ARMORCRITCHANCEMAP.put(CHAIN,2.5f);
        ARMORCRITCHANCEMAP.put(DIAMOND,4f);
        ARMORCRITCHANCEMAP.put(NETHERITE,7f);
        // TODO:remember to add armor material of WARDENITE to ARMORCRITCHANCEMAP in sifu mod value:7.0f
    }

    public static float getArmorCritChance(RegistryEntry<ArmorMaterial> armorMaterial) {
        return ARMORCRITCHANCEMAP.getOrDefault(armorMaterial, 1f);
    }

    public static <T> CritResult calculateCritValues(T material, MaterialCritChanceProvider<T> critChanceProvider) {
        CritTier tier = getRandomTier();
        float baseCritChance = critChanceProvider.getCritChance(material);
        float tierMultiplier = tier.getMultiplier();
        float getTierHealth = tier.getHealth();
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
        float critChanceQuality = calculateQualityPercentage(critChance, critChanceResultMin, critChanceResultMax);
        float critDamageQuality = calculateQualityPercentage(critDamage, critDamageResultMin, critDamageResultMax);

        return new CritResult(critChance, critDamage, tier, critChanceQuality, critDamageQuality, getTierHealth);
    }

    public static <T> CritResult calculateCritValues(T material, MaterialCritChanceProvider<T> critChanceProvider, CritTier tier) {
        float baseCritChance = critChanceProvider.getCritChance(material);
        float tierMultiplier = tier.getMultiplier();
        float getTierHealth = tier.getHealth();
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
        float critChanceQuality = calculateQualityPercentage(critChance, critChanceResultMin, critChanceResultMax);
        float critDamageQuality = calculateQualityPercentage(critDamage, critDamageResultMin, critDamageResultMax);

        return new CritResult(critChance, critDamage, tier, critChanceQuality, critDamageQuality, getTierHealth);
    }

    private static float calculateQualityPercentage(float value, float minValue, float maxValue) {
        return ((value - minValue) / (maxValue - minValue)) * 100;
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
