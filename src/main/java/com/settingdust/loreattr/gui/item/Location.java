package com.settingdust.loreattr.gui.item;

/**
 * Created by SettingDust on 17-1-8.
 */
public class Location {
    private int x;
    private int y;

    public Location(int x, int y) {
        this.x = x > 8 ? 0 : x < 0 ? 0 : x;
        this.y = y > 5 ? 0 : y < 0 ? 0 : y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x > 8 ? 0 : x < 0 ? 0 : x;
    }

    public void setY(int y) {
        this.y = y > 5 ? 0 : y < 0 ? 0 : y;
    }

    public int toSlot() {
        return x + y * 9;
    }

    public static Location getLocation(int slot) {
        return new Location((int) Math.floor(slot / 8), slot % 8);
    }
}
