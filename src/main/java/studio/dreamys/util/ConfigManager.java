package studio.dreamys.util;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.io.File;

public class ConfigManager {
    public static boolean allowRidingFlyingMobs;

    public static void OpenSmashBatsConfig(FMLInitializationEvent event) {
        Configuration config = new Configuration(new File("config/opensmashbats.cfg"));
        config.load();

        allowRidingFlyingMobs = config.getBoolean("Allow riding flying mobs", "Main", false, "Set to true to allow riding flying mobs. Hostile mobs like Ghasts may not like this. Use at your own risk.");

        config.save();
    }
}