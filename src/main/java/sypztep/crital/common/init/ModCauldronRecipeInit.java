package sypztep.crital.common.init;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.yunitrish.adaptor.api.CauldronRecipeProvider;
import net.yunitrish.adaptor.api.CauldronRecipeRegistry;
import net.yunitrish.adaptor.recipe.CauldronRecipe;


public class ModCauldronRecipeInit implements CauldronRecipeProvider {
    @Override
    public void addCauldronRecipes() {
        addConcreteRecipe("white_concrete", Items.WHITE_CONCRETE_POWDER, Items.WHITE_CONCRETE);
        addConcreteRecipe("orange_concrete", Items.ORANGE_CONCRETE_POWDER, Items.ORANGE_CONCRETE);
        addConcreteRecipe("magenta_concrete", Items.MAGENTA_CONCRETE_POWDER, Items.MAGENTA_CONCRETE);
        addConcreteRecipe("light_blue_concrete", Items.LIGHT_BLUE_CONCRETE_POWDER, Items.LIGHT_BLUE_CONCRETE);
        addConcreteRecipe("yellow_concrete", Items.YELLOW_CONCRETE_POWDER, Items.YELLOW_CONCRETE);
        addConcreteRecipe("lime_concrete", Items.LIME_CONCRETE_POWDER, Items.LIME_CONCRETE);
        addConcreteRecipe("pink_concrete", Items.PINK_CONCRETE_POWDER, Items.PINK_CONCRETE);
        addConcreteRecipe("gray_concrete", Items.GRAY_CONCRETE_POWDER, Items.GRAY_CONCRETE);
        addConcreteRecipe("light_gray_concrete", Items.LIGHT_GRAY_CONCRETE_POWDER, Items.LIGHT_GRAY_CONCRETE);
        addConcreteRecipe("cyan_concrete", Items.CYAN_CONCRETE_POWDER, Items.CYAN_CONCRETE);
        addConcreteRecipe("purple_concrete", Items.PURPLE_CONCRETE_POWDER, Items.PURPLE_CONCRETE);
        addConcreteRecipe("blue_concrete", Items.BLUE_CONCRETE_POWDER, Items.BLUE_CONCRETE);
        addConcreteRecipe("brown_concrete", Items.BROWN_CONCRETE_POWDER, Items.BROWN_CONCRETE);
        addConcreteRecipe("green_concrete", Items.GREEN_CONCRETE_POWDER, Items.GREEN_CONCRETE);
        addConcreteRecipe("red_concrete", Items.RED_CONCRETE_POWDER, Items.RED_CONCRETE);
        addConcreteRecipe("black_concrete", Items.BLACK_CONCRETE_POWDER, Items.BLACK_CONCRETE);
    }
    private static void addConcreteRecipe(String recipeName, Item concretePowder, Item concrete) {
        CauldronRecipe concreteRecipe = new CauldronRecipe(CauldronRecipeRegistry.DeviceType.BOILED, recipeName)
                .setRecipeItem(concretePowder.getDefaultStack())
                .setResultItem(concrete.getDefaultStack());
        CauldronRecipeRegistry.registerRecipe(concreteRecipe);
    }
}
