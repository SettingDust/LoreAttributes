package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class UnbreakableAttribute extends Attribute{
    private final Pattern unbreakableRegex;

    public UnbreakableAttribute() {
        super("unbreakable");
        this.unbreakableRegex = Pattern.compile(LanguageUtils.getString("lore.unbreakable.keyword").toLowerCase());
    }

    public Pattern getUnbreakableRegex() {
        return unbreakableRegex;
    }
}
