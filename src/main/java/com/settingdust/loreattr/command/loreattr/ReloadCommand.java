package com.settingdust.loreattr.command.loreattr;

import com.settingdust.loreattr.LoreAttributes;
import com.settingdust.loreattr.command.LoreAttrCommand;
import com.settingdust.loreattr.gui.rune.util.RuneUtils;
import com.settingdust.loreattr.util.LanguageUtils;
import org.bukkit.command.CommandSender;

/**
 * Created by SettingDust on 17-1-8.
 */
public class ReloadCommand extends LoreAttrCommand {
    public ReloadCommand(String[] name, String[] args, String permission, boolean onlyPlayer) {
        super(name, args, permission, onlyPlayer);
    }

    @Override
    public boolean excute(CommandSender sender, String[] args) {
        LoreAttributes.plugin.reloadConfig();
        LanguageUtils.config.reloadConfig();
        RuneUtils.config.reloadConfig();
        sender.sendMessage(LanguageUtils.getString(getPath() + ".message"));
        return true;
    }
}
