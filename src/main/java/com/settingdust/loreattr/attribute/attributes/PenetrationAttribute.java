package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.LoreAttributes;
import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class PenetrationAttribute extends Attribute {
    private Pattern penetrationRegex;

    public PenetrationAttribute() {
        super("penetration");
        this.penetrationRegex = Pattern.compile("[+](\\d+)[%][ ](" + LanguageUtils.getString("lore.penetration.keyword").toLowerCase() + ")");
    }

    public Pattern getPenetrationRegex() {
        return penetrationRegex;
    }
}
