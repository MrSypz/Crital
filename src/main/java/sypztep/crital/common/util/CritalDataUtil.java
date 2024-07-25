package sypztep.crital.common.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CritalDataUtil {
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
        CritResult result = (tier == null)
                ? calculateCritValues(stack)
                : calculateCritValues(stack, tier, chancePerc, damagePerc);

        applyCritValues(stack, result);
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
        CritalItemDataEntry itemData = CritalItemDataEntry.getCritalItemData(stack);

        float baseCritChance = itemData.baseCritChance();
        float baseCritDamage = itemData.baseCritDamage();
        float minCritChance = itemData.minCritChanceMultiply();
        float maxCritChance = itemData.maxCritChanceMultiply();
        float minCritDamage = itemData.minCritDamageMultiply();
        float maxCritDamage = itemData.maxCritDamageMultiply();

        float tierMultiplier = tier.getMultiplier();

        // Generate random increases within the specified ranges
        float critChanceIncrease = minCritChance + chancePerc * (maxCritChance - minCritChance);
        float critDamageIncrease = minCritDamage + damagePerc * (maxCritDamage - minCritDamage);

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

        return new CritResult(critChance, critDamage, tier, critChanceQuality, critDamageQuality);
    }

    private static float calculateQualityPercentage(float value, float minValue, float maxValue) {
        return ((value - minValue) / (maxValue - minValue)) * 100;
    }

    public static boolean matchesItemData(ItemStack stack) {
        String itemID = CritalItemDataEntry.getItemId(stack);
        CritalItemDataEntry itemData = CritalItemDataEntry.getCritalItemData(itemID);
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
