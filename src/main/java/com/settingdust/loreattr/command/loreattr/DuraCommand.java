package com.settingdust.loreattr.command.loreattr;

import com.settingdust.loreattr.attribute.AttributesManager;
import com.settingdust.loreattr.command.LoreAttrCommand;
import com.settingdust.loreattr.util.LanguageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by SettingDust on 17-1-8.
 */
public class DuraCommand extends LoreAttrCommand {
    public DuraCommand(String[] labels, String[] args, String permission, boolean onlyPlayer) {
        super(labels, args, permission, onlyPlayer);
    }

    @Override
    public boolean excute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        if (AttributesManager.hasDura(p.getItemInHand())) {
            switch (args.length) {
                case 0:
                    p.sendMessage(String.valueOf(AttributesManager.getDura(p.getItemInHand())));
                    return true;
                case 1:
                    if (args[0].matches("\\d+")) {
                        AttributesManager.addDura(p.getItemInHand(), Integer.parseInt(args[0]));
                        p.sendMessage(LanguageUtils.getString(getPath() + ".message").replace("{amount}", args[0]));
                        return true;
                    }
                    break;
            }
        } else {
            p.sendMessage(LanguageUtils.getString("command.loreattr.dura.error.dura"));
            return true;
        }
        return false;
    }
}
