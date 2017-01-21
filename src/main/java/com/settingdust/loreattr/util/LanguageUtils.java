package com.settingdust.loreattr.util;

import com.settingdust.loreattr.LoreAttributes;
import com.settingdust.loreattr.config.Config;
import org.bukkit.ChatColor;

import java.io.File;

/**
 * Author: SettingDust
 * Date: 16-8-7
 * By IntelliJ IDEA
 */
public class LanguageUtils {
    public static Config config;

    public static void init() {
        config = new Config("language" + File.separator
                + LoreAttributes.plugin.getConfig().getString("language") + ".yml",
                LoreAttributes.plugin);
    }


    public static String getString(String path) {
        if (config.getConfig().contains(path))
            return ChatColor.translateAlternateColorCodes('&', config.getConfig().getString(path));
        else
            return "";
    }
}
