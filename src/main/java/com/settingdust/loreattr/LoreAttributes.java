package com.settingdust.loreattr;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.attribute.AttributesManager;
import com.settingdust.loreattr.attribute.handler.LoreHandler;
import com.settingdust.loreattr.command.Names;
import com.settingdust.loreattr.command.handler.CommandHandler;
import com.settingdust.loreattr.util.LanguageUtils;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by SettingDust on 17-1-8.
 */
public class LoreAttributes extends JavaPlugin {
    public static LoreAttributes plugin;

    @Override
    public void onLoad() {
        this.plugin = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        LanguageUtils.init();
        AttributesManager.init();

        ConfigurationSerialization.registerClass(Attribute.class);
    }

    @Override
    public void onEnable() {
        this.getCommand(Names.LOREATTR.getName()).setExecutor(new CommandHandler());
        this.getServer().getPluginManager().registerEvents(new LoreHandler(this), this);
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
        AttributesManager.disable();
    }
}
