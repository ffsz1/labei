package com.erban.main.message;


import java.io.Serializable;

/**
 * 大额礼物消息，全服消息
 */
public class BigGiftMessage implements Serializable {
    private String messId;    // 消息唯一标识
    private Long messTime;    // 消息创建时间
    private Long uid;
    private Long targetUid;
    private Long roomUid;
    private int giftId;
    private int giftNum;
    private Long[] targetSize;

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public Long getMessTime() {
        return messTime;
    }

    public void setMessTime(Long messTime) {
        this.messTime = messTime;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(Long targetUid) {
        this.targetUid = targetUid;
    }

    public Long getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(Long roomUid) {
        this.roomUid = roomUid;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public Long[] getTargetSize() {
        return targetSize;
    }

    public void setTargetSize(Long[] targetSize) {
        this.targetSize = targetSize;
    }
}
