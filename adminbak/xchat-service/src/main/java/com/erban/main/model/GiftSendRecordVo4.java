package com.erban.main.model;

import java.util.Date;

/**
 * 房间礼物赠送明细
 */
public class GiftSendRecordVo4 {
    private Long senUid;
    private Long receiveUid;
    private Long roomUid;
    private String sendNick;
    private String receiveNick;
    private Integer giftNum;
    private String giftName;
    private String giftUrl;
    private String time;
    private Long totalGoldNum;

    public Long getSenUid() {
        return senUid;
    }

    public void setSenUid(Long senUid) {
        this.senUid = senUid;
    }

    public Long getReceiveUid() {
        return receiveUid;
    }

    public void setReceiveUid(Long receiveUid) {
        this.receiveUid = receiveUid;
    }

    public String getSendNick() {
        return sendNick;
    }

    public void setSendNick(String sendNick) {
        this.sendNick = sendNick;
    }

    public String getReceiveNick() {
        return receiveNick;
    }

    public void setReceiveNick(String receiveNick) {
        this.receiveNick = receiveNick;
    }

    public Integer getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftUrl() {
        return giftUrl;
    }

    public void setGiftUrl(String giftUrl) {
        this.giftUrl = giftUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(Long roomUid) {
        this.roomUid = roomUid;
    }

    public Long getTotalGoldNum() {
        return totalGoldNum;
    }

    public void setTotalGoldNum(Long totalGoldNum) {
        this.totalGoldNum = totalGoldNum;
    }
}
