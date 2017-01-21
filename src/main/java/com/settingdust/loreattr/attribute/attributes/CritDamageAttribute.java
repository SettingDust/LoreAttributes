package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;
import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class CritDamageAttribute extends Attribute {
    private Pattern critDamageRegex;

    public CritDamageAttribute() {
        super("critical-damage");
        this.critDamageRegex = Pattern.compile("[+](\\d+)[ ](" + LanguageUtils.getString("lore." + getName() + ".keyword").toLowerCase() + ")");
    }

    public Pattern getCritDamageRegex() {
        return critDamageRegex;
    }
}
