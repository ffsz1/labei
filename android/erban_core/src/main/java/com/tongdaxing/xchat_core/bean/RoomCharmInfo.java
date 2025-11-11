package com.tongdaxing.xchat_core.bean;

/**
 * 文件描述：
 *
 * @auther：zwk
 * @data：2019/3/5
 */
public class RoomCharmInfo {
    private int value;
    private boolean withHat;

    public RoomCharmInfo() {
    }

    public RoomCharmInfo(int value, boolean withHat) {
        this.value = value;
        this.withHat = withHat;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isWithHat() {
        return withHat;
    }

    public void setWithHat(boolean withHat) {
        this.withHat = withHat;
    }
}
