package com.settingdust.loreattr.gui.rune.handler;

import com.settingdust.loreattr.gui.item.Location;
import com.settingdust.loreattr.gui.rune.util.RuneUtils;
import com.settingdust.loreattr.util.LanguageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by SettingDust on 17-1-15.
 */
public class GuiHandler implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (RuneUtils.isRuneGui(event.getInventory()) && event.getSlot() >= 0) {
            if (!(isValid(event.getCurrentItem()) && RuneUtils.isRune(event.getCurrentItem()))) {
                if (isValid(event.getCurrentItem())
                        && event.getCurrentItem().getType().equals(Material.getMaterial(LanguageUtils.getString("gui.rune.slot.type")))
                        && event.getCurrentItem().getDurability() == LanguageUtils.config.getConfig().getInt("gui.rune.slot.id")) {
                    if ((!event.getCurrentItem().hasItemMeta()
                            || !event.getCurrentItem().getItemMeta().hasDisplayName())
                            || event.getCurrentItem().hasItemMeta()
                            && event.getCurrentItem().getItemMeta().hasDisplayName()
                            && event.getCurrentItem().getItemMeta().getDisplayName().equals(LanguageUtils.getString("gui.rune.slot.display"))) {
                        if (isValid(event.getCursor()) && RuneUtils.isRune(event.getCursor())) {
                            event.setCurrentItem(new ItemStack(Material.AIR));
                        } else {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                } else if (!(isValid(event.getCursor()) && RuneUtils.isRune(event.getCursor()))
                        || isValid(event.getCurrentItem())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (RuneUtils.isRuneGui(event.getInventory())) {
            RuneUtils.saveRunes(event.getInventory(), (Player) event.getPlayer());
        }
    }

    private boolean isValid(ItemStack itemStack) {
        return itemStack != null && !itemStack.getType().equals(Material.AIR);
    }
}
