package com.erban.main.vo;

import java.util.Date;

/**
 * Created by liuguofu on 2017/12/4.
 */
public class PacketWithDrawRecordVo extends UserBasicVo {
    private String recordId;

    private Integer packetProdCashId;

    private Double packetNum;

    private Date createTime;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }


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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
