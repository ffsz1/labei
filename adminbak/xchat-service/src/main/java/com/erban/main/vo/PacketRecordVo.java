package com.erban.main.vo;

public class PacketRecordVo {

    private long uid;           //账号
    private long date;          //日期
    private long recordTime;    //具体时间
    private String typeStr;     //红包类型
    private Double packetNum;   //红包数量

    @Override
    public String toString() {
        return "PacketRecordVo{" +
                "uid=" + uid +
                ", date=" + date +
                ", detailTime=" + recordTime +
                ", typeStr='" + typeStr + '\'' +
                ", packetNum=" + packetNum +
                '}';
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public Double getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(Double packetNum) {
        this.packetNum = packetNum;
    }
}
