package sypztep.crital.common.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import sypztep.crital.common.api.MaterialCritChanceProvider;
import sypztep.crital.common.data.CritData;
import sypztep.crital.common.data.CritTier;

import static sypztep.crital.common.data.CritData.calculateCritValues;

public class CritalDataUtil {
    public static NbtCompound getNbtCompound(ItemStack stack) {
        NbtCompound value = new NbtCompound();
        @Nullable var data = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (data != null)
            value = data.copyNbt();
        return value;
    }

    public static CritTier getCritTierFromStack(ItemStack stack) {
        if (getNbtCompound(stack).contains(CritData.TIER_FLAG)) {
            String critTierName = CritalDataUtil.getNbtCompound(stack).getString(CritData.TIER_FLAG).trim().toUpperCase();
            return CritTier.valueOf(critTierName);
        }
        return null;
    }

    public static <T> void applyCritData(ItemStack stack, T material, MaterialCritChanceProvider<T> critChanceProvider, float critChanceMultiplier, float critDamageMultiplier) {
        CritData.CritResult result = calculateCritValues(material, critChanceProvider, critChanceMultiplier, critDamageMultiplier);
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.putFloat(CritData.CRITCHANCE_FLAG, result.critChance());
            currentNbt.putFloat(CritData.CRITDAMAGE_FLAG, result.critDamage());
            currentNbt.putString(CritData.TIER_FLAG, result.tier().getName());
        }));
    }
}
