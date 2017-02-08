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
        applyHealth(event.getPlayer(),
                event.getPlayer().getEquipment().getArmorContents(),
                event.getItem(), true);
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
            LivingEntity livingEntity = event.getWhoClicked();
            ItemStack item = event.getCursor();
            if (event.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
                if (!AttributesManager.canUse((Player) livingEntity, item)) {
                    event.setCancelled(true);
                } else if (AttributesManager.canUse((Player) livingEntity, event.getCurrentItem())) {
                    applyHealth(livingEntity, livingEntity.getEquipment().getArmorContents(), item, true);
                    item = event.getCurrentItem();
                    applyHealth(livingEntity, livingEntity.getEquipment().getArmorContents(), item, false);
                } else {
                    applyHealth(livingEntity, livingEntity.getEquipment().getArmorContents(), item, true);
                    item = event.getCurrentItem();
                    forceApplyHealth(livingEntity, livingEntity.getEquipment().getArmorContents(), item, false);
                }
            } else if (event.getClick().isShiftClick()) {
                item = event.getCurrentItem();
                applyHealth(livingEntity, livingEntity.getEquipment().getArmorContents(), item, true);
            }
            if (event.getClick().isKeyboardClick()
                    && event.getAction().equals(InventoryAction.HOTBAR_SWAP)) {
                item = event.getClickedInventory().getItem(event.getHotbarButton());
                if (!AttributesManager.canUse((Player) livingEntity, item)) {
                    event.setCancelled(true);
                } else {
                    applyHealth(livingEntity, livingEntity.getEquipment().getArmorContents(), item, true);
                }
            }
        }
    }


    public void applyHealth(final LivingEntity livingEntity, final ItemStack[] armors, final ItemStack item, final boolean plus) {
        Bukkit.getScheduler().runTaskLater(instance, new Runnable() {
            public void run() {
                ItemStack[] nowArmors = livingEntity.getEquipment().getArmorContents();
                for (int i = 0; i < armors.length; i++) {
                    if (!LoreUtils.itemIsSimilar(armors[i], nowArmors[i])) {
                        if (!AttributesManager.canUse((Player) livingEntity, item)) {
                            nowArmors[i] = null;
                            item.setAmount(1);
                            ((Player) livingEntity).getInventory().addItem(item);
                            livingEntity.getEquipment().setArmorContents(armors);
                        } else {
                            livingEntity.setMaxHealth(livingEntity.getMaxHealth()
                                    + (plus ? AttributesManager.getHealth(item) : -AttributesManager.getHealth(item)));
                        }
                    }
                }
            }
        }, 0L);
    }

    public void forceApplyHealth(final LivingEntity livingEntity, final ItemStack[] armors, final ItemStack item, final boolean plus) {
        Bukkit.getScheduler().runTaskLater(instance, new Runnable() {
            public void run() {
                ItemStack[] nowArmors = livingEntity.getEquipment().getArmorContents();
                for (int i = 0; i < armors.length; i++) {
                    if (!LoreUtils.itemIsSimilar(armors[i], nowArmors[i])) {
                        livingEntity.setMaxHealth(livingEntity.getMaxHealth()
                                + (plus ? AttributesManager.getHealth(item) : -AttributesManager.getHealth(item)));
                    }
                }
            }
        }, 0L);
    }
}
