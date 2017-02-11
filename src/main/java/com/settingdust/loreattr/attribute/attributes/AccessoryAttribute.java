package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class AccessoryAttribute extends Attribute {
    private Pattern accessoryRegex;

    public AccessoryAttribute() {
        super("accessory");
        this.accessoryRegex = Pattern.compile(LanguageUtils.getString("lore." + getName() + ".keyword").toLowerCase());
    }

    public Pattern getAccessoryRegex() {
        return accessoryRegex;
    }
}
