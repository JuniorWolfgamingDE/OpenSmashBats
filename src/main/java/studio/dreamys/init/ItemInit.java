package studio.dreamys.init;

import net.minecraft.item.Item;
import studio.dreamys.objects.tool.EnumMaterial;
import studio.dreamys.objects.tool.SmashBatTool;

import java.util.ArrayList;
import java.util.List;

public class ItemInit {
    public static final List<Item> ITEMS = new ArrayList<Item>();

    // Standard type smash bats
    public static final Item WOODEN_SMASH_BAT = new SmashBatTool("wooden_smash_bat", EnumMaterial.WOOD, null);
    public static final Item IRON_SMASH_BAT = new SmashBatTool("iron_smash_bat", EnumMaterial.IRON, null);
    public static final Item GOLDEN_SMASH_BAT = new SmashBatTool("golden_smash_bat", EnumMaterial.GOLD, null);
    public static final Item DIAMOND_SMASH_BAT = new SmashBatTool("diamond_smash_bat", EnumMaterial.DIAMOND, null);

    // Special type smash bats
    public static final Item BLAST_SMASH_BAT = new SmashBatTool("blast_smash_bat", EnumMaterial.BLAST, "blast");
    public static final Item CREEPER_SMASH_BAT = new SmashBatTool("creeper_smash_bat", EnumMaterial.WOOD, "creeper");
    public static final Item RIDING_SMASH_BAT = new SmashBatTool("riding_smash_bat", EnumMaterial.RIDING, "riding");
    public static final Item THUNDER_SMASH_BAT = new SmashBatTool("thunder_smash_bat", EnumMaterial.WOOD, "thunder");
}