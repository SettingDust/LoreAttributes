package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class LevelAttribute extends Attribute {

    private Pattern levelRegex;

    public LevelAttribute() {
        super("level");
        this.levelRegex = Pattern.compile(LanguageUtils.getString("lore.level.keyword").toLowerCase() + "[ ](\\d+)");
    }

    public Pattern getLevelRegex() {
        return levelRegex;
    }
}
