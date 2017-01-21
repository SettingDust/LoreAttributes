package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class DuraAttribute extends Attribute {
    private Pattern duraRegex;

    public DuraAttribute() {
        super("dura");
        this.duraRegex = Pattern.compile("(" + LanguageUtils.getString("lore.dura.keyword") + ": )(\\d+)[/](\\d+)");
    }

    public Pattern getDuraRegex() {
        return duraRegex;
    }
}
