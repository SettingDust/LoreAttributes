package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class TrueDamageAttribute extends Attribute{
    private Pattern trueDamageRegex;

    public TrueDamageAttribute() {
        super("true-damage");
        this.trueDamageRegex = Pattern.compile("[+](\\d+)[ ](" + LanguageUtils.getString("lore.true-damage.keyword").toLowerCase() + ")");
    }

    public Pattern getTrueDamageRegex() {
        return trueDamageRegex;
    }
}
