package sypztep.crital.common.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Formatting;
import sypztep.crital.common.ModConfig;
import sypztep.crital.common.api.crital.NewCriticalOverhaul;
import sypztep.crital.common.data.CritResult;
import sypztep.crital.common.data.CritalData;
import sypztep.crital.common.data.CritTier;
import sypztep.crital.common.data.CritalItemDataEntry;
import sypztep.crital.common.init.ModDataComponent;
import sypztep.tyrannus.common.util.ItemStackHelper;

import java.util.*;

public final class CritalDataUtil {
    public static final Random random = new Random();
    /*------------------CritData--------------------------*/
    public static String getCritChance(ItemStack stack) {
        return ItemStackHelper.getNbtCompound(stack, ModDataComponent.CRITAL).getString(CritalData.CRITCHANCE);
    }
    public static String getCritDamage(ItemStack stack) {
        return ItemStackHelper.getNbtCompound(stack, ModDataComponent.CRITAL).getString(CritalData.CRITDAMAGE);
    }
    public static String getCritChanceQuality(ItemStack stack) {
        return ItemStackHelper.getNbtCompound(stack, ModDataComponent.CRITAL).getString(CritalData.CRITCHANCE_QUALITY);
    }
    public static String getCritDamageQuality(ItemStack stack) {
        return ItemStackHelper.getNbtCompound(stack, ModDataComponent.CRITAL).getString(CritalData.CRITDAMAGE_QUALITY);
    }
    public static String getTier(ItemStack stack) {
        return ItemStackHelper.getNbtCompound(stack, ModDataComponent.CRITAL).getString(CritalData.TIER);
    }

    public static CritTier getCritTierFromStack(ItemStack stack) {
        if (ItemStackHelper.getNbtCompound(stack, ModDataComponent.CRITAL).contains(CritalData.TIER)) {
            return CritTier.fromName(getTier(stack));
        }
        return null;
    }

    public static void applyCritData(ItemStack stack) {
        applyCritData(stack, null, 0, 0);
    }

    public static void applyCritData(ItemStack stack, CritTier tier) {
        applyCritData(stack, tier, 0, 0);
    }

    public static void applyCritData(ItemStack stack, CritTier tier, float chancePerc, float damagePerc) {
        CritResult result;
        Random random = new Random();
        float chance = chancePerc != 0 ? chancePerc : random.nextFloat();
        float damage = damagePerc != 0 ? damagePerc : random.nextFloat();

        if (tier == null) {
            result = calculateCritValues(stack); // Calculate crit values without specific tier and percentages
        } else {
            result = calculateCritValues(stack, tier, chance, damage); // Calculate crit values with specific tier and percentages
        }

        applyCritValues(stack, result); // Apply the calculated crit values to the ItemStack
    }

    private static void applyCritValues(ItemStack stack, CritResult result) {
        stack.apply(ModDataComponent.CRITAL, NbtComponent.DEFAULT, applied -> applied.apply(compound -> {
            compound.putFloat(CritalData.CRITCHANCE, result.critChance());
            compound.putFloat(CritalData.CRITDAMAGE, result.critDamage());
            compound.putFloat(CritalData.CRITCHANCE_QUALITY, result.critChanceQuality());
            compound.putFloat(CritalData.CRITDAMAGE_QUALITY, result.critDamageQuality());
            compound.putString(CritalData.TIER, result.tier().getName());
        }));
    }

    public static CritResult calculateCritValues(ItemStack stack) {
        return calculateCritValues(stack, getRandomTier());
    }

    public static CritResult calculateCritValues(ItemStack stack, CritTier tier) {
        return calculateCritValues(stack, tier, random.nextFloat(), random.nextFloat());
    }

    public static CritResult calculateCritValues(ItemStack stack, CritTier tier, float chancePerc, float damagePerc) {
        // Fetch the item data; if not found, return a default CritResult with zeros
        Optional<CritalItemDataEntry> optionalItemData = CritalItemDataEntry.getCritalItemData(stack);

        if (optionalItemData.isEmpty()) {
            // Log a warning and return a default result to avoid crashing
            System.err.println("Warning: Item data not found for item: " + stack.getItem());
            return new CritResult(0, 0, tier, 0, 0);
        }

        // Fetch the item data entry
        CritalItemDataEntry itemData = optionalItemData.get();

        // Retrieve base and multiplier values
        float baseCritChance = itemData.baseCritChance();
        float baseCritDamage = itemData.baseCritDamage();
        float minCritChance = itemData.minCritChanceMultiply();
        float maxCritChance = itemData.maxCritChanceMultiply();
        float minCritDamage = itemData.minCritDamageMultiply();
        float maxCritDamage = itemData.maxCritDamageMultiply();

        // Retrieve tier multiplier
        float tierMultiplier = tier.getMultiplier();

        // Generate increases within the specified ranges
        float critChanceIncrease = minCritChance + chancePerc * (maxCritChance - minCritChance);
        float critDamageIncrease = minCritDamage + damagePerc * (maxCritDamage - minCritDamage);

        // Apply base calculations with the increases
        float critChance = (baseCritChance * tierMultiplier) * critChanceIncrease;
        float critDamage = (baseCritDamage * tierMultiplier) * critDamageIncrease;

        // Define minimum and maximum possible results
        float critChanceResultMin = baseCritChance * tierMultiplier * minCritChance;
        float critChanceResultMax = baseCritChance * tierMultiplier * maxCritChance;
        float critDamageResultMin = baseCritDamage * tierMultiplier * minCritDamage;
        float critDamageResultMax = baseCritDamage * tierMultiplier * maxCritDamage;

        // Calculate the quality percentage
        float critChanceQuality = calculateQualityPercentage(critChance, critChanceResultMin, critChanceResultMax);
        float critDamageQuality = calculateQualityPercentage(critDamage, critDamageResultMin, critDamageResultMax);

        // Return the calculated result
        return new CritResult(critChance, critDamage, tier, critChanceQuality, critDamageQuality);
    }


    private static float calculateQualityPercentage(float value, float minValue, float maxValue) {
        return ((value - minValue) / (maxValue - minValue)) * 100;
    }

    public static boolean matchesItemData(ItemStack stack) {
        String itemID = CritalItemDataEntry.getItemId(stack);
        Optional<CritalItemDataEntry> itemDataOpt = CritalItemDataEntry.getCritalItemData(itemID);
        return itemDataOpt.isPresent();
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


    public static List<NbtCompound> getNbtFromEquippedSlots(LivingEntity living) {
        List<NbtCompound> nbtList = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (ModConfig.exceptoffhandslot && slot == EquipmentSlot.OFFHAND) continue;
            ItemStack itemStack = living.getEquippedStack(slot);
            if (!itemStack.isEmpty()) {
                nbtList.add(ItemStackHelper.getNbtCompound(itemStack, ModDataComponent.CRITAL));
            }
        }
        return nbtList;
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
