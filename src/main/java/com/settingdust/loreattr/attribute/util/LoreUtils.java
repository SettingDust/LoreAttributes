package com.settingdust.loreattr.attribute.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by SettingDust on 17-1-8.
 */
public class LoreUtils {

    public static boolean random(int chance) {
        return (int) (Math.random() * 100) < chance;
    }

    public static List<String> getLore(LivingEntity entity, boolean armor) {
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

        return lore;
    }

    public static boolean itemIsSimilar(ItemStack item1, ItemStack item2) {
        boolean similar = false;
        if (item1 != null && item2 != null) {
            similar = item1.getType().equals(item2.getType())
                    && item1.getDurability() == item2.getDurability();
            if (item1.hasItemMeta() && item2.hasItemMeta())
                similar = similar && item1.getItemMeta().equals(item2.getItemMeta());

        }
        return similar;
    }
}
