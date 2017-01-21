package com.settingdust.loreattr.gui.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SettingDust on 17-1-8.
 */
public class Item implements ConfigurationSerializable {
    private int id = 0;
    private Material type = Material.AIR;
    private List<String> lore = new ArrayList<>();
    private String displayName = "null";
    private Condition condition = new Condition(true, true);

    public Item(String displayName, Material type) {
        this.displayName = displayName;
        this.type = type;
    }

    public Item(ItemStack itemStack, Condition condition) {
        this.type = itemStack.getType();
        this.displayName = itemStack.hasItemMeta() ? itemStack.getItemMeta().getDisplayName() : displayName;
        this.id = itemStack.getDurability();
        this.condition = condition;
        this.lore = itemStack.hasItemMeta() ? itemStack.getItemMeta().getLore() : lore;
    }

    public Item(ItemStack itemStack) {
        this.type = itemStack.getType();
        this.displayName = itemStack.hasItemMeta() ? itemStack.getItemMeta().getDisplayName() : displayName;
        this.id = itemStack.getDurability();
        this.lore = itemStack.hasItemMeta() ? itemStack.getItemMeta().getLore() : lore;
    }

    public Item(String displayName, int id, Material type, Condition condition) {
        this.displayName = displayName;
        this.type = type;
        this.id = id;
        this.condition = condition;
    }

    public boolean hasLore() {
        return lore != null;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getId() {
        return id;
    }

    public Material getType() {
        return type;
    }

    public void setType(Material type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public List<String> getLore() {
        return lore;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("lore", lore);
        map.put("id", id);
        map.put("type", type);
        map.put("display", displayName);
        map.put("condition", condition);
        return map;
    }

    public static Item deserialize(Map<String, Object> map) {
        Item item = new Item(String.valueOf(map.get("display")),
                (int) map.get("id"),
                (Material) map.get("type"),
                (Condition) map.get("condition"));
        item.setLore((List<String>) map.get("lore"));
        return item;
    }

    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(type);
        itemStack.setDurability((short) id);
        ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(type);
        if (itemMeta != null) {
            if (displayName != null)
                itemMeta.setDisplayName(displayName);
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }
}
