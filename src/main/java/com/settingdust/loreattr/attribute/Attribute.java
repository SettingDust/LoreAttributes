package com.settingdust.loreattr.attribute;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by SettingDust on 17-1-8.
 */
public class Attribute implements ConfigurationSerializable {
    private String name = "";

    public Attribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", this.name);
        return map;
    }

    public static Attribute deserialize(Map<String, Object> map) {
        Attribute attribute = new Attribute(String.valueOf(map.get("name")));
        return attribute;
    }
}
