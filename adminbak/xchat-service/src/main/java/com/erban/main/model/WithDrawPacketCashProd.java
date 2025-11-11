package com.erban.main.model;

import java.util.Date;

public class WithDrawPacketCashProd {
    private Integer packetProdCashId;

    private Double packetNum;

    private Byte prodStauts;

    private Integer seqNo;

    private Date createTime;

    public Integer getPacketProdCashId() {
        return packetProdCashId;
    }

    public void setPacketProdCashId(Integer packetProdCashId) {
        this.packetProdCashId = packetProdCashId;
    }

    public Double getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(Double packetNum) {
        this.packetNum = packetNum;
    }

    public Byte getProdStauts() {
        return prodStauts;
    }

    public void setProdStauts(Byte prodStauts) {
        this.prodStauts = prodStauts;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
