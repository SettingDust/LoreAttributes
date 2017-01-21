package com.settingdust.loreattr.command.util;

import com.settingdust.loreattr.util.LanguageUtils;
import org.bukkit.ChatColor;

/**
 * Author: SettingDust
 * Date: 16-8-27
 * By IntelliJ IDEA
 */
public class CommandUtils {
    public static String formatArgs(String[] args, int start) {
        String line = "";
        if (start < args.length)
            for (int i = start; i < args.length; i++) {
                line = ChatColor.translateAlternateColorCodes('&', args[i]) + " ";
            }
        return line;
    }

    public static String getCommandDescription(String name) {
        if (LanguageUtils.config.getConfig().contains(name + ".description"))
            return LanguageUtils.getString(name + ".description");
        else
            return null;
    }
}
