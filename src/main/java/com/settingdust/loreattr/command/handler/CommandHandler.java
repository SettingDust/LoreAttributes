package com.settingdust.loreattr.command.handler;

import com.settingdust.loreattr.command.BaseCommand;
import com.settingdust.loreattr.command.Names;
import com.settingdust.loreattr.command.loreattr.*;
import com.settingdust.loreattr.util.LanguageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: SettingDust
 * Date: 16-8-7
 * By IntelliJ IDEA
 */
public class CommandHandler implements CommandExecutor {
    private static List<BaseCommand> commands = new ArrayList<>();

    public CommandHandler() {
        commands.add(new StatsCommand(new String[]{"stats"}, new String[0], "loreattr.use", true));
        commands.add(new ReloadCommand(new String[]{"reload"}, new String[0], "loreattr.admin", false));
        commands.add(new SaveCommand(new String[]{"save"}, new String[0], "loreattr.admin", false));
        for (int i = 0; i < commands.size(); i++) {
            for (int i1 = 0; i1 < commands.get(i).getArgs().length; i1++) {
                if (commands.get(i).getArgs()[i1].matches("\\[.*?\\]")) {
                    commands.get(i).getArgs()[i1] =
                            "[" + LanguageUtils.getString(
                                    commands.get(i).getPath() + ".args." + (commands.get(i).getArgs()[i1].replace("[", "").replace("]", "")))
                                    + "]";
                }
                if (commands.get(i).getArgs()[i1].matches("<.*?>")) {
                    commands.get(i).getArgs()[i1] =
                            "<" + LanguageUtils.getString(
                                    commands.get(i).getPath() + ".args." + (commands.get(i).getArgs()[i1].replace("<", "").replace(">", "")))
                                    + ">";
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        for (Names main : Names.values()) {
            if (main.getName().toString().equalsIgnoreCase(cmd.getName())) {
                if (args.length == 0) {
                    List<String> infoFormat = LanguageUtils.config.getConfig().getStringList("help");
                    List<String> commandsInfo = new ArrayList<>();
                    for (String line : infoFormat) {
                        line = line.replace("&", "§").replace("{author}", "DC Stuidio");
                        if (line.contains("{command}")
                                || line.contains("{description}")) {
                            for (BaseCommand command : commands) {
                                if (command.getDescription() != null) {
                                    String commandLine = "/" + main.getName();
                                    for (String name : command.getName()) {
                                        commandLine = commandLine + " " + name;
                                    }
                                    for (String arg : command.getArgs()) {
                                        commandLine = commandLine + " " + arg;
                                    }
                                    commandLine = line.replace("{command}", commandLine).replace("{permission}", command.getPermission());
                                    commandsInfo.add(commandLine.replace("{description}", command.getDescription()));
                                }
                            }
                        } else {
                            commandsInfo.add(line);
                        }
                    }
                    sender.sendMessage(commandsInfo.toArray(new String[commandsInfo.size()]));
                    return true;
                }

                for (BaseCommand command : commands) {
                    boolean excute = true;
                    if (command.getName().length <= args.length) {
                        for (int i = 0; i < command.getName().length; i++) {
                            if (!command.getName()[i].equalsIgnoreCase(args[i])) {
                                excute = false;
                                break;
                            }
                        }
                    }
                    if (excute) {
                        if (command.isOnlyPlayer()
                                && sender instanceof ConsoleCommandSender) {
                            sender.sendMessage(LanguageUtils.getString("command.error.console"));
                        } else if (command.getPermission() != null
                                && !sender.hasPermission(command.getPermission())) {
                            sender.sendMessage(LanguageUtils.getString("command.error.permission"));
                        } else {
                            List<String> newArgs = new ArrayList<>();
                            int must = 0;
                            for (int i = command.getName().length; i < args.length; i++) {
                                if (i >= 0) {
                                    newArgs.add(args[i]);
                                }
                            }
                            for (String arg : command.getArgs()) {
                                if (arg.matches("\\[.*?\\]")) must++;
                            }
                            if (newArgs.size() < must) {
                                sender.sendMessage(LanguageUtils.getString("command.error.args"));
                                return false;
                            }
                            if (!command.excute(sender, newArgs.toArray(new String[newArgs.size()]))) {
                                sender.sendMessage(LanguageUtils.getString("command.error.args"));
                                return false;
                            }
                        }
                        return true;
                    }
                }
                sender.sendMessage(LanguageUtils.getString("command.error.args"));
            }
        }
        return false;
    }
}
