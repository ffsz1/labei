package com.erban.main.model;

import java.util.Date;

public class RedeemCode {
    private String code;

    private Long amount;

    private Long useUid;

    private String useIp;

    private String useImei;

    private Integer useStatus;

    private Date useTime;

    private Date createTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getUseUid() {
        return useUid;
    }

    public void setUseUid(Long useUid) {
        this.useUid = useUid;
    }

    public String getUseIp() {
        return useIp;
    }

    public void setUseIp(String useIp) {
        this.useIp = useIp == null ? null : useIp.trim();
    }

    public String getUseImei() {
        return useImei;
    }

    public void setUseImei(String useImei) {
        this.useImei = useImei == null ? null : useImei.trim();
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
