package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;
import java.util.regex.Pattern;


/**
 * Created by SettingDust on 17-1-8.
 */
public class RegenAttribute extends Attribute {
    private final Pattern regenRegex;

    public RegenAttribute() {
        super("regen");
        this.regenRegex = Pattern.compile("[+](\\d+)[ ](" + LanguageUtils.getString("lore." + getName() + ".keyword").toLowerCase() + ")");
    }

    public Pattern getRegenRegex() {
        return regenRegex;
    }
}
