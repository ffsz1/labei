package com.juxiao.xchat.dao.item.enumeration;

public enum BoxDrawType {
    headwear(1),    // 1头饰
    car(2),         // 2座驾
    gift(3),        // 3礼物
    ;

    private int value;

    BoxDrawType(int value) {
        this.value = value;
    }

    public boolean compareTo(int value) {
        return value == this.value;
    }

    public int getValue() {
        return value;
    }
}
