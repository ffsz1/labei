package com.erban.main.param.admin;

import com.erban.main.util.StringUtils;

/**
 * 订单管理
 */
public class UserAssociateParam extends BaseParam {
    private Long erbanNo;//邀请人拉贝号
    private String beginDate;//开始时间
    private String endDate;//结束时间
    private Long uid; //邀请人UID

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
        this.beginDate = StringUtils.isBlank(beginDate)?null:StringUtils.trim(beginDate);
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate =  StringUtils.isBlank(endDate)?null:StringUtils.trim(endDate);
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
