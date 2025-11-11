package com.erban.main.message;


import java.io.Serializable;

/**
 * 礼物消息
 */
public class GiftMessage implements Serializable {
    private String messId;    // 消息唯一标识
    private Long messTime;    // 消息创建时间
    private Long sendUid;     // 发送者UID
    private Long recvUid;     // 接收者UID
    private Long roomUid;     // 房间主UID
    private Byte roomType;    // 房间类型
    private Long goldNum;     // 金币数量
    private Long afterGoldNum;     // 减去用户礼物后的金币数量
    private Long useGiftPurseNum;     // 剩余多少用户礼物
    private Double diamondNum;// 钻石数量
    private Integer giftId;   // 礼物标识
    private Integer giftNum;  // 礼物数量
    private Byte sendType; // 赠送类型，如： 1对1送礼物，全麦送礼物
    private String expressMessage; // 表白留言

    public Long getUseGiftPurseNum() {
        return useGiftPurseNum;
    }

    public void setUseGiftPurseNum(Long useGiftPurseNum) {
        this.useGiftPurseNum = useGiftPurseNum;
    }

    public Long getAfterGoldNum() {
        return afterGoldNum;
    }

    public void setAfterGoldNum(Long afterGoldNum) {
        this.afterGoldNum = afterGoldNum;
    }

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

    public Long getSendUid() {
        return sendUid;
    }

    public void setSendUid(Long sendUid) {
        this.sendUid = sendUid;
    }

    public Long getRecvUid() {
        return recvUid;
    }

    public void setRecvUid(Long recvUid) {
        this.recvUid = recvUid;
    }

    public Long getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(Long roomUid) {
        this.roomUid = roomUid;
    }

    public Byte getRoomType() {
        return roomType;
    }

    public void setRoomType(Byte roomType) {
        this.roomType = roomType;
    }

    public Double getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(Double diamondNum) {
        this.diamondNum = diamondNum;
    }

    public Long getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(Long goldNum) {
        this.goldNum = goldNum;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public Integer getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }

    public Byte getSendType() {
        return sendType;
    }

    public void setSendType(Byte sendType) {
        this.sendType = sendType;
    }

    public String getExpressMessage() {
        return expressMessage;
    }

    public void setExpressMessage(String expressMessage) {
        this.expressMessage = expressMessage;
    }

    @Override
    public String toString() {
        return "GiftMessage{" +
                "messId='" + messId + '\'' +
                ", messTime=" + messTime +
                ", sendUid=" + sendUid +
                ", recvUid=" + recvUid +
                ", roomUid=" + roomUid +
                ", roomType=" + roomType +
                ", goldNum=" + goldNum +
                ", afterGoldNum=" + afterGoldNum +
                ", useGiftPurseNum=" + useGiftPurseNum +
                ", diamondNum=" + diamondNum +
                ", giftId=" + giftId +
                ", giftNum=" + giftNum +
                ", sendType=" + sendType +
                ", expressMessage='" + expressMessage + '\'' +
                '}';
    }
}
