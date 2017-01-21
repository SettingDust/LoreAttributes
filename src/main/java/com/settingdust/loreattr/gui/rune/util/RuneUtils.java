package com.settingdust.loreattr.gui.rune.util;

import com.settingdust.loreattr.LoreAttributes;
import com.settingdust.loreattr.config.Config;
import com.settingdust.loreattr.gui.item.Condition;
import com.settingdust.loreattr.gui.item.Item;
import com.settingdust.loreattr.gui.item.Location;
import com.settingdust.loreattr.gui.rune.RuneGui;
import com.settingdust.loreattr.util.LanguageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-15.
 */
public class RuneUtils {
    public static Config config;
    public static ItemStack slot = new ItemStack(Material.getMaterial(LanguageUtils.getString("gui.rune.slot.type")));

    public static void init() {
        config = new Config("datas.yml", LoreAttributes.plugin, false);

        ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(slot.getType());
        itemMeta.setDisplayName(LanguageUtils.getString("gui.rune.slot.display"));
        slot.setItemMeta(itemMeta);
    }

    public static void save() {
        config.saveConfig();
    }

    public static List<Item> getRunes(Player player, int page) {
        List<Item> runes = new ArrayList<>();
        Item redBlock = new Item(slot, new Condition(false, true));
        if (config.getConfig().contains(player.getName() + "." + page + ".runes")) {
            for (Object o : config.getConfig().getList(player.getName() + "." + page + ".runes")) {
                runes.add(o == null ? redBlock : new Item((ItemStack) o));
            }
        }
        while (runes.size() < 12) {
            runes.add(redBlock);
        }
        while (runes.size() > 12) {
            runes.remove(redBlock);
        }
        while (runes.size() > 12) {
            runes.remove(runes.size() - 1);
        }
        return runes;
    }

    public static List<ItemStack> saveRunes(Inventory inventory, Player player) {
        List<ItemStack> runes = new ArrayList<>();
        if (isRuneGui(inventory)) {
            for (int i = 1; i < 5; i++) {
                runes.add(inventory.getItem(new Location(3, i).toSlot()));
                runes.add(inventory.getItem(new Location(4, i).toSlot()));
                runes.add(inventory.getItem(new Location(5, i).toSlot()));
            }
            while (runes.size() < 12) {
                runes.add(slot);
            }
            while (runes.size() > 12) {
                runes.remove(slot);
            }
            while (runes.size() > 12) {
                runes.remove(runes.size() - 1);
            }
            config.getConfig().set(player.getName() + "." + getPage(inventory) + ".runes", runes);
        }
        return runes;
    }

    public static void displayGui(Player player, int page) {
        RuneGui gui = new RuneGui(page, player);
        gui.display(player);
    }

    public static boolean isRuneGui(Inventory inventory) {
        Pattern regex = Pattern.compile("(" + LanguageUtils.getString("gui.rune.title").replace("{page}", ")(\\d+)"));
        Matcher matcher = regex.matcher(inventory.getTitle());
        return matcher.find();
    }

    public static int getPage(Inventory inventory) {
        Pattern regex = Pattern.compile(LanguageUtils.getString("gui.rune.title").replace("{page}", "(\\d+)"));
        Matcher matcher = regex.matcher(inventory.getTitle());
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
    }

    public static List<String> getState(Player player, int page) {
        List<Item> items = getRunes(player, page);
        List<String> lore = new ArrayList<>();
        for (Item item : items) {
            if (item.hasLore())
                lore.addAll(item.getLore());
        }
        return lore;
    }

    public static boolean isRune(ItemStack itemStack) {
        boolean is = false;
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            Pattern regex = Pattern.compile(LanguageUtils.getString("gui.rune.rune").replace("{type}","(\\w*)"));
            Matcher matcher = regex.matcher(ChatColor.stripColor(itemMeta.getDisplayName()));
            is = matcher.find();
        }
        return is;
    }

    public static List<String> getState(Player player) {
        List<String> lore = new ArrayList<>();
        for (int i = 1; i < LoreAttributes.plugin.getConfig().getInt("rune_limit") + 1; i++) {
            lore.addAll(getState(player, i));
        }
        return lore;
    }
}
