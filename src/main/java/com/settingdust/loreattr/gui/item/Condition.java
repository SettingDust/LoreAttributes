package com.settingdust.loreattr.gui.item;

/**
 * Created by SettingDust on 17-1-8.
 */
public class Condition {
    public boolean drag = false;
    public boolean click = false;

    public Condition(boolean drag, boolean click) {
        this.drag = drag;
        this.click = click;
    }
}
