package com.juxiao.xchat.dao.room.enumeration;

public enum RoomVsResultType {
    /**
     * 房间PK结果类型：0（有胜负），1（平局）
     */
    WIN_OR_LOSE((byte) 0),

    DRAW((byte) 1);

    RoomVsResultType(byte value){
        this.value = value;
    }
    private byte value;

    public byte getValue(){
        return value;
    }
}
