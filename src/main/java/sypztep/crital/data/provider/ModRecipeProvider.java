package sypztep.crital.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.init.ModItem;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItem.COPPERAL_WEAPON, 1)
                .pattern("#C")
                .pattern("C#")
                .input('#', Items.AMETHYST_SHARD)
                .input('C', Items.COPPER_INGOT)
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .offerTo(exporter, CritalMod.id(getRecipeName(ModItem.COPPERAL_WEAPON)));
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItem.COPPERAL_ARMOR, 1)
                .pattern("#0")
                .pattern("0#")
                .input('#', Items.AMETHYST_SHARD)
                .input('0', Items.ECHO_SHARD)
                .criterion(hasItem(Items.ECHO_SHARD), conditionsFromItem(Items.ECHO_SHARD))
                .offerTo(exporter, CritalMod.id(getRecipeName(ModItem.COPPERAL_ARMOR)));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModItem.GRINDER_TABLE)
                .pattern("@@")
                .pattern("##")
                .pattern("##")
                .input('#', ItemTags.PLANKS)
                .input('@', Items.COPPER_INGOT)
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .offerTo(exporter, CritalMod.id(getRecipeName(ModItem.GRINDER_TABLE)));
    }
}
