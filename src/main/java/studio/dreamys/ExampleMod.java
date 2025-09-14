package studio.dreamys;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import studio.dreamys.init.SoundInit;
import studio.dreamys.proxy.CommonProxy;
import studio.dreamys.config.ConfigManager;
import studio.dreamys.util.delayedEvents.DelayedEventManager;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION, name = ExampleMod.MODNAME, guiFactory = "studio.dreamys.config.ConfigGuiFactory")
public class ExampleMod {
    public static final String MODID = "opensmashbats";
    public static final String VERSION = "1.0";
    public static final String MODNAME = "OpenSmashBats";

    @Instance
    public static ExampleMod instance;

    @SidedProxy(clientSide = "studio.dreamys.proxy.ClientProxy", serverSide = "studio.dreamys.proxy.CommonProxy")
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