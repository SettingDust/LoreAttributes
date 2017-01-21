package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class LightingAttribute extends Attribute {
    private Pattern extraLightingRegex;

    public LightingAttribute() {
        super("lighting");
        this.extraLightingRegex = Pattern.compile("[+](\\d+)[%][ ](" + LanguageUtils.getString("lore.lighting.keyword").toLowerCase() + ")");
    }

    public Pattern getExtraLightingRegex() {
        return extraLightingRegex;
    }
}
