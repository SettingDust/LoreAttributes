package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;
import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class DamageAttribute extends Attribute {
    private Pattern damageValueRegex;
    private Pattern negitiveDamageValueRegex;
    private Pattern damageRangeRegex;

    public DamageAttribute() {
        super("damage");
        this.damageValueRegex = Pattern.compile("[+](\\d+)[ ](" + LanguageUtils.getString("lore." + getName() + ".keyword").toLowerCase() + ")");
        this.negitiveDamageValueRegex = Pattern.compile("[-](\\d+)[ ](" + LanguageUtils.getString("lore." + getName() + ".keyword").toLowerCase() + ")");
        this.damageRangeRegex = Pattern.compile("(\\d+)(-)(\\d+)[ ](" + LanguageUtils.getString("lore." + getName() + ".keyword").toLowerCase() + ")");
    }

    public Pattern getDamageRangeRegex() {
        return damageRangeRegex;
    }

    public Pattern getDamageValueRegex() {
        return damageValueRegex;
    }

    public Pattern getNegitiveDamageValueRegex() {
        return negitiveDamageValueRegex;
    }
}
