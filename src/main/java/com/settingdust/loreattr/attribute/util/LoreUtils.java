package com.settingdust.loreattr.attribute.util;

import com.settingdust.levelpoints.LevelPoints;
import com.settingdust.loreattr.LoreAttributes;
import com.settingdust.loreattr.attribute.AttributesManager;
import com.settingdust.loreattr.attribute.attributes.AccessoryAttribute;
import com.settingdust.loreattr.gui.rune.util.RuneUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;


/**
 * Created by SettingDust on 17-1-8.
 */
public class LoreUtils {

    public static Integer getKeyValue(String name, String key) {
        double value = 0;
        if (Bukkit.getPluginManager().isPluginEnabled("LevelPotions")) {
            for (String type : LevelPoints.pointsUtils.getAttributes().keySet()) {
                for (String s : LevelPoints.pointsUtils.getAttributes().get(type).attributes.keySet()) {
                    if (s.equalsIgnoreCase(key)) {
                        value = value + LevelPoints.pointsUtils.getAttribute(name, type, key);
                    }
                }
            }
        }
        return (int) value;
    }

    public static boolean random(int chance) {
        return (int) (Math.random() * 100) < chance;
    }

    public static List<String> getLore(LivingEntity entity, boolean armor, boolean accessory) {
        List lore = new ArrayList();
        if (armor) {
            for (ItemStack item : entity.getEquipment().getArmorContents()) {
                if ((item != null) &&
                        (item.hasItemMeta()) &&
                        (item.getItemMeta().hasLore())) {
                    for (String s : item.getItemMeta().getLore()) {
                        lore.add(ChatColor.stripColor(s));
                    }
                }
            }
        }
        ItemStack item;
        item = entity.getEquipment().getItemInHand();
        if ((item != null) &&
                (item.hasItemMeta()) &&
                (item.getItemMeta().hasLore())) {
            for (String s : item.getItemMeta().getLore()) {
                lore.add(ChatColor.stripColor(s));
            }
        }
        if (entity instanceof HumanEntity && accessory) {
            HumanEntity humanEntity = (HumanEntity) entity;
            item = humanEntity.getInventory().getItem(LoreAttributes.plugin.getConfig().getInt("accessory", 35));
            if ((item != null) &&
                    (item.hasItemMeta()) &&
                    (item.getItemMeta().hasLore())) {
                List<String> itemLore = item.getItemMeta().getLore();
                Matcher matcher =
                        ((AccessoryAttribute) AttributesManager.getAttribute("accessory")).getAccessoryRegex().matcher(itemLore.toString());
                if (matcher.find())
                    for (String s : itemLore) {
                        lore.add(ChatColor.stripColor(s));
                    }
            }
        }
        if (entity instanceof HumanEntity) {
            for (String s : RuneUtils.getState((Player) entity)) {
                lore.add(ChatColor.stripColor(s));
            }
        }

        return lore;
    }

    public static boolean itemIsSimilar(ItemStack item1, ItemStack item2) {
        boolean similar = false;
        if (item1 != null
                && !item1.getType().equals(Material.AIR)
                && item2 != null
                && !item2.getType().equals(Material.AIR)) {
            similar = item1.getDurability() == item2.getDurability()
                    && item1.getType().equals(item2.getType());
            if (item1.hasItemMeta() && item2.hasItemMeta())
                similar = similar && item1.getItemMeta().equals(item2.getItemMeta());

        }
        return similar;
    }
}
