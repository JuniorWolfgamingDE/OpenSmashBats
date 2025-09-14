package net.juniorwmg.opensmashbats;

import net.juniorwmg.opensmashbats.compat.FutureMCCompat;
import net.juniorwmg.opensmashbats.config.ConfigManager;
import net.juniorwmg.opensmashbats.init.SoundInit;
import net.juniorwmg.opensmashbats.proxy.CommonProxy;
import net.juniorwmg.opensmashbats.util.delayedEvents.DelayedEventManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.MODID, version = Main.VERSION, name = Main.MODNAME, guiFactory = "net.juniorwmg.opensmashbats.config.ConfigGuiFactory", dependencies = "after:futuremc")
public class Main {
    public static final String MODID = "opensmashbats";
    public static final String VERSION = "1.1.1";
    public static final String MODNAME = "OpenSmashBats";

    @Instance
    public static Main instance;

    @SidedProxy(clientSide = "net.juniorwmg.opensmashbats.proxy.ClientProxy", serverSide = "net.juniorwmg.opensmashbats.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("Pre-Initialization phase started!");
        ConfigManager.OpenSmashBatsConfig();
        SoundInit.registerSounds();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Initialization phase started!");
        boolean futureMCAllowed = ConfigManager.allowFutureMCCompat;
        System.out.println("Config ready!\nAllow riding flying mobs: " + ConfigManager.allowRidingFlyingMobs);

        // Future MC compatibility
        if (Loader.isModLoaded("futuremc") && futureMCAllowed) {
            FutureMCCompat.init();
        } else if (!futureMCAllowed) {
            System.out.println("Future MC compatibility is disabled in the mod configuration.");
        } else {
            System.out.println("Future MC is not installed - skipping compatibility.");
        }

        MinecraftForge.EVENT_BUS.register(DelayedEventManager.class);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        System.out.println("Post-Initialization phase started!");
    }
}