package sypztep.crital.common.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.ItemTags;
import org.jetbrains.annotations.Nullable;
import sypztep.crital.common.api.crital.MaterialCritChanceProvider;
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
            String critTierName = CritalDataUtil.getNbtCompound(stack).getString(CritData.TIER_FLAG);
            return CritTier.fromName(critTierName);
        }
        return null;
    }
    public static <T> void applyCritData(ItemStack stack, T material, MaterialCritChanceProvider<T> critChanceProvider) {
        CritData.CritResult result = calculateCritValues(material, critChanceProvider);
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(itemnbt -> {
            itemnbt.putFloat(CritData.CRITCHANCE_FLAG, result.critChance());
            itemnbt.putFloat(CritData.CRITDAMAGE_FLAG, result.critDamage());
            itemnbt.putFloat(CritData.CRITCHANCE_QUALITY_FLAG, result.critChanceQuality());
            itemnbt.putFloat(CritData.CRITDAMAGE_QUALITY_FLAG, result.critDamageQuality());
            if (stack.getItem() instanceof ArmorItem)
                itemnbt.putFloat(CritData.HEALTH_FLAG, result.health());
            itemnbt.putString(CritData.TIER_FLAG, result.tier().getName());
        }));
    }
    public static <T> void applyCritData(ItemStack stack, T material, MaterialCritChanceProvider<T> critChanceProvider, CritTier tier) {
        CritData.CritResult result = calculateCritValues(material, critChanceProvider, tier);
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(itemnbt -> {
            itemnbt.putFloat(CritData.CRITCHANCE_FLAG, result.critChance());
            itemnbt.putFloat(CritData.CRITDAMAGE_FLAG, result.critDamage());
            itemnbt.putFloat(CritData.CRITCHANCE_QUALITY_FLAG, result.critChanceQuality());
            itemnbt.putFloat(CritData.CRITDAMAGE_QUALITY_FLAG, result.critDamageQuality());
            if (stack.isIn(ItemTags.ARMOR_ENCHANTABLE))
                itemnbt.putFloat(CritData.HEALTH_FLAG, result.health());
            itemnbt.putString(CritData.TIER_FLAG, result.tier().getName());
        }));
    }
}
