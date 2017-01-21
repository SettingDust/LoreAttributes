package com.settingdust.loreattr.gui.rune;

import com.settingdust.loreattr.gui.BaseGui;
import com.settingdust.loreattr.gui.item.Condition;
import com.settingdust.loreattr.gui.item.Item;
import com.settingdust.loreattr.gui.item.Location;
import com.settingdust.loreattr.gui.rune.util.RuneUtils;
import com.settingdust.loreattr.util.LanguageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SettingDust on 17-1-8.
 */
public class RuneGui extends BaseGui {
    private List<Item> runes = new ArrayList<>();

    public RuneGui(int page, Player player) {
        this.setTitle(LanguageUtils.getString("gui.rune.title").replace("{page}", "" + page));
        this.runes = RuneUtils.getRunes(player, page);
        this.paint();
    }

    @Override
    public void paint() {
        /**
         * 绘制边界
         */
        for (int i = 0; i < 6; i++) {
            this.setItem(new Location(0, i),
                    Material.getMaterial(LanguageUtils.getString("gui.rune.border.type")),
                    LanguageUtils.getString("gui.rune.border.display"),
                    LanguageUtils.config.getConfig().getInt("gui.rune.border.id"),
                    new Condition(false, false));
            this.setItem(new Location(1, i),
                    Material.getMaterial(LanguageUtils.getString("gui.rune.border.type")),
                    LanguageUtils.getString("gui.rune.border.display"),
                    LanguageUtils.config.getConfig().getInt("gui.rune.border.id"),
                    new Condition(false, false));
            this.setItem(new Location(7, i),
                    Material.getMaterial(LanguageUtils.getString("gui.rune.border.type")),
                    LanguageUtils.getString("gui.rune.border.display"),
                    LanguageUtils.config.getConfig().getInt("gui.rune.border.id"),
                    new Condition(false, false));
            this.setItem(new Location(8, i),
                    Material.getMaterial(LanguageUtils.getString("gui.rune.border.type")),
                    LanguageUtils.getString("gui.rune.border.display"),
                    LanguageUtils.config.getConfig().getInt("gui.rune.border.id"),
                    new Condition(false, false));
        }

        this.setItem(new Location(2, 0),
                Material.getMaterial(LanguageUtils.getString("gui.rune.corner.type")),
                LanguageUtils.getString("gui.rune.corner.display"),
                LanguageUtils.config.getConfig().getInt("gui.rune.corner.id"),
                new Condition(false, false));
        this.setItem(new Location(6, 0),
                Material.getMaterial(LanguageUtils.getString("gui.rune.corner.type")),
                LanguageUtils.getString("gui.rune.corner.display"),
                LanguageUtils.config.getConfig().getInt("gui.rune.corner.id"),
                new Condition(false, false));
        this.setItem(new Location(2, 5),
                Material.getMaterial(LanguageUtils.getString("gui.rune.corner.type")),
                LanguageUtils.getString("gui.rune.corner.display"),
                LanguageUtils.config.getConfig().getInt("gui.rune.corner.id"),
                new Condition(false, false));
        this.setItem(new Location(6, 5),
                Material.getMaterial(LanguageUtils.getString("gui.rune.corner.type")),
                LanguageUtils.getString("gui.rune.corner.display"),
                LanguageUtils.config.getConfig().getInt("gui.rune.corner.id"),
                new Condition(false, false));

        this.setItem(new Location(4, 0),
                Material.getMaterial(LanguageUtils.getString("gui.rune.mid.type")),
                LanguageUtils.getString("gui.rune.mid.display"),
                LanguageUtils.config.getConfig().getInt("gui.rune.mid.id"),
                new Condition(false, false));
        this.setItem(new Location(4, 5),
                Material.getMaterial(LanguageUtils.getString("gui.rune.mid.type")),
                LanguageUtils.getString("gui.rune.mid.display"),
                LanguageUtils.config.getConfig().getInt("gui.rune.mid.id"),
                new Condition(false, false));

        this.setItem(new Location(3, 0),
                Material.getMaterial(LanguageUtils.getString("gui.rune.line.type")),
                LanguageUtils.getString("gui.rune.line.display"),
                LanguageUtils.config.getConfig().getInt("gui.rune.line.id"),
                new Condition(false, false));
        this.setItem(new Location(3, 5),
                Material.getMaterial(LanguageUtils.getString("gui.rune.line.type")),
                LanguageUtils.getString("gui.rune.line.display"),
                LanguageUtils.config.getConfig().getInt("gui.rune.line.id"),
                new Condition(false, false));
        this.setItem(new Location(5, 0),
                Material.getMaterial(LanguageUtils.getString("gui.rune.line.type")),
                LanguageUtils.getString("gui.rune.line.display"),
                LanguageUtils.config.getConfig().getInt("gui.rune.line.id"),
                new Condition(false, false));
        this.setItem(new Location(5, 5),
                Material.getMaterial(LanguageUtils.getString("gui.rune.line.type")),
                LanguageUtils.getString("gui.rune.line.display"),
                LanguageUtils.config.getConfig().getInt("gui.rune.line.id"),
                new Condition(false, false));
        for (int i = 1; i < 5; i++) {
            this.setItem(new Location(2, i),
                    Material.getMaterial(LanguageUtils.getString("gui.rune.line.type")),
                    LanguageUtils.getString("gui.rune.line.display"),
                    LanguageUtils.config.getConfig().getInt("gui.rune.line.id"),
                    new Condition(false, false));
            this.setItem(new Location(6, i),
                    Material.getMaterial(LanguageUtils.getString("gui.rune.line.type")),
                    LanguageUtils.getString("gui.rune.line.display"),
                    LanguageUtils.config.getConfig().getInt("gui.rune.line.id"),
                    new Condition(false, false));
        }
        /**
         * 绘制符文
         */
        for (int i = 0; i < runes.size(); i++) {
            Item rune = runes.get(i);
            this.setItem(new Location(i % 3 + 3, (int) Math.floor(i / 3) + 1), rune);
        }
    }
}
