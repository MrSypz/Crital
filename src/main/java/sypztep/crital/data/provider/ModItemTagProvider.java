package sypztep.crital.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import sypztep.crital.common.init.ModItem;
import sypztep.crital.common.init.ModTag;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ModTag.Items.WEAPON_GRINDER_MATERIAL)
                .add(ModItem.COPPERAL_WEAPON);
        getOrCreateTagBuilder(ModTag.Items.ARMOR_GRINDER_MATERIAL)
                .add(ModItem.COPPERAL_ARMOR);
        getOrCreateTagBuilder(ModTag.Items.GRINDABLE_ITEM)
                .addOptionalTag(ConventionalItemTags.ARMORS)
                .addOptionalTag(ConventionalItemTags.MELEE_WEAPON_TOOLS)
                .addOptionalTag(ConventionalItemTags.RANGED_WEAPON_TOOLS)
                .addOptionalTag(ItemTags.PICKAXES)
                .addOptionalTag(ItemTags.SHOVELS)
                .addOptionalTag(ItemTags.HOES)
                .addOptionalTag(ItemTags.AXES);
    }
}
