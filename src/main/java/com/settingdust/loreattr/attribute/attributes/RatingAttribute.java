package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;

import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class RatingAttribute extends Attribute {
    private Pattern ratingRegex;

    public RatingAttribute() {
        super("rating");
        this.ratingRegex = Pattern.compile("[+](\\d+)[%][ ](" + LanguageUtils.getString("lore.rating.keyword").toLowerCase() + ")");
    }

    public Pattern getRatingRegex() {
        return ratingRegex;
    }
}
