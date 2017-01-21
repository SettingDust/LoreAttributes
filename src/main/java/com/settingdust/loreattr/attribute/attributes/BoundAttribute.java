package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class BoundAttribute extends Attribute{
    private final Pattern boundRegex;

    public BoundAttribute() {
        super("bound");
        this.boundRegex = Pattern.compile("(" + LanguageUtils.getString("lore.bound.keyword").toLowerCase() + ": )(\\w*)");
    }

    public Pattern getBoundRegex() {
        return boundRegex;
    }
}
