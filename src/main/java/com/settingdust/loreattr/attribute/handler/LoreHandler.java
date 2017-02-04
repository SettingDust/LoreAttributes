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
import org.bukkit.event.inventory.*;
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
                    AttributesManager.getDodgeBonus((LivingEntity) event.getEntity()))) {
                event.setDamage(0.0D);
                event.setCancelled(true);
                return;
            }
            if ((damager instanceof Player)) {
                if (AttributesManager.canAttack(((Player) damager).getName())) {
                    AttributesManager.addAttackCooldown(((Player) damager).getName());
                } else {
                    if (!LanguageUtils.config.getConfig().getBoolean("lore.attack-speed.display-message")) {
                        event.setCancelled(true);
                        return;
                    }
                    ((Player) damager).sendMessage(LanguageUtils.getString("lore.attack-speed.message"));
                    event.setCancelled(true);
                    return;
                }

            }
            int damageBonus = AttributesManager.getDamageBonus(damager);
            int armor = AttributesManager.getArmorBonus(entity);
            if (AttributesManager.useRangeOfDamage(damager))
                damage = Math.max(0.0D, damageBonus - armor);
            else
                damage = Math.max(0.0D, event.getDamage() + damageBonus - armor);
            event.setDamage(damage);
            entity.setHealth(Math.max(entity.getHealth(), 0));
            double steal = Math.min(damager.getMaxHealth(), damager.getHealth() + Math.min(AttributesManager.getLifeSteal(damager), event.getDamage()));
            if (steal >= 0 && steal <= damager.getMaxHealth())
                damager.setHealth(Math.min(damager.getMaxHealth(), damager.getHealth() + Math.min(AttributesManager.getLifeSteal(damager), event.getDamage())));
        } else if ((event.getDamager() instanceof Arrow)) {
            Arrow arrow = (Arrow) event.getDamager();
            if ((arrow.getShooter() != null) && ((arrow.getShooter() instanceof LivingEntity))) {
                LivingEntity damager = (LivingEntity) arrow.getShooter();
                if (LoreUtils.random(
                        AttributesManager.getDodgeBonus((LivingEntity) event.getEntity()))) {
                    event.setDamage(0.0D);
                    event.setCancelled(true);
                    return;
                }
                if ((damager instanceof Player)) {
                    if (AttributesManager.canAttack(((Player) damager).getName())) {
                        AttributesManager.addAttackCooldown(((Player) damager).getName());
                    } else {
                        if (!LanguageUtils.config.getConfig().getBoolean("lore.attack-speed.display-message")) {
                            event.setCancelled(true);
                            return;
                        }
                        ((Player) damager).sendMessage(LanguageUtils.getString("lore.attack-speed.message"));
                        event.setCancelled(true);
                        return;
                    }
                }

                int damageBonus = AttributesManager.getDamageBonus(damager);
                int armor = AttributesManager.getArmorBonus(entity);
                if (AttributesManager.useRangeOfDamage(damager))
                    damage = Math.max(0, damageBonus - armor);
                else {
                    damage = Math.max(0.0D, event.getDamage() + damageBonus - armor);
                }
                event.setDamage(damage);
                entity.setHealth(Math.max(entity.getHealth(), 0));
                double steal = Math.min(damager.getMaxHealth(), damager.getHealth() + Math.min(AttributesManager.getLifeSteal(damager), event.getDamage()));
                if (steal >= 0 && steal <= damager.getMaxHealth())
                    damager.setHealth(Math.min(damager.getMaxHealth(), damager.getHealth() + Math.min(AttributesManager.getLifeSteal(damager), event.getDamage())));
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

    @EventHandler(priority = EventPriority.NORMAL)
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
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void applyHealth(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && !event.isCancelled()) {
            final LivingEntity livingEntity = event.getWhoClicked();
            ItemStack item = event.getCursor();
            if (event.getSlotType().equals(InventoryType.SlotType.ARMOR) && !event.getClick().equals(ClickType.DOUBLE_CLICK)) {
                if (!AttributesManager.canUse((Player) livingEntity, item)) {
                    event.setCancelled(true);
                } else {
                    event.getWhoClicked().setMaxHealth(livingEntity.getMaxHealth()
                            + AttributesManager.getHealth(item));
                    event.getWhoClicked().setHealth(livingEntity.getMaxHealth());
                }

                item = event.getCurrentItem();
                if (AttributesManager.canUse((Player) livingEntity, item)) {
                    event.getWhoClicked().setMaxHealth(livingEntity.getMaxHealth()
                            - AttributesManager.getHealth(item));
                    event.getWhoClicked().setHealth(livingEntity.getMaxHealth());
                }
                item = event.getCurrentItem();
                if (!AttributesManager.canUse((Player) livingEntity, item)) {
                    event.setCancelled(true);
                }
            } else if (event.getClick().equals(ClickType.SHIFT_LEFT)
                    || event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                item = event.getCurrentItem();
                final ItemStack[] armors = livingEntity.getEquipment().getArmorContents();
                final ItemStack fitem = item;
                Bukkit.getScheduler().runTaskLater(instance, new Runnable() {
                    public void run() {
                        ItemStack[] nowArmors = livingEntity.getEquipment().getArmorContents();
                        for (int i = 0; i < armors.length; i++) {
                            if (nowArmors != null
                                    && !nowArmors[i].getType().equals(Material.AIR)
                                    && !LoreUtils.itemIsSimilar(armors[i], nowArmors[i])) {
                                if (!AttributesManager.canUse((Player) livingEntity, fitem)) {
                                    nowArmors[i] = null;
                                    fitem.setAmount(1);
                                    ((Player) livingEntity).getInventory().addItem(fitem);
                                    livingEntity.getEquipment().setArmorContents(armors);
                                } else {
                                    livingEntity.setMaxHealth(livingEntity.getMaxHealth()
                                            + AttributesManager.getHealth(fitem));
                                    livingEntity.setHealth(livingEntity.getMaxHealth());
                                }
                            }
                        }
                    }
                }, 0L);
            }
        }
    }
}
