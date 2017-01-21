package com.settingdust.loreattr.command;


/**
 * Created by SettingDust on 17-1-9.
 */
public class LoreAttrCommand extends BaseCommand {
    public LoreAttrCommand(String[] labels, String[] args, String permission, boolean onlyPlayer) {
        super(labels, args, permission, onlyPlayer, "loreattr");
    }
}
