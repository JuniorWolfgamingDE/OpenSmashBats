package net.juniorwmg.opensmashbats.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.io.File;

public class ConfigManager {
    public static boolean allowRidingFlyingMobs;
    public static boolean allowFutureMCCompat;
    public static Configuration config;

    // TODO: improve this class! Duplicated code.

    public static void OpenSmashBatsConfig(FMLInitializationEvent event) {
        config = new Configuration(new File("config/opensmashbats.cfg"));
        config.load();

        allowRidingFlyingMobs = config.getBoolean("Allow riding flying mobs", "Main", false, "Set to true to allow riding flying mobs. Hostile mobs like Ghasts may not like this. Use at your own risk.");
        allowFutureMCCompat = config.getBoolean("Allow Future MC compat", "Main", true, "Set to false to disable Future MC compatibility. This removes the ability to craft the Netherite Smash Bat if Future MC is loaded. Does nothing if Future MC is not loaded.");
        config.getCategory("Main").get("Allow Future MC compat").setRequiresMcRestart(true);

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void syncConfig() {
        allowRidingFlyingMobs = config.getBoolean("Allow riding flying mobs", "Main", false, "Set to true to allow riding flying mobs. Hostile mobs like Ghasts may not like this. Use at your own risk.");
        allowFutureMCCompat = config.getBoolean("Allow Future MC compat", "Main", true, "Set to false to disable Future MC compatibility. This removes the ability to craft the Netherite Smash Bat if Future MC is loaded. Does nothing if Future MC is not loaded.");
        config.getCategory("Main").get("Allow Future MC compat").setRequiresMcRestart(true);

        if (config.hasChanged()) {
            config.save();
        }
    }
}