package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class LifeStealAttribute extends Attribute {
    private Pattern lifestealRegex;

    public LifeStealAttribute() {
        super("life-steal");
        this.lifestealRegex = Pattern.compile("[+](\\d+)[ ](" + LanguageUtils.getString("lore.life-steal.keyword").toLowerCase() + ")");
    }

    public Pattern getLifestealRegex() {
        return lifestealRegex;
    }
}
