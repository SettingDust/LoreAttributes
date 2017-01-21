package com.settingdust.loreattr.attribute;

import com.settingdust.loreattr.LoreAttributes;
import com.settingdust.loreattr.attribute.attributes.*;
import com.settingdust.loreattr.attribute.util.LoreUtils;
import com.settingdust.loreattr.util.LanguageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;

/**
 * Created by SettingDust on 17-1-8.
 */
public class AttributesManager {
    private static Map<String, Attribute> attributes = new HashMap<>();

    private static Map<String, Timestamp> attackLog;
    private static boolean attackSpeedEnabled;

    /**
     * 初始化
     */
    public static void init() {
        attributes.put("health", new HealthAttribute());
        attributes.put("regen", new RegenAttribute());
        attributes.put("attack-speed", new AttackSpeedAttribute());
        attributes.put("damage", new DamageAttribute());
        attributes.put("dodge",new DodgeAttribute());
        attributes.put("critical-chance",new CritChanceAttribute());
        attributes.put("critical-damage",new CritDamageAttribute());
        attributes.put("life-steal",new LifeStealAttribute());
        attributes.put("armor",new ArmorAttribute());
        attributes.put("restriction",new RestrictionAttribute());
        attributes.put("level",new LevelAttribute());
        attributes.put("unbreakable",new UnbreakableAttribute());
        attributes.put("dura-regen",new DuraRegenAttribute());
        attributes.put("exp",new ExpAttribute());
        attributes.put("bound",new BoundAttribute());
        attributes.put("dura",new DuraAttribute());
        attributes.put("true-damage",new TrueDamageAttribute());
        attributes.put("penetration",new PenetrationAttribute());
        attributes.put("rating",new RatingAttribute());
        attributes.put("critical-defense",new CritDefenseAttribute());
        attributes.put("lighting",new LightingAttribute());
        attributes.put("accessory",new AccessoryAttribute());

        attackSpeedEnabled = false;

        if (LanguageUtils.config.getConfig().getBoolean("lore." + attributes.get("attack-speed").getName() + ".enabled")) {
            attackSpeedEnabled = true;
            attackLog = new HashMap<>();
        }
    }

    /**
     * @param name
     * @return 属性对象
     */
    public static Attribute getAttribute(String name) {
        return attributes.containsKey(name) ? attributes.get(name) : null;
    }

    /**
     * @param player
     * @param item
     * @return 玩家是否可以使用物品
     */
    public static boolean canUse(Player player, ItemStack item) {
        if ((item != null) &&
                (item.hasItemMeta()) &&
                (item.getItemMeta().hasLore()) &&
                !player.getGameMode().equals(GameMode.CREATIVE)) {
            List lore = item.getItemMeta().getLore();
            String allLore = lore.toString().toLowerCase();
            Matcher valueMatcher = ((LevelAttribute) attributes.get("level")).getLevelRegex().matcher(allLore);
            if (valueMatcher.find()) {
                if (player.getLevel() < Integer.valueOf(valueMatcher.group(1))) {
                    player.sendMessage(LanguageUtils.getString("lore.level.message"));
                    return false;
                }
            }
            valueMatcher = ((RestrictionAttribute) attributes.get("restriction")).getRestrictionRegex().matcher(allLore);
            if (valueMatcher.find()) {
                if (player.hasPermission("loreattr." + valueMatcher.group(2))) {
                    return true;
                }
                if (LanguageUtils.config.getConfig().getBoolean("lore.restriction.display-message")) {
                    player.sendMessage(LanguageUtils.getString("lore.restriction.message").replace("%itemname%", item.getType().toString()));
                }
                return false;
            }
            valueMatcher = ((BoundAttribute) attributes.get("bound")).getBoundRegex().matcher(allLore);
            if (valueMatcher.find()) {
                String name = getBound(item);
                if (!player.getName().equalsIgnoreCase(name)) {
                    player.sendMessage(LanguageUtils.getString("lore.bound.message").replace("{name}", name));
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 应用生命回复
     *
     * @param event
     */
    public static void applyRegen(EntityRegainHealthEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (((event.getEntity() instanceof Player)) &&
                (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED)) {

            LivingEntity entity = (LivingEntity) event.getEntity();
            if (!entity.isValid()) {
                return;
            }

            Integer regenBonus = 0;
            List<String> lore = LoreUtils.getLore(entity, true, true);
            for (String s : lore) {
                Matcher matcher = ((RegenAttribute) attributes.get("regen")).getRegenRegex().matcher(s.toLowerCase());
                if (matcher.find()) {
                    regenBonus = regenBonus + Integer.valueOf(matcher.group(1));
                }
            }

            if (entity instanceof Player)
                regenBonus += LoreUtils.getKeyValue(((Player)entity).getName(), "healthRegen");

            event.setAmount(event.getAmount() + regenBonus);

            if (event.getAmount() <= 0.0D)
                event.setCancelled(true);
        }
    }

    /**
     * 攻速监听取消
     */
    public static void disable() {
        attackSpeedEnabled = false;
        if (attackLog != null)
            attackLog.clear();
    }

    /**
     * @param player
     * @return 攻击冷却
     */
    private static double getAttackCooldown(Player player) {
        if (!attackSpeedEnabled) {
            return 0.0D;
        }
        return LanguageUtils.config.getConfig().getDouble("lore.attack-speed.base-delay") * 0.1D - getAttackSpeed(player) * 0.1D;
    }

    /**
     * 添加攻击冷却
     *
     * @param playerName
     */
    public static void addAttackCooldown(String playerName) {
        if (!attackSpeedEnabled) {
            return;
        }
        Timestamp able = new Timestamp((long) (new Date().getTime() + getAttackCooldown(Bukkit.getPlayer(playerName)) * 1000.0D));

        attackLog.put(playerName, able);
    }

    /**
     * @param playerName
     * @return 是否可以攻击
     */
    public static boolean canAttack(String playerName) {
        if (!attackSpeedEnabled) {
            return true;
        }
        if (!attackLog.containsKey(playerName)) {
            return true;
        }
        Date now = new Date();
        return now.after(attackLog.get(playerName));
    }

    /**
     * @param player
     * @return 攻击速度
     */
    private static double getAttackSpeed(Player player) {
        if (player == null) {
            return 1.0D;
        }

        double speed = 1.0D;
        List<String> lore = LoreUtils.getLore(player, true, true);
        for (String s : lore) {
            Matcher valueMatcher = ((AttackSpeedAttribute) attributes.get("attack-speed")).getAttackSpeedRegex().matcher(s.toLowerCase());
            if (valueMatcher.find()) {
                speed += Integer.valueOf(valueMatcher.group(1));
            }
        }
        speed += LoreUtils.getKeyValue(player.getName(), "attackSpeed");

        return speed;
    }

    /**
     * @param entity
     * @return
     */
    public static int getHpBonus(LivingEntity entity) {
        Integer hpToAdd = 0;
        List<String> lore = LoreUtils.getLore(entity, true, true);
        for (String s : lore) {
            Matcher matcher = ((HealthAttribute) attributes.get("health")).getRegex().matcher(s.toLowerCase());
            Matcher nematcher = ((HealthAttribute) attributes.get("health")).getNegRegex().matcher(s.toLowerCase());
            if (matcher.find())
                hpToAdd += Integer.valueOf(matcher.group(1));
            if (nematcher.find())
                hpToAdd -= Integer.valueOf(matcher.group(1));
        }
        if (entity instanceof Player)
            hpToAdd += LoreUtils.getKeyValue(((Player)entity).getName(), "health");
        return hpToAdd;
    }

    /**
     * @param entity
     * @return
     */
    public static int getRegenBonus(LivingEntity entity) {
        if (!entity.isValid()) {
            return 0;
        }
        Integer regenBonus = 0;
        List<String> lore = LoreUtils.getLore(entity, true, true);
        for (String s : lore) {
            Matcher matcher = ((RegenAttribute) attributes.get("regen")).getRegenRegex().matcher(s.toLowerCase());
            if (matcher.find()) {
                regenBonus = regenBonus + Integer.valueOf(matcher.group(1));
            }
        }
        if (entity instanceof Player)
            regenBonus += LoreUtils.getKeyValue(((Player)entity).getName(), "healthRegen");
        return regenBonus;
    }

    /**
     * @param entity
     */
    public static void applyHealth(LivingEntity entity) {
        entity.setMaxHealth(LoreAttributes.plugin.getConfig().getInt("base_health") + getHpBonus(entity));
    }

    /**
     * 获取
     *
     * @param entity
     * @return 伤害值
     */
    public static int getDamageBonus(LivingEntity entity) {
        if (!entity.isValid()) {
            return 0;
        }
        Integer damageMin = 0;
        Integer damageMax = 0;
        Integer damageBonus = 0;
        List<String> lore = LoreUtils.getLore(entity, false, false);
        for (String s : lore) {
            Matcher negValueMatcher = ((DamageAttribute) attributes.get("damage")).getDamageValueRegex().matcher(s.toLowerCase());
            Matcher rangeMatcher = ((DamageAttribute) attributes.get("damage")).getDamageRangeRegex().matcher(s.toLowerCase());
            Matcher valueMatcher = ((DamageAttribute) attributes.get("damage")).getDamageRangeRegex().matcher(s.toLowerCase());
            if (rangeMatcher.find()) {
                damageMin = Integer.valueOf(rangeMatcher.group(1));
                damageMax = Integer.valueOf(rangeMatcher.group(3));
            }
            if (valueMatcher.find()) {
                damageBonus += Integer.valueOf(valueMatcher.group(1));
                if (negValueMatcher.find()) {
                    damageBonus -= Integer.valueOf(negValueMatcher.group(1));
                }
            }
        }
        if (entity instanceof Player)
            damageBonus += LoreUtils.getKeyValue(((Player)entity).getName(), "damage");

        if (damageMax < 1) {
            damageMax = 1;
        }
        if (damageMin < 1) {
            damageMin = 1;
        }
        return (int) Math.round(Math.random() * (damageMax - damageMin) + damageMin + damageBonus + getCritDamage(entity));
    }

    /**
     * @param entity
     * @return 是否为范围伤害
     */
    public static boolean useRangeOfDamage(LivingEntity entity) {
        if (!entity.isValid()) {
            return false;
        }
        List<String> lore = LoreUtils.getLore(entity, true, true);
        for (String s : lore) {
            Matcher rangeMatcher = ((DamageAttribute) attributes.get("damage")).getDamageRangeRegex().matcher(s.toLowerCase());
            if (rangeMatcher.find()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param entity
     * @return 闪避几率
     */
    public static int getDodgeBonus(LivingEntity entity) {
        Integer dodgeBonus = 0;
        List<String> lore = LoreUtils.getLore(entity, true, true);
        for (String s : lore) {
            Matcher valueMatcher = ((DodgeAttribute) attributes.get("dodge")).getDodgeRegex().matcher(s.toLowerCase());
            if (valueMatcher.find()) {
                dodgeBonus += Integer.valueOf(valueMatcher.group(1));
            }
        }
        if (entity instanceof Player) {
            dodgeBonus += LoreUtils.getKeyValue(((Player)entity).getName(), "dodgeChance");
        }
        return dodgeBonus;
    }

    /**
     * @param entity
     * @return 暴击伤害
     */
    public static int getCritDamage(LivingEntity entity) {
        if (!LoreUtils.random(getCritChance(entity))) {
            return 0;
        }
        Integer damage = 0;
        List<String> lore = LoreUtils.getLore(entity, true, true);
        for (String s : lore) {
            Matcher valueMatcher = ((CritDamageAttribute) attributes.get("critical-damage")).getCritDamageRegex().matcher(s.toLowerCase());
            if (valueMatcher.find()) {
                damage += Integer.valueOf(valueMatcher.group(1));
            }
        }
        if (entity instanceof Player) {
            damage += LoreUtils.getKeyValue(((Player)entity).getName(), "critDamage");
        }

        double defense = getCritDefense(entity);
        if (defense > 0.0D && defense < 100.0D) {
            damage -= (int) (damage * defense);
        }

        return damage;
    }

    /**
     * @param entity
     * @return 暴击几率
     */
    private static int getCritChance(LivingEntity entity) {
        Integer chance = 0;
        List<String> lore = LoreUtils.getLore(entity, true, true);
        for (String s : lore) {
            Matcher valueMatcher = ((CritChanceAttribute) attributes.get("critical-chance")).getCritChanceRegex().matcher(s.toLowerCase());
            if (valueMatcher.find()) {
                chance += Integer.valueOf(valueMatcher.group(1));
            }
        }
        if (entity instanceof Player) {
            chance += LoreUtils.getKeyValue(((Player)entity).getName(), "critChance");
        }
        return chance;
    }

    /**
     * @param entity
     * @return 生命偷取
     */
    public static int getLifeSteal(LivingEntity entity) {
        Integer steal = 0;
        List<String> lore = LoreUtils.getLore(entity, true, true);
        for (String s : lore) {
            Matcher valueMatcher = ((LifeStealAttribute) attributes.get("life-steal")).getLifestealRegex().matcher(s.toLowerCase());
            if (valueMatcher.find()) {
                steal += Integer.valueOf(valueMatcher.group(1));
            }
        }
        return steal;
    }

    /**
     * @param entity
     * @return 护甲
     */
    public static int getArmorBonus(LivingEntity entity) {
        Integer armor = 0;
        List<String> lore = LoreUtils.getLore(entity, true, true);
        for (String s : lore) {
            Matcher valueMatcher = ((ArmorAttribute) attributes.get("armor")).getArmorRegex().matcher(s.toLowerCase());
            if (valueMatcher.find()) {
                armor += Integer.valueOf(valueMatcher.group(1));
            }
        }
        return armor;
    }

    /**
     * @param item
     * @return 是否无限耐久
     */
    public static boolean isUnbreakable(ItemStack item) {
        if (item != null
                && item.hasItemMeta()
                && !item.getType().equals(Material.AIR)
                && item.getItemMeta().hasLore()) {
            List lore = item.getItemMeta().getLore();
            String allLore = lore.toString().toLowerCase();
            Matcher matcher = ((UnbreakableAttribute) attributes.get("unbreakable")).getUnbreakableRegex().matcher(allLore);
            return (matcher.find());
        }
        return false;
    }

    /**
     * @param item
     * @return 耐久恢复
     */
    public static int getDuraRegen(ItemStack item) {
        int duraRegen = 0;
        if (item != null
                && item.hasItemMeta()
                && !item.getType().equals(Material.AIR)
                && item.getItemMeta().hasLore()) {
            List lore = item.getItemMeta().getLore();
            String allLore = lore.toString().toLowerCase();
            Matcher matcher = ((DuraRegenAttribute) attributes.get("dura-regen")).getDuraRegenRegex().matcher(allLore);
            if (matcher.find())
                duraRegen = Integer.valueOf(matcher.group(1));
        }
        return duraRegen;
    }

    /**
     * @param player
     * @return 经验加成
     */
    public static int getExp(Player player) {
        int exp = 0;
        List<String> lore = LoreUtils.getLore(player, true, true);
        for (String s : lore) {
            Matcher valueMatcher = ((ExpAttribute) attributes.get("exp")).getExpRegex().matcher(s.toLowerCase());
            if (valueMatcher.find()) {
                exp += Integer.valueOf(valueMatcher.group(1));
            }
        }
        return exp;
    }

    /**
     * @param item
     * @return 玩家名称
     */
    public static String getBound(ItemStack item) {
        String player = null;
        if (item != null
                && item.hasItemMeta()
                && !item.getType().equals(Material.AIR)
                && item.getItemMeta().hasLore()) {
            List lore = item.getItemMeta().getLore();
            String allLore = lore.toString().toLowerCase();
            Matcher matcher = ((BoundAttribute) attributes.get("bound")).getBoundRegex().matcher(allLore);
            if (matcher.find()) {
                player = matcher.group(2);
            }
        }
        return player;
    }

    /**
     * @param item
     * @return 是否拥有耐久
     */
    public static boolean hasDura(ItemStack item) {
        boolean dura = false;
        if (item != null
                && item.hasItemMeta()
                && !item.getType().equals(Material.AIR)
                && item.getItemMeta().hasLore()) {
            List lore = item.getItemMeta().getLore();
            String allLore = lore.toString();
            Matcher matcher = ((DuraAttribute) attributes.get("dura")).getDuraRegex().matcher(allLore);
            dura = matcher.find();
        }
        return dura;
    }

    /**
     * @param item
     * @return 耐久度
     */
    public static int getDura(ItemStack item) {
        int dura = 0;
        if (hasDura(item)
                && item != null
                && item.hasItemMeta()
                && !item.getType().equals(Material.AIR)
                && item.getItemMeta().hasLore()) {
            List lore = item.getItemMeta().getLore();
            String allLore = lore.toString();
            Matcher matcher = ((DuraAttribute) attributes.get("dura")).getDuraRegex().matcher(allLore);

            if (matcher.find())
                dura = Integer.parseInt(matcher.group(2));
        }
        return dura;
    }

    /**
     * @param item
     * @param dura
     * @return 物品
     */
    public static ItemStack addDura(ItemStack item, int dura) {
        if (item != null
                && item.hasItemMeta()
                && !item.getType().equals(Material.AIR)
                && item.getItemMeta().hasLore()) {
            List<String> lore = item.getItemMeta().getLore();
            for (int i = 0; i < lore.size(); i++) {
                Matcher matcher = ((DuraAttribute) attributes.get("dura")).getDuraRegex().matcher(lore.get(i));
                if (matcher.find()) {
                    if (Integer.parseInt(matcher.group(2)) + dura <= 0) {
                        lore.set(i, lore.get(i).replace(matcher.group(0),
                                matcher.group(1) + "0/" + Integer.parseInt(matcher.group(3))));
                    } else {
                        if (Integer.parseInt(matcher.group(2)) + dura >
                                Integer.parseInt(matcher.group(3))) {
                            lore.set(i, lore.get(i).replace(
                                    matcher.group(0),
                                    matcher.group(1) + Integer.parseInt(matcher.group(3)) + "/" + Integer.parseInt(matcher.group(3))));
                        } else {
                            lore.set(i, lore.get(i).replace(
                                    matcher.group(0),
                                    matcher.group(1) + (Integer.parseInt(matcher.group(2)) + dura) + "/" + Integer.parseInt(matcher.group(3))))
                            ;
                        }
                        ItemMeta itemMeta = item.getItemMeta();
                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                        item.setDurability((short) 0);
                    }
                }
            }
        }
        return item;
    }

    /**
     * @param damager
     * @return 真实伤害
     */
    public static int getTrueDamage(LivingEntity damager) {
        Integer damage = 0;
        List<String> lore = LoreUtils.getLore(damager, true, true);
        for (String s : lore) {
            Matcher valueMatcher = ((TrueDamageAttribute) attributes.get("true-damage")).getTrueDamageRegex().matcher(s.toLowerCase());
            if (valueMatcher.find()) {
                damage += Integer.valueOf(valueMatcher.group(1));
            }
        }
        return damage;
    }

    /**
     * @param entity
     * @param armor
     * @return 破甲之后的护甲值
     */
    public static int getPenetrationArmor(LivingEntity entity, int armor) {
        return (int) (armor - (armor * (getPenetration(entity) / getProtectEnchantment(entity)) / 100));
    }

    /**
     * @param entity
     * @return 破甲
     */
    public static int getPenetration(LivingEntity entity) {
        int penetration = 0;
        List<String> lore = LoreUtils.getLore(entity, true, true);
        for (String s : lore) {
            Matcher valueMatcher = ((PenetrationAttribute) attributes.get("penetration")).getPenetrationRegex().matcher(s.toLowerCase());
            if (valueMatcher.find()) {
                penetration += Integer.valueOf(valueMatcher.group(1));
            }
        }
        return penetration;
    }

    /**
     * @param entity
     * @return 保护等级
     */
    public static double getProtectEnchantment(LivingEntity entity) {
        double protect = 1;
        for (ItemStack item : entity.getEquipment().getArmorContents()) {
            if (item != null
                    && !item.getType().equals(Material.AIR)) {
                Map<Enchantment, Integer> enchantments = item.getEnchantments();
                for (Enchantment enchantment : enchantments.keySet()) {
                    if (enchantment.getId() == 0) {
                        int EPF = (int) Math.floor(
                                (6 + enchantments.get(enchantment) ^ 2) / 3);
                        protect -= protect + ((EPF > 20 ? 20 : EPF) * 0.04);
                    }
                }
            }
        }
        return protect >= 0 ? protect : 1;
    }

    /**
     * @param entity
     * @return 命中率
     */
    public static int getRatingBonus(LivingEntity entity) {
        Integer ratingBonus = 0;
        List<String> lore = LoreUtils.getLore(entity, true, true);
        for (String s : lore) {
            Matcher valueMatcher = ((RatingAttribute) attributes.get("rating")).getRatingRegex().matcher(s.toLowerCase());
            if (valueMatcher.find()) {
                ratingBonus += Integer.valueOf(valueMatcher.group(1));
            }
        }
        return ratingBonus;
    }

    /**
     * @param entity
     * @return 暴击防御
     */
    public static double getCritDefense(LivingEntity entity) {
        double defense = 0.0D;
        List<String> lore = LoreUtils.getLore(entity, true, true);
        for (String s : lore) {
            Matcher valueMatcher = ((CritDefenseAttribute) attributes.get("critical-defense")).getCriticalDefenseRegex().matcher(s.toLowerCase());
            if (valueMatcher.find()) {
                defense += Integer.valueOf(valueMatcher.group(1)) / 100D;
            }
        }
        return defense;
    }

    /**
     * @param entity
     * @return 是否闪电
     */
    public static boolean extraLighting(LivingEntity entity) {
        List<String> lore = LoreUtils.getLore(entity, true, true);
        for (String s : lore) {
            Matcher valueMatcher = ((LightingAttribute) attributes.get("lighting")).getExtraLightingRegex().matcher(s.toLowerCase());
            if (valueMatcher.find()) {
                if (LoreUtils.random(Integer.valueOf(valueMatcher.group(1))))
                    return true;
            }
        }
        return false;
    }

    public static void displayLoreStats(Player sender) {
        HashSet<String> message = new HashSet<String>();

        if (getHpBonus(sender) != 0)
            message.add(ChatColor.GRAY + LanguageUtils.getString("lore.health.keyword") + ": " + ChatColor.WHITE + getHpBonus(sender));
        if (getRegenBonus(sender) != 0)
            message.add(ChatColor.GRAY + LanguageUtils.getString("lore.regen.keyword") + ": " + ChatColor.WHITE + getRegenBonus(sender));
        if (LanguageUtils.config.getConfig().getBoolean("lore.attack-speed.enabled"))
            message.add(ChatColor.GRAY + LanguageUtils.getString("lore.attack-speed.keyword") + ": " + ChatColor.WHITE + getAttackSpeed(sender));
        if (getDamageBonus(sender) != 0)
            message.add(ChatColor.GRAY + LanguageUtils.getString("lore.damage.keyword") + ": " + ChatColor.WHITE + getDamageBonus(sender));
        if (getDodgeBonus(sender) != 0)
            message.add(ChatColor.GRAY + LanguageUtils.getString("lore.dodge.keyword") + ": " + ChatColor.WHITE + getDodgeBonus(sender) + "%");
        if (getCritChance(sender) != 0)
            message.add(ChatColor.GRAY + LanguageUtils.getString("lore.critical-chance.keyword") + ": " + ChatColor.WHITE + getCritChance(sender) + "%");
        if (getCritDamage(sender) != 0)
            message.add(ChatColor.GRAY + LanguageUtils.getString("lore.critical-damage.keyword") + ": " + ChatColor.WHITE + getCritDamage(sender));
        if (getLifeSteal(sender) != 0)
            message.add(ChatColor.GRAY + LanguageUtils.getString("lore.life-steal.keyword") + ": " + ChatColor.WHITE + getLifeSteal(sender));
        if (getArmorBonus(sender) != 0)
            message.add(ChatColor.GRAY + LanguageUtils.getString("lore.armor.keyword") + ": " + ChatColor.WHITE + getArmorBonus(sender));
        if (getCritDefense(sender) != 0)
            message.add(ChatColor.GRAY + LanguageUtils.getString("lore.critical-defense.keyword") + ": " + ChatColor.WHITE + getCritDefense(sender));
        if (getRatingBonus(sender) != 0)
            message.add(ChatColor.GRAY + LanguageUtils.getString("lore.rating.keyword") + ": " + ChatColor.WHITE + getRatingBonus(sender));
        if (getTrueDamage(sender) != 0)
            message.add(ChatColor.GRAY + LanguageUtils.getString("lore.true-damage.keyword") + ": " + ChatColor.WHITE + getTrueDamage(sender));
        if (getPenetration(sender) != 0)
            message.add(ChatColor.GRAY + LanguageUtils.getString("lore.penetration.keyword") + ": " + ChatColor.WHITE + getPenetration(sender));
        String newMessage = "";
        for (String toSend : message) {
            newMessage = newMessage + "     " + toSend;
            if (newMessage.length() > 40) {
                sender.sendMessage(newMessage);
                newMessage = "";
            }
        }
        if (newMessage.length() > 0) {
            sender.sendMessage(newMessage);
        }
        message.clear();
    }
}
