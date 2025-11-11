package com.juxiao.xchat.dao.room.enumeration;

public enum RoomType {

    /**
     * 拍卖房
     */
    auct((byte) 1),
    /**
     * 轻聊房
     */
    radio((byte) 2),
    /**
     * 轰趴房
     */
    game((byte) 3);

    private Byte type;

    RoomType(Byte type) {
        this.type = type;
    }

    public Byte getType() {
        return type;
    }
}
