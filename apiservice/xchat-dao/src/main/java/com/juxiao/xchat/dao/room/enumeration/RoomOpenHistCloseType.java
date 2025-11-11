package com.juxiao.xchat.dao.room.enumeration;

public enum  RoomOpenHistCloseType {

    /**
     * 正常关闭
     */
    NORMAL((byte) 1),
    /**
     * 异常关闭
     */
    ERROR((byte) 0);


    private Byte type;

    RoomOpenHistCloseType(Byte type) {
        this.type = type;
    }

    public Byte getType() {
        return type;
    }
}
