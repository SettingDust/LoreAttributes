package com.settingdust.loreattr.gui;

import com.settingdust.loreattr.gui.item.Condition;
import com.settingdust.loreattr.gui.item.Item;
import com.settingdust.loreattr.gui.item.Location;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SettingDust on 17-1-8.
 */
public class BaseGui {
    private String title = "null";
    private List<Item> items = new ArrayList<>();
    public Item air = new Item(null, Material.AIR);

    public BaseGui() {
        for (int i = 0; i < 6 * 9; i++) {
            items.add(air);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setItem(Location location, Material type, String display, Condition condition) {
        Item item = new Item(display, type);
        item.setCondition(condition);
        setItem(location, item);
    }

    public void setItem(Location location, Item item) {
        items.set(location.toSlot(), item);
    }

    public void setItem(Location location, Material type, String display, int id, Condition condition) {
        Item item = new Item(display, type);
        item.setId(id);
        item.setCondition(condition);
        setItem(location, item);
    }

    public void removeItem(Location location) {
        items.get(location.toSlot()).setDisplayName(null);
        items.get(location.toSlot()).setType(Material.AIR);
    }

    public Item getItem(Location location) {
        return items.get(location.toSlot());
    }

    public void paint() {

    }

    public void display(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 6 * 9, title);
        for (int i = 0; i < items.size(); i++) {
            ItemStack itemStack = items.get(i).getItemStack();
            itemStack.addUnsafeEnchantment(Enchantment.LUCK, 0);
            inventory.setItem(i, itemStack);
        }
        player.openInventory(inventory);
    }
}
