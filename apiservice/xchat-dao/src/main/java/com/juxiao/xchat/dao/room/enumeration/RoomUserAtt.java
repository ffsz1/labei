package com.juxiao.xchat.dao.room.enumeration;

public enum  RoomUserAtt {

    /**
     * A面用户
     */
    A((byte) 1),
    /**
     * B面用户
     */
    B((byte) 2);

    RoomUserAtt(byte att){
        this.att = att;
    }
    private byte att;

    public byte getAtt(){
        return att;
    }
}
