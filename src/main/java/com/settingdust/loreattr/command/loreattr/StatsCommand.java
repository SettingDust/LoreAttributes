package com.settingdust.loreattr.command.loreattr;

import com.settingdust.loreattr.attribute.AttributesManager;
import com.settingdust.loreattr.command.LoreAttrCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by SettingDust on 17-1-8.
 */
public class StatsCommand extends LoreAttrCommand {
    public StatsCommand(String[] labels, String[] args, String permission, boolean onlyPlayer) {
        super(labels, args, permission, onlyPlayer);
    }

    @Override
    public boolean excute(CommandSender sender, String[] args) {
        AttributesManager.displayLoreStats((Player) sender);
        return true;
    }
}
