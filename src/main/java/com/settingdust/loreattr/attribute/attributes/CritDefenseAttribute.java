package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class CritDefenseAttribute extends Attribute {
    private Pattern criticalDefenseRegex;

    public CritDefenseAttribute() {
        super("critical-defense");
        this.criticalDefenseRegex = Pattern.compile("[+](\\d+)[%][ ](" + LanguageUtils.getString("lore.critical-defense.keyword").toLowerCase() + ")");
    }

    public Pattern getCriticalDefenseRegex() {
        return criticalDefenseRegex;
    }
}
