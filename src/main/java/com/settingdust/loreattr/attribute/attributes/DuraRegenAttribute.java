package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.LoreAttributes;
import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class DuraRegenAttribute extends Attribute{
    private Pattern duraRegenRegex;

    public DuraRegenAttribute() {
        super("duraregen");
        this.duraRegenRegex = Pattern.compile("[+](\\d+)[/][s][ ](" + LanguageUtils.getString("lore.duraregen.keyword").toLowerCase() + ")");
    }

    public Pattern getDuraRegenRegex() {
        return duraRegenRegex;
    }
}
