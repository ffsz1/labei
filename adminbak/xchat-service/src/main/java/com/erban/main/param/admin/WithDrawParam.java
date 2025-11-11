package com.erban.main.param.admin;

import com.erban.main.util.StringUtils;

/**
 * 后台管理-体现请求入参
 */
public class WithDrawParam extends BaseParam {
    private Long erbanNo;//uid
    private String beginDate;//开始时间 yyyy-MM-dd
    private String endDate;//结束时间 yyyy-MM-dd
    private Byte status;//状态
    private Byte gender;
    private Byte withdrawStatus;
    private Byte tranType;
    private Byte realTranType;

    public Byte getRealTranType() {
        return realTranType;
    }

    public void setRealTranType(Byte realTranType) {
        this.realTranType = realTranType;
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = StringUtils.isBlank(beginDate) ? null : StringUtils.trim(beginDate);
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = StringUtils.isBlank(endDate) ? null : StringUtils.trim(endDate);
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Byte getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(Byte withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    public Byte getTranType() {
        return tranType;
    }

    public void setTranType(Byte tranType) {
        this.tranType = tranType;
    }


}
