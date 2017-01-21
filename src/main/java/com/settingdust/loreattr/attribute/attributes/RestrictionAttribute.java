package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class RestrictionAttribute extends Attribute {
    private Pattern restrictionRegex;

    public RestrictionAttribute() {
        super("restriction");
        this.restrictionRegex = Pattern.compile("(" + LanguageUtils.getString("lore.restriction.keyword").toLowerCase() + ": )(\\w*)");
    }

    public Pattern getRestrictionRegex() {
        return restrictionRegex;
    }
}
