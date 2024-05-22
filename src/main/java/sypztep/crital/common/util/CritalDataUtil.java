package sypztep.crital.common.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;
import sypztep.crital.common.api.MaterialCritChanceProvider;
import sypztep.crital.common.data.CritData;
import sypztep.crital.common.data.CritResult;
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
            String critTierName = CritalDataUtil.getNbtCompound(stack).getString(CritData.TIER_FLAG);
            return CritTier.fromName(critTierName);
        }
        return null;
    }
    @Unique
    public static   <T> void applyCritData(ItemStack stack, T material, MaterialCritChanceProvider<T> critChanceProvider) {
        CritResult result = calculateCritValues(material, critChanceProvider);
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(itemnbt -> {
            itemnbt.putFloat(CritData.CRITCHANCE_FLAG, result.critChance());
            itemnbt.putFloat(CritData.CRITDAMAGE_FLAG, result.critDamage());
            itemnbt.putFloat(CritData.CRITCHANCE_QUALITY_FLAG, result.critChanceQuality());
            itemnbt.putFloat(CritData.CRITDAMAGE_QUALITY_FLAG, result.critDamageQuality());
            itemnbt.putString(CritData.TIER_FLAG, result.tier().getName());
        }));
    }
}
