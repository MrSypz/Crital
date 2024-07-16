package sypztep.crital.common.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import sypztep.crital.common.api.crital.NewCriticalOverhaul;
import sypztep.crital.common.data.CritalData;
import sypztep.crital.common.data.CritTier;
import sypztep.crital.common.data.CritalItemData;
import sypztep.tyrannus.common.util.ItemStackHelper;

import java.util.Random;

public class CritalDataUtil {
    public static float getCritChance(ItemStack stack) {
        return ItemStackHelper.getNbtCompound(stack).getFloat(CritalData.CRITCHANCE);
    }
    public static CritTier getCritTierFromStack(ItemStack stack) {
        if (ItemStackHelper.getNbtCompound(stack).contains(CritalData.TIER_FLAG)) {
            String critTierName = ItemStackHelper.getNbtCompound(stack).getString(CritalData.TIER_FLAG);
            return CritTier.fromName(critTierName);
        }
        return null;
    }
//    public static <T> void applyCritData(ItemStack stack, T material, MaterialCritChanceProvider<T> critChanceProvider) {
//        CritalData.CritResult result = calculateCritValues(material, critChanceProvider);
//        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(itemnbt -> {
//            itemnbt.putFloat(CritalData.CRITCHANCE, result.critChance());
//            itemnbt.putFloat(CritalData.CRITDAMAGE, result.critDamage());
//            itemnbt.putFloat(CritalData.CRITCHANCE_QUALITY, result.critChanceQuality());
//            itemnbt.putFloat(CritalData.CRITDAMAGE_QUALITY, result.critDamageQuality());
//            if (stack.isIn(ItemTags.ARMOR_ENCHANTABLE))
//                itemnbt.putFloat(CritalData.HEALTH_FLAG, result.health());
//            itemnbt.putString(CritalData.TIER_FLAG, result.tier().getName());
//        }));
//    }
//    public static <T> void applyCritData(ItemStack stack, T material, MaterialCritChanceProvider<T> critChanceProvider, CritTier tier) {
//        CritalData.CritResult result = calculateCritValues(material, critChanceProvider, tier);
//        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(itemnbt -> {
//            itemnbt.putFloat(CritalData.CRITCHANCE, result.critChance());
//            itemnbt.putFloat(CritalData.CRITDAMAGE, result.critDamage());
//            itemnbt.putFloat(CritalData.CRITCHANCE_QUALITY, result.critChanceQuality());
//            itemnbt.putFloat(CritalData.CRITDAMAGE_QUALITY, result.critDamageQuality());
//            if (stack.isIn(ItemTags.ARMOR_ENCHANTABLE))
//                itemnbt.putFloat(CritalData.HEALTH_FLAG, result.health());
//            itemnbt.putString(CritalData.TIER_FLAG, result.tier().getName());
//        }));
//    }

    public static float getCritRate(ClientPlayerEntity player) {
        if (player instanceof NewCriticalOverhaul invoker)
            return invoker.getTotalCritRate();
        return 0.0F; // Return a default value if the player is not a LivingEntityInvoker
    }

    public static float getCritDamage(ClientPlayerEntity player) {
        if (player instanceof NewCriticalOverhaul invoker)
            return invoker.getTotalCritDamage();
        return 0.0F; // Return a default value if the player is not a LivingEntityInvoker
    }
    public static final Random random = new Random();

    public record CritResult(float critChance, float critDamage, CritTier tier, float critChanceQuality,
                             float critDamageQuality, float health) {
    }

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


//    public static float getToolCritChance(ToolMaterial toolMaterial) {
//        return TOOLRITCHANCEMAP.getOrDefault(toolMaterial,1f);
//    }
//
//
//    public static float getArmorCritChance(RegistryEntry<ArmorMaterial> armorMaterial) {
//        return ARMORCRITCHANCEMAP.getOrDefault(armorMaterial, 1f);
//    }

    public static CritResult calculateCritValues(ItemStack stack) {
        CritalItemData itemData = CritalItemData.getCritalItemData(stack);
        CritTier tier = getRandomTier();

        float baseCritChance = itemData.baseCritChance();
        float baseCritDamage = itemData.baseCritDamage();
        float minCritChance = itemData.minCritChanceMultiply();
        float maxCritChance = itemData.maxCritChanceMultiply();
        float minCritDamage = itemData.maxCritChanceMultiply();
        float maxCritDamage = itemData.maxCritChanceMultiply();

        float tierMultiplier = tier.getMultiplier();
        float getTierHealth = tier.getHealth();
        // Generate random increases within the specified ranges
        float critChanceIncrease = minCritChance + random.nextFloat() * (maxCritChance - minCritChance);
        float critDamageIncrease = minCritDamage + random.nextFloat() * (maxCritDamage - minCritDamage);

        // Apply the base calculations with the random increases
        float critChance = (baseCritChance * tierMultiplier) * critChanceIncrease;
        float critDamage = (baseCritDamage * tierMultiplier) * critDamageIncrease;

        // Define the minimum and maximum possible results
        float critChanceResultMin = baseCritChance * tierMultiplier * minCritChance;
        float critChanceResultMax = baseCritChance * tierMultiplier * maxCritChance;
        float critDamageResultMin = baseCritDamage * tierMultiplier * minCritDamage;
        float critDamageResultMax = baseCritDamage * tierMultiplier * maxCritDamage;

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
