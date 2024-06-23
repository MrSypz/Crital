package sypztep.crital.common.init;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import sypztep.crital.common.CritalMod;

public class ModTag {
    public static class Items {
        public static final TagKey<Item> WEAPON_GRINDER_MATERIAL = TagKey.of(Registries.ITEM.getKey(), CritalMod.id("weapon_grinder_material"));
        public static final TagKey<Item> ARMOR_GRINDER_MATERIAL = TagKey.of(Registries.ITEM.getKey(), CritalMod.id("armor_grinder_material"));
        public static final TagKey<Item> GRINDABLE_ITEM = TagKey.of(Registries.ITEM.getKey(), CritalMod.id("grindable_item"));
    }
}
