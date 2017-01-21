package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class CritChanceAttribute extends Attribute{
    private final Pattern critChanceRegex;

    public CritChanceAttribute() {
        super("critical-chance");
        this.critChanceRegex = Pattern.compile("[+](\\d+)[%][ ](" + LanguageUtils.getString("lore.critical-chance.keyword").toLowerCase() + ")");
    }

    public Pattern getCritChanceRegex() {
        return critChanceRegex;
    }
}
