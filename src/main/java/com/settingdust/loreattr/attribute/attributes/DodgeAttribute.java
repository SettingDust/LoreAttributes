package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class DodgeAttribute extends Attribute{
    private Pattern dodgeRegex;

    public DodgeAttribute() {
        super("dodge");
        this.dodgeRegex = Pattern.compile("[+](\\d+)[%][ ](" + LanguageUtils.getString("lore.dodge.keyword").toLowerCase() + ")");
    }

    public Pattern getDodgeRegex() {
        return dodgeRegex;
    }
}
