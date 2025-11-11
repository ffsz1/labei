package com.juxiao.xchat.dao.room.enumeration;

public enum RoomOptStatus {

    /**
     * 在线
     */
    in((byte) 1),
    /**
     * 离线
     */
    out((byte) 2);

    RoomOptStatus(byte status){
        this.status = status;
    }

    private byte status;

    public byte getStatus() {
        return status;
    }
}
