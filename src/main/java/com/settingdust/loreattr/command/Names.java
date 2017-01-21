package com.settingdust.loreattr.command;

/**
 * Created by SettingDust on 17-1-9.
 */
public enum Names {
    LOREATTR("loreattr") {};
    private String name = "";

    Names(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
