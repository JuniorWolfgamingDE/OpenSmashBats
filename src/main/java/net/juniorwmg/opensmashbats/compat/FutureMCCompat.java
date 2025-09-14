package net.juniorwmg.opensmashbats.compat;

import net.juniorwmg.opensmashbats.init.ItemInit;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipe;
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipes;
import thedarkcolour.futuremc.registry.FItems;

public class FutureMCCompat {

    public static void init() {
        addSmithingRecipes();
        System.out.println("Future MC compatibility enabled.");
    }

    private static void addSmithingRecipes() {
        addNetheriteSmashBatRecipe();
    }

    private static void addNetheriteSmashBatRecipe() {
        Ingredient input = Ingredient.fromStacks(new ItemStack(ItemInit.DIAMOND_SMASH_BAT, 1, 32767));
        Ingredient material = Ingredient.fromItem(FItems.INSTANCE.getNETHERITE_INGOT());
        ItemStack output = new ItemStack(ItemInit.NETHERITE_SMASH_BAT);

        SmithingRecipe customRecipe = new SmithingRecipe(input, material, output);
        SmithingRecipes.INSTANCE.getRecipes().add(customRecipe);
    }
}