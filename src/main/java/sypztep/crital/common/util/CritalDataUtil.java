package sypztep.crital.common.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import sypztep.crital.common.data.CritData;
import sypztep.crital.common.data.CritTier;

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

}
