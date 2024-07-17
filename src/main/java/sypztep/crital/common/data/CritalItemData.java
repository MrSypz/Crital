package sypztep.crital.common.data;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

public record CritalItemData(
String itemId,
float baseCritChance,
float baseCritDamage,
float minCritChanceMultiply,
float maxCritChanceMultiply,
float minCritDamageMultiply,
float maxCritDamageMultiply
) {
    public static CritalItemData getCritalItemData(ItemStack stack) {
        String itemID = Registries.ITEM.getId(stack.getItem()).toString();
        return CritalItemDataSerializer.getConfigCache().get(itemID);
    }
    public static CritalItemData getCritalItemData(String itemID) {
        return CritalItemDataSerializer.getConfigCache().get(itemID);
    }
    public static String getItemId(ItemStack stack) {
        return Registries.ITEM.getId(stack.getItem()).toString();
    }
}
