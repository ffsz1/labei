package com.erban.main.model;

public class ChargeProd {
    private String chargeProdId;

    private String prodName;

    private String prodDesc;

    private Byte prodStatus;

    private Long money;

    private Integer changeGoldRate;

    private Integer giftGoldNum;

    private Integer firstGiftGoldNum;

    private Byte channel;

    private Byte seqNo;

    public String getChargeProdId() {
        return chargeProdId;
    }

    public void setChargeProdId(String chargeProdId) {
        this.chargeProdId = chargeProdId == null ? null : chargeProdId.trim();
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName == null ? null : prodName.trim();
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc == null ? null : prodDesc.trim();
    }

    public Byte getProdStatus() {
        return prodStatus;
    }

    public void setProdStatus(Byte prodStatus) {
        this.prodStatus = prodStatus;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Integer getChangeGoldRate() {
        return changeGoldRate;
    }

    public void setChangeGoldRate(Integer changeGoldRate) {
        this.changeGoldRate = changeGoldRate;
    }

    public Integer getGiftGoldNum() {
        return giftGoldNum;
    }

    public void setGiftGoldNum(Integer giftGoldNum) {
        this.giftGoldNum = giftGoldNum;
    }

    public Integer getFirstGiftGoldNum() {
        return firstGiftGoldNum;
    }

    public void setFirstGiftGoldNum(Integer firstGiftGoldNum) {
        this.firstGiftGoldNum = firstGiftGoldNum;
    }

    public Byte getChannel() {
        return channel;
    }

    public void setChannel(Byte channel) {
        this.channel = channel;
    }

    public Byte getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Byte seqNo) {
        this.seqNo = seqNo;
    }
}
