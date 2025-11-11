package com.juxiao.xchat.dao.sysconf.enumeration;

public enum SysConfDrawActSwitch {
    OPEN("1"),
    CLOSE("2");

    private String value;

    SysConfDrawActSwitch(String value) {
        this.value = value;
    }

    public static boolean isOpen(String value) {
        return OPEN.value.equalsIgnoreCase(value);
    }
}
