/**
 *
 */
package tsuteto.smashbat;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import tsuteto.smashbat.packet.PacketPipeline;
import tsuteto.smashbat.packet.PacketPlayerSmashing;

/**
 * The main class of Smash Bats Mod
 *
 * @author Tsuteto
 *
 */
@Mod(modid = SmashBatMod.modid, name = "Smash Bats Mod", version = "1.4.5_1-MC1.7.2")
public class SmashBatMod
{
    public static final String modid = "SmashBatsMod";
    public static final String resourceDomain = "smashbat:";

    public static PacketPipeline packetPipeline = new PacketPipeline();

    public static boolean use32xTexture = false;

    public static Item itemSmashBatWood;
    public static Item itemSmashBatIron;
    public static Item itemSmashBatGold;
    public static Item itemSmashBatDiamond;
    public static Item itemSmashBatCreeper;
    public static Item itemSmashBatTnt;
    public static Item itemSmashBatThunder;
    public static Item itemSmashBatRiding;

//    @SidedProxy(clientSide = "tsuteto.smashbat.SmashBatMod$ClientProxy", serverSide = "tsuteto.smashbat.SmashBatMod$ServerProxy")
//    public static ISidedProxy sidedProxy;

    @Mod.EventHandler
    public void preload(FMLPreInitializationEvent event)
    {
        Configuration conf = new Configuration(event.getSuggestedConfigurationFile());
        conf.load();

        use32xTexture = conf.get("general", "use32xTexture", use32xTexture, "If true, the bats use 32x resolution textures by ryomaco_514.").getBoolean(false);

        conf.save();

        /*
         * Item registration
         */
        itemSmashBatWood    = registerItem("smashBatWood", new ItemSmashBat(ItemSmashBat.EnumMaterial.WOOD));
        itemSmashBatIron    = registerItem("smashBatIron", new ItemSmashBat(ItemSmashBat.EnumMaterial.IRON));
        itemSmashBatGold    = registerItem("smashBatGold", new ItemSmashBat(ItemSmashBat.EnumMaterial.GOLD));
        itemSmashBatDiamond = registerItem("smashBatDiamond", new ItemSmashBat(ItemSmashBat.EnumMaterial.EMERALD));
        itemSmashBatCreeper = registerItem("smashBatCreeper", new ItemSmashBat(ItemSmashBat.EnumMaterial.CREEPER));
        itemSmashBatTnt     = registerItem("smashBatTnt", new ItemSmashBat(ItemSmashBat.EnumMaterial.BLAST));
        itemSmashBatThunder = registerItem("smashBatThunder", new ItemSmashBat(ItemSmashBat.EnumMaterial.THUNDER));
        itemSmashBatRiding  = registerItem("smashBatRiding", new ItemSmashBat(ItemSmashBat.EnumMaterial.RIDING));
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);

        packetPipeline.registerChannel("smashbat", PacketPlayerSmashing.class);

        GameRegistry.addShapedRecipe(new ItemStack(itemSmashBatWood),
                "X",
                "X",
                "Y",
                Character.valueOf('X'), Blocks.planks,
                Character.valueOf('Y'), Items.leather
        );
        GameRegistry.addShapedRecipe(new ItemStack(itemSmashBatIron),
                "X",
                "X",
                "Y",
                Character.valueOf('X'), Items.iron_ingot,
                Character.valueOf('Y'), Items.leather
        );
        GameRegistry.addShapedRecipe(new ItemStack(itemSmashBatGold),
                "X",
                "X",
                "Y",
                Character.valueOf('X'), Items.gold_ingot,
                Character.valueOf('Y'), Items.leather
        );
        GameRegistry.addShapedRecipe(new ItemStack(itemSmashBatDiamond),
                "X",
                "X",
                "Y",
                Character.valueOf('X'), Items.diamond,
                Character.valueOf('Y'), Items.leather
        );
        GameRegistry.addShapedRecipe(new ItemStack(itemSmashBatCreeper),
                "X|X",
                "X|X",
                " Y ",
                Character.valueOf('X'), Items.gunpowder,
                Character.valueOf('|'), Items.blaze_rod,
                Character.valueOf('Y'), Items.leather
        );
        GameRegistry.addShapedRecipe(new ItemStack(itemSmashBatTnt),
                "#",
                "#",
                "Y",
                Character.valueOf('#'), Blocks.tnt,
                Character.valueOf('Y'), Items.leather
        );
        GameRegistry.addShapedRecipe(new ItemStack(itemSmashBatThunder),
                "ΔXΔ",
                "ΔXΔ",
                " Y ",
                Character.valueOf('Δ'), Items.glowstone_dust,
                Character.valueOf('X'), Items.iron_ingot,
                Character.valueOf('Y'), Items.leather
        );
        GameRegistry.addShapedRecipe(new ItemStack(itemSmashBatRiding),
                "T",
                "T",
                "Y",
                Character.valueOf('T'), Items.carrot,
                Character.valueOf('Y'), Items.leather
        );
    }

    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event)
    {
        packetPipeline.postInitialize();
    }

    private Item registerItem(String name, Item item)
    {
        item.setUnlocalizedName(SmashBatMod.resourceDomain + name);
        item.setTextureName(SmashBatMod.resourceDomain + name);
        GameRegistry.registerItem(item, name, SmashBatMod.modid);
        return item;
    }

    public static void sendSmashToClient(EntityPlayerMP player, double vecX, double vecY, double vecZ)
    {
        packetPipeline.sendTo(new PacketPlayerSmashing(vecX, vecY, vecZ), player);
    }

    @SubscribeEvent
    public void onEntityLivingUpdate(LivingUpdateEvent event)
    {
        Entity entity = event.entity;
        ItemSmashBat.SmashEntry entrySmash = ItemSmashBat.smashEntryMap.get(entity.getEntityId());
        if (entrySmash != null && entity instanceof EntityPlayerMP)
        {
            entity.addVelocity(entrySmash.velocity.xCoord, entrySmash.velocity.yCoord, entrySmash.velocity.zCoord);
            sendSmashToClient((EntityPlayerMP)entity, entrySmash.velocity.xCoord, entrySmash.velocity.yCoord,
                    entrySmash.velocity.zCoord);
            ItemSmashBat.smashEntryMap.remove(entity.getEntityId());
        }

        if (!entity.worldObj.isRemote && entity.fallDistance > 0.5)
        {
            ItemSmashBat.SmashEntry entryImpact = ItemSmashBat.eventEntryMap.get(entity.getEntityId());
            if (entryImpact != null && entryImpact.material.delayedEvent != null)
            {
                entryImpact.material.delayedEvent.onEvent(entryImpact, entity);
                ItemSmashBat.eventEntryMap.remove(event.entity.getEntityId());
            }
        }
    }

    @SubscribeEvent
    public void onEntityLivingFall(LivingFallEvent event)
    {
        ItemSmashBat.eventEntryMap.remove(event.entity.getEntityId());
    }

    @SubscribeEvent
    public void onEntityLivingDead(LivingDeathEvent event)
    {
        ItemSmashBat.eventEntryMap.remove(event.entity.getEntityId());
        ItemSmashBat.smashEntryMap.remove(event.entity.getEntityId());
    }

//    public static class ClientProxy implements ISidedProxy
//    {
//    }
//
//    public static class ServerProxy implements ISidedProxy
//    {
//    }
//
//    public static interface ISidedProxy
//    {
//    }
}
