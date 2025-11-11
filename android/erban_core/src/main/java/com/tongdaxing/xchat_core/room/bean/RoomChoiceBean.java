package com.tongdaxing.xchat_core.room.bean;

public class RoomChoiceBean {
    private boolean random;
    private int position;
    private int resId;
    private boolean isSelect;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }
}
