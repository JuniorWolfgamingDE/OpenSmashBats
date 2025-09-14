package net.juniorwmg.opensmashbats.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ConfigGui extends GuiConfig {
    public ConfigGui(GuiScreen parent) {
        super(parent,
                new ConfigElement(ConfigManager.config.getCategory("Main")).getChildElements(),
                "opensmashbats",
                false,
                false,
                GuiConfig.getAbridgedConfigPath(ConfigManager.config.toString()));
    }
}