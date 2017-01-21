package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class ExpAttribute extends Attribute {
    private final Pattern expRegex;

    public ExpAttribute() {
        super("exp");
        this.expRegex = Pattern.compile("[+](\\d+)[%][ ](" + LanguageUtils.getString("lore.exp.keyword").toLowerCase() + ")");
    }

    public Pattern getExpRegex() {
        return expRegex;
    }
}
