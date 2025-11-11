package com.juxiao.xchat.dao.room.enumeration;

public enum RoomVsScoreType {
    /**
     * 房间PK得分类型：0（按礼物总价值）
     */
    SCORE_BY_GITF_GOLD_TOTAL((byte) 0);

    RoomVsScoreType(byte value){
        this.value = value;
    }
    private byte value;

    public byte getValue(){
        return value;
    }
}
