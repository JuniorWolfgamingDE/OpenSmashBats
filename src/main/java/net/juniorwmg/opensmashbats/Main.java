package net.juniorwmg.opensmashbats;

import net.juniorwmg.opensmashbats.config.ConfigManager;
import net.juniorwmg.opensmashbats.init.SoundInit;
import net.juniorwmg.opensmashbats.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.juniorwmg.opensmashbats.util.delayedEvents.DelayedEventManager;

@Mod(modid = Main.MODID, version = Main.VERSION, name = Main.MODNAME, guiFactory = "net.juniorwmg.opensmashbats.config.ConfigGuiFactory")
public class Main {
    public static final String MODID = "opensmashbats";
    public static final String VERSION = "1.0";
    public static final String MODNAME = "OpenSmashBats";

    @Instance
    public static Main instance;

    @SidedProxy(clientSide = "net.juniorwmg.opensmashbats.proxy.ClientProxy", serverSide = "net.juniorwmg.proxy.opensmashbats.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("Pre-Initialization phase started!");
        SoundInit.registerSounds();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Initialization phase started!");
        ConfigManager.OpenSmashBatsConfig(event);
        System.out.println("Config ready!\nAllow riding flying mobs: " + ConfigManager.allowRidingFlyingMobs);

        MinecraftForge.EVENT_BUS.register(DelayedEventManager.class);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        System.out.println("Post-Initialization phase started!");
    }
}