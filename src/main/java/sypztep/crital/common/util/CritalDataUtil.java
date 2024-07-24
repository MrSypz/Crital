package sypztep.crital.common.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import sypztep.crital.common.api.crital.NewCriticalOverhaul;
import sypztep.crital.common.data.CritResult;
import sypztep.crital.common.data.CritalData;
import sypztep.crital.common.data.CritTier;
import sypztep.crital.common.data.CritalItemData;

import java.util.Random;

public class CritalDataUtil {
    public static CritTier getCritTierFromStack(ItemStack stack) {
        if (stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().contains(CritalData.TIER_FLAG)) {
            String critTierName = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getString(CritalData.TIER_FLAG);
            return CritTier.fromName(critTierName);
        }
        return null;
    }

    public static void applyCritData(ItemStack stack) {
        CritResult result = calculateCritValues(stack);
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, applied -> applied.apply(compound -> {
            compound.putFloat(CritalData.CRITCHANCE, result.critChance());
            compound.putFloat(CritalData.CRITDAMAGE, result.critDamage());
            compound.putFloat(CritalData.CRITCHANCE_QUALITY, result.critChanceQuality());
            compound.putFloat(CritalData.CRITDAMAGE_QUALITY, result.critDamageQuality());
            if (stack.getItem() instanceof ArmorItem)
                compound.putFloat(CritalData.HEALTH_FLAG, result.health());
            compound.putString(CritalData.TIER_FLAG, result.tier().getName());
        }));
    }
    public static void applyCritData(ItemStack stack, CritTier tier) {
        CritResult result = calculateCritValues(stack, tier);
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, applied -> applied.apply(compound -> {
            compound.putFloat(CritalData.CRITCHANCE, result.critChance());
            compound.putFloat(CritalData.CRITDAMAGE, result.critDamage());
            compound.putFloat(CritalData.CRITCHANCE_QUALITY, result.critChanceQuality());
            compound.putFloat(CritalData.CRITDAMAGE_QUALITY, result.critDamageQuality());
            if (stack.getItem() instanceof ArmorItem)
                compound.putFloat(CritalData.HEALTH_FLAG, result.health());
            compound.putString(CritalData.TIER_FLAG, result.tier().getName());
        }));
    }

    public static CritResult calculateCritValues(ItemStack stack) {
        return calculateCritValues(stack, getRandomTier());
    }

    public static CritResult calculateCritValues(ItemStack stack, CritTier tier) {
        CritalItemData itemData = CritalItemData.getCritalItemData(stack);

        float baseCritChance = itemData.baseCritChance();
        float baseCritDamage = itemData.baseCritDamage();
        float minCritChance = itemData.minCritChanceMultiply();
        float maxCritChance = itemData.maxCritChanceMultiply();
        float minCritDamage = itemData.minCritDamageMultiply();
        float maxCritDamage = itemData.maxCritDamageMultiply();

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

    public static boolean matchesItemData(ItemStack stack) {
        String itemID = CritalItemData.getItemId(stack);
        CritalItemData itemData = CritalItemData.getCritalItemData(itemID);
        return itemData != null && itemID.equals(itemData.itemId());
    }

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
