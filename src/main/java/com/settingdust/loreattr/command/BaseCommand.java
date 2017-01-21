package com.settingdust.loreattr.command;

import com.settingdust.loreattr.command.util.CommandUtils;
import org.bukkit.command.CommandSender;

/**
 * Author: SettingDust
 * Date: 16-8-7
 * By IntelliJ IDEA
 */
public class BaseCommand {
    private String[] args = null;
    private String description = null;
    private String permission = null;
    private boolean onlyPlayer = false;
    private String[] labels = null;
    public String name = null;

    public BaseCommand(String[] labels, String[] args, String permission, boolean onlyPlayer, String name) {
        this.args = args;
        this.onlyPlayer = onlyPlayer;
        this.labels = labels;
        this.name = name;
        this.description = CommandUtils.getCommandDescription(getPath());
        if (permission != null) {
            this.permission = permission;
        }
    }

    public String[] getArgs() {
        return args;
    }

    public String[] getName() {
        return labels;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }

    public boolean excute(CommandSender sender, String[] args) {
        return false;
    }

    public boolean isOnlyPlayer() {
        return onlyPlayer;
    }

    public String getPath() {
        String path = "command." + name;
        for (String s : getName()) {
            path = path + "." + s;
        }
        return path;
    }
}
