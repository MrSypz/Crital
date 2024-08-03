package sypztep.crital.common.data;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record CritalItemDataEntry(
float baseCritChance,
float baseCritDamage,
float minCritChanceMultiply,
float maxCritChanceMultiply,
float minCritDamageMultiply,
float maxCritDamageMultiply
) {
    public static final Map<RegistryEntry<Item>, CritalItemDataEntry> CRITAL_ITEM_ENTRY_MAP = new HashMap<>();

    public static Optional<CritalItemDataEntry> getCritalItemData(ItemStack stack) {
        RegistryEntry<Item> itemEntry = Registries.ITEM.getEntry(stack.getItem());
        return Optional.ofNullable(CRITAL_ITEM_ENTRY_MAP.get(itemEntry));
    }
    public static Optional<CritalItemDataEntry> getCritalItemData(String itemID) {
        Identifier itemIdentifier = Identifier.of(itemID);
        RegistryEntry<Item> itemEntry = Registries.ITEM.getEntry(itemIdentifier).orElse(null);
        return Optional.ofNullable(CRITAL_ITEM_ENTRY_MAP.get(itemEntry));
    }
    public static String getItemId(ItemStack stack) {
        return Registries.ITEM.getId(stack.getItem()).toString();
    }
}
