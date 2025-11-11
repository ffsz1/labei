package com.erban.admin.main.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

public class GiftVo {
    @Excel(name = "赠拉贝号")
    private Long sendNo;
    @Excel(name = "赠昵称")
    private String sendNick;
    @Excel(name = "收拉贝号")
    private Long reciveNo;
    @Excel(name = "收昵称")
    private String reciveNick;
    @Excel(name = "房间ID")
    private Long roomNo;
    @Excel(name = "房间名称")
    private String roomNick;
    @Excel(name = "礼物名称")
    private String giftName;
    private Byte giftType;
    @Excel(name = "礼物类型")
    private String giftTypeName;
    @Excel(name = "礼物数量")
    private Integer giftNum;
    @Excel(name = "总金币")
    private Integer totalGoldNum;
    @Excel(name = "创建时间", format = "yyyy-MM-dd hh:mm:ss", width = 20)
    private Date createTime;

    public Long getSendNo() {
        return sendNo;
    }

    public void setSendNo(Long sendNo) {
        this.sendNo = sendNo;
    }

    public String getSendNick() {
        return sendNick;
    }

    public void setSendNick(String sendNick) {
        this.sendNick = sendNick;
    }

    public Long getReciveNo() {
        return reciveNo;
    }

    public void setReciveNo(Long reciveNo) {
        this.reciveNo = reciveNo;
    }

    public String getReciveNick() {
        return reciveNick;
    }

    public void setReciveNick(String reciveNick) {
        this.reciveNick = reciveNick;
    }

    public Long getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(Long roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomNick() {
        return roomNick;
    }

    public void setRoomNick(String roomNick) {
        this.roomNick = roomNick;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public Integer getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }

    public Integer getTotalGoldNum() {
        return totalGoldNum;
    }

    public void setTotalGoldNum(Integer totalGoldNum) {
        this.totalGoldNum = totalGoldNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getGiftType() {
        return giftType;
    }

    public void setGiftType(Byte giftType) {
        this.giftType = giftType;
    }

    public String getGiftTypeName() {
        return giftTypeName;
    }

    public void setGiftTypeName(String giftTypeName) {
        this.giftTypeName = giftTypeName;
    }

    @Override
    public String toString() {
        return "GiftVo{" +
                "sendNo=" + sendNo +
                ", sendNick='" + sendNick + '\'' +
                ", reciveNo=" + reciveNo +
                ", reciveNick='" + reciveNick + '\'' +
                ", roomNo=" + roomNo +
                ", roomNick='" + roomNick + '\'' +
                ", giftName='" + giftName + '\'' +
                ", giftType=" + giftType +
                ", giftTypeName='" + giftTypeName + '\'' +
                ", giftNum=" + giftNum +
                ", totalGoldNum=" + totalGoldNum +
                ", createTime=" + createTime +
                '}';
    }
}
