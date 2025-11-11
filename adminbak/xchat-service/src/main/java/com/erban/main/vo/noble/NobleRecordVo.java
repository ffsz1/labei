package com.erban.main.vo.noble;

public class NobleRecordVo {

    private Long uid;       // 账单用户
    private String optStr;  // 如：开通"皇帝"贵族
    private String payStr;  // 如：-2000元
    private long recordTime;// 记录的时间

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getOptStr() {
        return optStr;
    }

    public void setOptStr(String optStr) {
        this.optStr = optStr;
    }

    public String getPayStr() {
        return payStr;
    }

    public void setPayStr(String payStr) {
        this.payStr = payStr;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }
}
