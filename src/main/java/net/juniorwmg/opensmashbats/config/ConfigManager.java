package net.juniorwmg.opensmashbats.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.io.File;

public class ConfigManager {
    public static boolean allowRidingFlyingMobs;
    public static Configuration config;

    public static void OpenSmashBatsConfig(FMLInitializationEvent event) {
        config = new Configuration(new File("config/opensmashbats.cfg"));
        config.load();

        allowRidingFlyingMobs = config.getBoolean("Allow riding flying mobs", "Main", false, "Set to true to allow riding flying mobs. Hostile mobs like Ghasts may not like this. Use at your own risk.");

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void syncConfig() {
        allowRidingFlyingMobs = config.getBoolean("Allow riding flying mobs", "Main", false, "Set to true to allow riding flying mobs. Hostile mobs like Ghasts may not like this. Use at your own risk.");

        if (config.hasChanged()) {
            config.save();
        }
    }
}