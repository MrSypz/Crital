package sypztep.crital.common.init;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.crital.common.CritalMod;

public class ModItem {
    public static Item GRINDER_TABLE;
    public static Item COPPERAL_WEAPON;
    public static Item COPPERAL_ARMOR;
    public static void init() {
        GRINDER_TABLE = registeritem("grinder_table",new BlockItem(ModBlockItem.GRINDER, new Item.Settings()));
        COPPERAL_WEAPON = registeritem("copperal_weapon", new Item(new Item.Settings().maxCount(99)));
        COPPERAL_ARMOR = registeritem("copperal_armor", new Item(new Item.Settings().maxCount(99)));
    }
    public static <T extends Item> T registeritem(String name, T item) {
        Registry.register(Registries.ITEM, CritalMod.id(name), item);
        return item;
    }
}
