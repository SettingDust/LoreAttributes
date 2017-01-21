package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;
import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class HealthAttribute extends Attribute {
    private Pattern negRegex;
    private Pattern regex;

    public HealthAttribute() {
        super("health");

        this.regex = Pattern.compile("[+](\\d+)[ ](" + LanguageUtils.getString("lore." + getName() + ".keyword").toLowerCase() + ")");
        this.negRegex = Pattern.compile("[-](\\d+)[ ](" + LanguageUtils.getString("lore." + getName() + ".keyword").toLowerCase() + ")");
    }

    public Pattern getNegRegex() {
        return negRegex;
    }

    public Pattern getRegex() {
        return regex;
    }
}
