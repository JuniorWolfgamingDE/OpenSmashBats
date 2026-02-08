package net.juniorwmg.opensmashbats.compat;

import mellohi138.netherized.init.NetherizedItems;
import net.juniorwmg.opensmashbats.Main;
import net.juniorwmg.opensmashbats.init.ItemInit;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NetherizedCompat {

    public static void init() {
        addNetheriteSmashBatRecipe();
        System.out.println("Netherized (Kedition) compatibility enabled.");
    }

    private static void addNetheriteSmashBatRecipe() {
        GameRegistry.addShapelessRecipe(
                new ResourceLocation(Main.MODID, "netherite_smash_bat_netherized_compat"),
                null,
                new ItemStack(ItemInit.NETHERITE_SMASH_BAT),
                Ingredient.fromStacks(new ItemStack(ItemInit.DIAMOND_SMASH_BAT)),
                Ingredient.fromStacks(new ItemStack(NetherizedItems.NETHERITE_INGOT))
        );
    }
}