package sypztep.crital.common.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import sypztep.crital.common.api.crital.MaterialCritChanceProvider;
import sypztep.crital.common.api.crital.NewCriticalOverhaul;
import sypztep.crital.common.data.CritalData;
import sypztep.crital.common.data.CritTier;
import sypztep.tyrannus.common.util.ItemStackHelper;

import static sypztep.crital.common.data.CritalData.calculateCritValues;

public class CritalDataUtil {
    public static CritTier getCritTierFromStack(ItemStack stack) {
        if (ItemStackHelper.getNbtCompound(stack).contains(CritalData.TIER_FLAG)) {
            String critTierName = ItemStackHelper.getNbtCompound(stack).getString(CritalData.TIER_FLAG);
            return CritTier.fromName(critTierName);
        }
        return null;
    }
    public static <T> void applyCritData(ItemStack stack, T material, MaterialCritChanceProvider<T> critChanceProvider) {
        CritalData.CritResult result = calculateCritValues(material, critChanceProvider);
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(itemnbt -> {
            itemnbt.putFloat(CritalData.CRITCHANCE, result.critChance());
            itemnbt.putFloat(CritalData.CRITDAMAGE, result.critDamage());
            itemnbt.putFloat(CritalData.CRITCHANCE_QUALITY, result.critChanceQuality());
            itemnbt.putFloat(CritalData.CRITDAMAGE_QUALITY, result.critDamageQuality());
            if (stack.isIn(ItemTags.ARMOR_ENCHANTABLE))
                itemnbt.putFloat(CritalData.HEALTH_FLAG, result.health());
            itemnbt.putString(CritalData.TIER_FLAG, result.tier().getName());
        }));
    }
    public static <T> void applyCritData(ItemStack stack, T material, MaterialCritChanceProvider<T> critChanceProvider, CritTier tier) {
        CritalData.CritResult result = calculateCritValues(material, critChanceProvider, tier);
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(itemnbt -> {
            itemnbt.putFloat(CritalData.CRITCHANCE, result.critChance());
            itemnbt.putFloat(CritalData.CRITDAMAGE, result.critDamage());
            itemnbt.putFloat(CritalData.CRITCHANCE_QUALITY, result.critChanceQuality());
            itemnbt.putFloat(CritalData.CRITDAMAGE_QUALITY, result.critDamageQuality());
            if (stack.isIn(ItemTags.ARMOR_ENCHANTABLE))
                itemnbt.putFloat(CritalData.HEALTH_FLAG, result.health());
            itemnbt.putString(CritalData.TIER_FLAG, result.tier().getName());
        }));
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
}
