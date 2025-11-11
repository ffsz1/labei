package com.erban.main.param.admin;

import com.erban.main.util.StringUtils;

/**
 * 靓号管理
 */
public class PrettyErbanNoParam extends BaseParam {
    private Long prettyErbanNo;//拉贝号
    private String beginDate;//开始时间
    private String endDate;//结束时间
    private Byte status;//使用状态
    private Byte type;//类型:1.

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getPrettyErbanNo() {
        return prettyErbanNo;
    }

    public void setPrettyErbanNo(Long prettyErbanNo) {
        this.prettyErbanNo = prettyErbanNo;
    }
}
