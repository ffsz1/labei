package com.erban.main.message;

import java.io.Serializable;

public class NobleMessage implements Serializable {

    private String messId;    // 消息唯一标识
    private Long messTime;    // 消息创建时间
    private Byte optType;     // 操作类型，1：开通，2：续费
    private Byte payType;     // 支付类型，1：金币，2：支付，3：打公款
    private Long uid;         // 开通的用户UID
    private Long roomUid;     // 开通所在房间UID（第一次开通有效）
    private Integer nobleId;  // 开通的贵族ID
    private String nobleName; // 开通的贵族名称
    private Long nobleGold;   // 开通后返还的贵族金币
    private Long payGold;     // 需要支付的金币
    private Long money;       // 支付和打公款开通/续费有效

    @Override
    public String toString() {
        return "NobleMessage{" +
                "messId='" + messId + '\'' +
                ", messTime=" + messTime +
                ", optType=" + optType +
                ", payType=" + payType +
                ", uid=" + uid +
                ", roomUid=" + roomUid +
                ", nobleId=" + nobleId +
                ", nobleName='" + nobleName + '\'' +
                ", nobleGold=" + nobleGold +
                ", payGold=" + payGold +
                ", money=" + money +
                '}';
    }

    public Byte getOptType() {
        return optType;
    }

    public void setOptType(Byte optType) {
        this.optType = optType;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(Long roomUid) {
        this.roomUid = roomUid;
    }

    public Integer getNobleId() {
        return nobleId;
    }

    public void setNobleId(Integer nobleId) {
        this.nobleId = nobleId;
    }

    public String getNobleName() {
        return nobleName;
    }

    public void setNobleName(String nobleName) {
        this.nobleName = nobleName;
    }

    public Long getNobleGold() {
        return nobleGold;
    }

    public void setNobleGold(Long nobleGold) {
        this.nobleGold = nobleGold;
    }

    public Long getPayGold() {
        return payGold;
    }

    public void setPayGold(Long payGold) {
        this.payGold = payGold;
    }
}
