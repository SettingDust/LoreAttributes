package com.settingdust.loreattr.attribute.handler;

import com.settingdust.loreattr.LoreAttributes;
import com.settingdust.loreattr.attribute.AttributesManager;
import com.settingdust.loreattr.attribute.util.LoreUtils;
import com.settingdust.loreattr.util.LanguageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SettingDust on 17-1-8.
 */
public class LoreHandler implements Listener {
    LoreAttributes instance;

    public LoreHandler(LoreAttributes plugin) {
        instance = plugin;
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                List<Player> players = new ArrayList<Player>(Bukkit.getServer().getOnlinePlayers());
                for (Player player : players) {
                    Inventory inv = player.getInventory();
                    ItemStack[] items = inv.getContents();
                    for (int i = 0; i < items.length; i++) {
                        int regen = AttributesManager.getDuraRegen(items[i]);
                        if (regen != 0 && items[i].getDurability() > 0) {
                            items[i].setDurability((short) (items[i].getDurability() - regen));
                            inv.setItem(i, items[i]);
                        }
                    }
                    items = player.getEquipment().getArmorContents();
                    for (int i = 0; i < items.length; i++) {
                        int regen = AttributesManager.getDuraRegen(items[i]);
                        if (regen != 0 && items[i].getDurability() > 0) {
                            items[i].setDurability((short) (items[i].getDurability() - regen));
                        }
                    }
                }
            }
        }, 0L, 60L);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void applyHealthRegen(EntityRegainHealthEvent event) {
        AttributesManager.applyRegen(event);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void applyHealthJoin(PlayerJoinEvent event) {
        AttributesManager.applyHealth(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void applyHealthClose(InventoryCloseEvent event) {
        AttributesManager.applyHealth(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void modifyEntityDamage(EntityDamageByEntityEvent event) {
        if ((event.isCancelled())
                || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        double damage;
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (event.getDamager() instanceof LivingEntity) {
            LivingEntity damager = (LivingEntity) event.getDamager();
            if (LoreUtils.random(
                    AttributesManager.getDodgeBonus((LivingEntity) event.getEntity())
                            - AttributesManager.getRatingBonus(damager))) {
                event.setDamage(0.0D);
                event.setCancelled(true);
                return;
            }
            if ((damager instanceof Player)) {
                if (AttributesManager.canAttack(((Player)damager).getName())) {
                    AttributesManager.addAttackCooldown(((Player)damager).getName());
                } else {
                    if (!LanguageUtils.config.getConfig().getBoolean("lore.attack-speed.display-message")) {
                        event.setCancelled(true);
                        return;
                    }
                    ((Player)damager).sendMessage(LanguageUtils.getString("lore.attack-speed.message"));
                    event.setCancelled(true);
                    return;
                }

            }
            int damageBonus = AttributesManager.getDamageBonus(damager);
            int armor = AttributesManager.getPenetrationArmor(damager, AttributesManager.getArmorBonus(entity));
            int trueDamage = AttributesManager.getTrueDamage(damager);
            if (AttributesManager.useRangeOfDamage(damager))
                damage = Math.max(0.0D, damageBonus - armor);
            else
                damage = Math.max(0.0D, event.getDamage() + damageBonus - armor);
            event.setDamage(damage);
            entity.setHealth(Math.max(entity.getHealth() - trueDamage, 0));
            double steal = Math.min(damager.getMaxHealth(), damager.getHealth() + Math.min(AttributesManager.getLifeSteal(damager), event.getDamage()));
            if (steal >= 0 && steal <= damager.getMaxHealth())
                damager.setHealth(Math.min(damager.getMaxHealth(), damager.getHealth() + Math.min(AttributesManager.getLifeSteal(damager), event.getDamage())));
            if (AttributesManager.extraLighting(damager)) {
                entity.getWorld().strikeLightning(entity.getLocation());
            }
        } else if ((event.getDamager() instanceof Arrow)) {
            Arrow arrow = (Arrow) event.getDamager();
            if ((arrow.getShooter() != null) && ((arrow.getShooter() instanceof LivingEntity))) {
                LivingEntity damager = (LivingEntity) arrow.getShooter();
                if (LoreUtils.random(
                        AttributesManager.getDodgeBonus((LivingEntity) event.getEntity())
                                - AttributesManager.getRatingBonus(damager))) {
                    event.setDamage(0.0D);
                    event.setCancelled(true);
                    return;
                }
                if ((damager instanceof Player)) {
                    if (AttributesManager.canAttack(((Player)damager).getName())) {
                        AttributesManager.addAttackCooldown(((Player)damager).getName());
                    } else {
                        if (!LanguageUtils.config.getConfig().getBoolean("lore.attack-speed.display-message")) {
                            event.setCancelled(true);
                            return;
                        }
                        ((Player)damager).sendMessage(LanguageUtils.getString("lore.attack-speed.message"));
                        event.setCancelled(true);
                        return;
                    }
                }

                int damageBonus = AttributesManager.getDamageBonus(damager);
                int armor = AttributesManager.getPenetrationArmor(damager, AttributesManager.getArmorBonus(entity));
                int trueDamage = AttributesManager.getTrueDamage(damager);
                if (AttributesManager.useRangeOfDamage(damager))
                    damage = Math.max(0, damageBonus - armor);
                else {
                    damage = Math.max(0.0D, event.getDamage() + damageBonus - armor);
                }
                event.setDamage(damage);
                entity.setHealth(Math.max(entity.getHealth() - trueDamage, 0));
                double steal = Math.min(damager.getMaxHealth(), damager.getHealth() + Math.min(AttributesManager.getLifeSteal(damager), event.getDamage()));
                if (steal >= 0 && steal <= damager.getMaxHealth())
                    damager.setHealth(Math.min(damager.getMaxHealth(), damager.getHealth() + Math.min(AttributesManager.getLifeSteal(damager), event.getDamage())));
                if (AttributesManager.extraLighting(damager)) {
                    entity.getWorld().strikeLightning(entity.getLocation());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void checkBowRestriction(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!AttributesManager.canUse((Player) event.getEntity(), event.getBow()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void checkCraftRestriction(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        for (ItemStack item : event.getInventory().getContents())
            if (!AttributesManager.canUse((Player) event.getWhoClicked(), item)) {
                event.setCancelled(true);
                return;
            }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void checkWeaponRestriction(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!AttributesManager.canUse((Player) event.getDamager(), ((Player) event.getDamager()).getItemInHand())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void applyUnlimitDura(PlayerItemDamageEvent event) {
        if (AttributesManager.isUnbreakable(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void applyExp(PlayerExpChangeEvent event) {
        if (event.getAmount() > 0) {
            int exp = event.getAmount() + AttributesManager.getExp(event.getPlayer()) / 100 * event.getAmount();
            event.setAmount(exp);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void checkArmorRestriction(PlayerInteractEvent event) {
        final Player livingEntity = event.getPlayer();
        final ItemStack[] armors = livingEntity.getEquipment().getArmorContents();
        final ItemStack fitem = event.getItem();
        AttributesManager.applyHealth(livingEntity);
        Bukkit.getScheduler().runTaskLater(instance, new Runnable() {
            public void run() {
                ItemStack[] nowArmors = livingEntity.getEquipment().getArmorContents();
                for (int i = 0; i < armors.length; i++) {
                    if (nowArmors != null
                            && !nowArmors[i].getType().equals(Material.AIR)) {
                        if (!AttributesManager.canUse(livingEntity, fitem) &&
                                !LoreUtils.itemIsSimilar(armors[i], nowArmors[i])) {
                            nowArmors[i] = armors[i];
                            fitem.setAmount(1);
                            livingEntity.getInventory().addItem(fitem);
                            livingEntity.getEquipment().setArmorContents(nowArmors);
                        }
                        if (!AttributesManager.canUse(livingEntity, nowArmors[i])) {
                            livingEntity.getInventory().addItem(nowArmors[i]);
                            nowArmors[i] = null;
                            livingEntity.getEquipment().setArmorContents(nowArmors);
                        }
                    }
                }
            }
        }, 0L);
    }

    /*@GuiHandler(priority = EventPriority.NORMAL)
    public void applyHatHealth(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equalsIgnoreCase("/hat")) {
            Player player = event.getPlayer();
            ItemStack item = player.getItemInHand();
            if (AttributesManager.canUse(player, item)) {
                player.setMaxHealth(player.getMaxHealth()
                        - AttributesManager.getHealth(player.getEquipment().getHelmet()));
                player.setMaxHealth(player.getMaxHealth()
                        + AttributesManager.getHealth(item));
                player.setHealth(player.getMaxHealth());
            } else {
                event.setCancelled(true);
            }
        }
    }*/

    @EventHandler(priority = EventPriority.NORMAL)
    public void applyDuraBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            ItemStack item = player.getItemInHand();
            if (AttributesManager.hasDura(item)) {
                AttributesManager.addDura(item, -1);
                //player.sendMessage(LanguageUtils.getString("lore.dura.message").replace("&", "§"));
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void applyDuraEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) event.getDamager();
            ItemStack item = livingEntity.getEquipment().getItemInHand();
            if (AttributesManager.hasDura(item)) {
                item = AttributesManager.addDura(item, -1);
                livingEntity.getEquipment().setItemInHand(item);
                //livingEntity.sendMessage(LanguageUtils.getString("lore.dura.message").replace("&", "§"));
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void applyDuraDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            LivingEntity livingEntity = (LivingEntity) event.getEntity();
            ItemStack[] items = livingEntity.getEquipment().getArmorContents();
            for (int i = 0; i < items.length; i++) {
                if (AttributesManager.hasDura(items[i])) {
                    items[i] = AttributesManager.addDura(items[i], -1);
                    //livingEntity.sendMessage(LanguageUtils.getString("lore.dura.message").replace("&", "§"));
                }
            }
            livingEntity.getEquipment().setArmorContents(items);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void saveItemFromHighDamage(PlayerItemBreakEvent event) {
        ItemStack item = event.getBrokenItem();
        Player player = event.getPlayer();
        if (AttributesManager.hasDura(item)
                && AttributesManager.getDura(item) > 0) {
            ItemStack[] armors = player.getEquipment().getArmorContents();
            if (armors == null) return;
            for (int i = 0; i < armors.length; i++) {
                if (!(armors[i] == null || armors[i].getType().equals(Material.AIR))) {
                    if (armors[i].isSimilar(item) && armors[i].getAmount() == 0) {
                        item = AttributesManager.addDura(item, -AttributesManager.getDura(item));
                        item.setAmount(1);
                        armors[i] = item;
                        break;
                    }
                }
            }
            player.getEquipment().setArmorContents(armors);
            armors = player.getInventory().getContents();
            for (int i = 0; i < armors.length; i++) {
                if (!(armors[i] == null || armors[i].getType().equals(Material.AIR))) {
                    if (armors[i].isSimilar(item) && armors[i].getAmount() == 0) {
                        item = AttributesManager.addDura(item, -AttributesManager.getDura(item));
                        item.setAmount(1);
                        armors[i] = item;
                        break;
                    }
                }
            }
            player.getInventory().setContents(armors);
        }
    }
}
