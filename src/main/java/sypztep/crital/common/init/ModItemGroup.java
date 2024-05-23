package sypztep.crital.common.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;


public class ModItemGroup {
    public static void init(){

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.addAfter(Items.NETHERITE_INGOT, ModItem.COPPERAL_WEAPON);
            content.addAfter(ModItem.COPPERAL_WEAPON, ModItem.COPPERAL_ARMOR);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
            content.addAfter(Items.BLAST_FURNACE, ModItem.GRINDER_TABLE);
        });
    }
}
