package com.settingdust.loreattr.command.loreattr;

import com.settingdust.loreattr.LoreAttributes;
import com.settingdust.loreattr.command.LoreAttrCommand;
import com.settingdust.loreattr.gui.rune.util.RuneUtils;
import com.settingdust.loreattr.util.LanguageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by SettingDust on 17-1-8.
 */
public class RuneCommand extends LoreAttrCommand {
    public RuneCommand(String[] labels, String[] args, String permission, boolean onlyPlayer) {
        super(labels, args, permission, onlyPlayer);
    }

    @Override
    public boolean excute(CommandSender sender, String[] args) {
        if (args[0].matches("\\d+")) {
            int page = Integer.parseInt(args[0]);
            if (page > LoreAttributes.plugin.getConfig().getInt("rune_limit")) {
                sender.sendMessage(LanguageUtils.getString("command.loreattr.rune.error.limit"));
            } else if (page < 1) {
                sender.sendMessage(LanguageUtils.getString("command.loreattr.rune.error.min"));
            } else if (page > 1) {
                if (!sender.hasPermission("loreattr.rune." + args[0]))
                    sender.sendMessage(LanguageUtils.getString("command.loreattr.rune.error.min"));
                else
                    RuneUtils.displayGui((Player) sender, page);
            } else {
                RuneUtils.displayGui((Player) sender, page);
            }
            return true;
        }
        return false;
    }
}
