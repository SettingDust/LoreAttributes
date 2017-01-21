package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class ArmorAttribute extends Attribute {
    private Pattern armorRegex;

    public ArmorAttribute() {
        super("armor");
        this.armorRegex = Pattern.compile("[+](\\d+)[ ](" + LanguageUtils.getString("lore.armor.keyword").toLowerCase() + ")");
    }

    public Pattern getArmorRegex() {
        return armorRegex;
    }
}
