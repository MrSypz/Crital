package sypztep.crital.common.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CritalDataUtil {
    public static final Random random = new Random();
    public static CritTier getCritTierFromStack(ItemStack stack) {
        if (stack.getOrDefault(ModDataComponent.CRITAL, NbtComponent.DEFAULT).copyNbt().contains(CritalData.TIER_FLAG)) {
            String critTierName = stack.getOrDefault(ModDataComponent.CRITAL, NbtComponent.DEFAULT).copyNbt().getString(CritalData.TIER_FLAG);
            return CritTier.fromName(critTierName);
        }
        return null;
    }

    public static void applyCritData(ItemStack stack) {
        CritResult result = calculateCritValues(stack);
        stack.apply(ModDataComponent.CRITAL, NbtComponent.DEFAULT, applied -> applied.apply(compound -> {
            compound.putFloat(CritalData.CRITCHANCE, result.critChance());
            compound.putFloat(CritalData.CRITDAMAGE, result.critDamage());
            compound.putFloat(CritalData.CRITCHANCE_QUALITY, result.critChanceQuality());
            compound.putFloat(CritalData.CRITDAMAGE_QUALITY, result.critDamageQuality());
            if (stack.getItem() instanceof ArmorItem)
                compound.putFloat(CritalData.VITALITY, result.baseUniqueAmpifier()); //UNIQUE
            compound.putString(CritalData.TIER_FLAG, result.tier().getName());
        }));
    }
    public static void applyCritData(ItemStack stack, CritTier tier) {
        CritResult result = calculateCritValues(stack, tier);
        stack.apply(ModDataComponent.CRITAL, NbtComponent.DEFAULT, applied -> applied.apply(compound -> {
            compound.putFloat(CritalData.CRITCHANCE, result.critChance());
            compound.putFloat(CritalData.CRITDAMAGE, result.critDamage());
            compound.putFloat(CritalData.CRITCHANCE_QUALITY, result.critChanceQuality());
            compound.putFloat(CritalData.CRITDAMAGE_QUALITY, result.critDamageQuality());
            if (stack.getItem() instanceof ArmorItem)
                compound.putFloat(CritalData.VITALITY, result.baseUniqueAmpifier()); //UNIQUE
            compound.putString(CritalData.TIER_FLAG, result.tier().getName());
        }));
    }
    public static void applyCritData(ItemStack stack, CritTier tier, float chancePerc, float damagePerc) {
        CritResult result = calculateCritValues(stack, tier, chancePerc, damagePerc);

        stack.apply(ModDataComponent.CRITAL, NbtComponent.DEFAULT, applied -> applied.apply(compound -> {
            compound.putFloat(CritalData.CRITCHANCE, result.critChance());
            compound.putFloat(CritalData.CRITDAMAGE, result.critDamage());
            compound.putFloat(CritalData.CRITCHANCE_QUALITY, result.critChanceQuality());
            compound.putFloat(CritalData.CRITDAMAGE_QUALITY, result.critDamageQuality());
            compound.putString(CritalData.TIER_FLAG, result.tier().getName());
        }));

        stack.apply(ModDataComponent.UNIQUE, NbtComponent.DEFAULT, applied -> applied.apply(compound -> {
            if (compound.contains(CritalData.UNIQUE_APPLIED_MARKER)) {
                stack.remove(ModDataComponent.UNIQUE);
            } else {
                if (isArmor(stack)) {
                    compound.putFloat(CritalData.VITALITY, result.baseUniqueAmpifier());
                } else {
                    compound.putFloat(CritalData.OMNIVAMP, result.baseUniqueAmpifier());
                }
                compound.putBoolean(CritalData.UNIQUE_APPLIED_MARKER, true);
            }
        }));
    }

    private static boolean isArmor(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem;
    }

    public static CritResult calculateCritValues(ItemStack stack) {
        return calculateCritValues(stack, getRandomTier());
    }

    public static CritResult calculateCritValues(ItemStack stack, CritTier tier) {
        return calculateCritValues(stack,tier, random.nextFloat(), random.nextFloat());
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
        float baseUniqueAmpifier = itemData.baseUniqueAmpifier() * tierMultiplier;

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

        return new CritResult(critChance, critDamage, tier, critChanceQuality, critDamageQuality, baseUniqueAmpifier);
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

    public static void ReplaceAttributeModifier(EntityAttributeInstance att, EntityAttributeModifier mod) {
        att.removeModifier(mod);
        att.addPersistentModifier(mod);
    }
    public static List<NbtCompound> getNbtFromEquippedSlots(LivingEntity living) {
        List<NbtCompound> nbtList = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (ModConfig.exceptoffhandslot && slot == EquipmentSlot.OFFHAND) continue;
            ItemStack itemStack = living.getEquippedStack(slot);
            if (!itemStack.isEmpty()) {
                nbtList.add(ItemStackHelper.getNbtCompound(itemStack , ModDataComponent.CRITAL));
            }
        }
        return nbtList;
    }
    public static List<NbtCompound> getUniquebtFromEquippedSlots(LivingEntity living) {
        List<NbtCompound> nbtList = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (ModConfig.exceptoffhandslot && slot == EquipmentSlot.OFFHAND) continue;
            ItemStack itemStack = living.getEquippedStack(slot);
            if (!itemStack.isEmpty()) {
                nbtList.add(ItemStackHelper.getNbtCompound(itemStack , ModDataComponent.UNIQUE));
            }
        }
        return nbtList;
    }

    public static List<NbtCompound> getNbtFromArmorSlots(LivingEntity living) {
        List<NbtCompound> nbtList = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot != EquipmentSlot.HEAD && slot != EquipmentSlot.FEET && slot != EquipmentSlot.CHEST && slot != EquipmentSlot.LEGS)
                continue;
            ItemStack itemStack = living.getEquippedStack(slot);
            if (!itemStack.isEmpty()) {
                nbtList.add(ItemStackHelper.getNbtCompound(itemStack ,ModDataComponent.CRITAL));
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
