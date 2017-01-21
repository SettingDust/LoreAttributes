package com.settingdust.loreattr.attribute.attributes;

import com.settingdust.loreattr.attribute.Attribute;
import com.settingdust.loreattr.util.LanguageUtils;
import java.util.regex.Pattern;



/**
 * Created by SettingDust on 17-1-8.
 */
public class AttackSpeedAttribute extends Attribute {
    private Pattern attackSpeedRegex;

    public AttackSpeedAttribute() {
        super("attack-speed");
        this.attackSpeedRegex = Pattern.compile("[+](\\d+)[ ](" + LanguageUtils.getString("lore." + getName() + "keyword").toLowerCase() + ")");

    }

    public Pattern getAttackSpeedRegex() {
        return attackSpeedRegex;
    }
}
