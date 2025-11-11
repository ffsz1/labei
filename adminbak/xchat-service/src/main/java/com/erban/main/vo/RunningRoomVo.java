package com.erban.main.vo;

/**
 * Created by liuguofu on 2017/8/22.
 */
public class RunningRoomVo {
    private Long uid;
    private int onlineNum;
    private Long roomId;
    private Byte type;
    private int calcSumDataIndex;//综合人气+流水值
    private int count;  // 计数器，用于指示某段时间内一直没人

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCalcSumDataIndex() {
        return calcSumDataIndex;
    }

    public void setCalcSumDataIndex(int calcSumDataIndex) {
        this.calcSumDataIndex = calcSumDataIndex;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {

        return roomId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public int getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(int onlineNum) {
        this.onlineNum = onlineNum;
    }
}
