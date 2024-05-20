package sypztep.crital.common.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import sypztep.crital.common.data.CritData;
import sypztep.crital.common.data.CritTier;

public class CritalDataUtil {
    public static NbtCompound getNbtCompoundFromStack(ItemStack stack) {
        NbtCompound value = new NbtCompound();
        @Nullable var data = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (data != null)
            value = data.copyNbt();
        return value;
    }
    public static CritTier getCritTierFromStack(ItemStack stack) {
        if (getNbtCompoundFromStack(stack).contains(CritData.TIER_FLAG)) {
            String critTierName = CritalDataUtil.getNbtCompoundFromStack(stack).getString(CritData.TIER_FLAG).trim().toUpperCase();
            return CritTier.valueOf(critTierName);
        }
        return null;
    }

}
