package com.erban.main.vo;

import com.xchat.common.utils.DateUtil;

import java.util.Date;

/**
 * 表白墙礼物
 */
public class ExpressWallVo {

    private Long id;
    private Long sendUid;
    private String sendNick;
    private String sendAvatar;

    private Integer giftId;
    private String giftName;
    private String picUrl;
    private Long giftGold;
    private Integer giftNum;

    private Long receiveUid;
    private String receiveNick;
    private String receiveAvatar;

    private String expressMessage;
    private Date createTime;
    private String date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSendUid() {
        return sendUid;
    }

    public void setSendUid(Long sendUid) {
        this.sendUid = sendUid;
    }

    public String getSendNick() {
        return sendNick;
    }

    public void setSendNick(String sendNick) {
        this.sendNick = sendNick;
    }

    public String getSendAvatar() {
        return sendAvatar;
    }

    public void setSendAvatar(String sendAvatar) {
        this.sendAvatar = sendAvatar;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Long getGiftGold() {
        return giftGold;
    }

    public void setGiftGold(Long giftGold) {
        this.giftGold = giftGold;
    }

    public Long getReceiveUid() {
        return receiveUid;
    }

    public Integer getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }

    public void setReceiveUid(Long receiveUid) {
        this.receiveUid = receiveUid;
    }

    public String getReceiveNick() {
        return receiveNick;
    }

    public void setReceiveNick(String receiveNick) {
        this.receiveNick = receiveNick;
    }

    public String getReceiveAvatar() {
        return receiveAvatar;
    }

    public void setReceiveAvatar(String receiveAvatar) {
        this.receiveAvatar = receiveAvatar;
    }

    public String getExpressMessage() {
        return expressMessage;
    }

    public void setExpressMessage(String expressMessage) {
        this.expressMessage = expressMessage;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.date = DateUtil.date2Str(createTime, DateUtil.DateFormat.YYYY_MM_DD_HH_MM_SS);
        this.createTime = createTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
