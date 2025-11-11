package com.xchat.oauth2.service.param;

import java.util.Date;
import java.util.List;

/**
 * @class: AcountListAminParam.java
 * @author: chenjunsheng
 * @date 2018/5/30
 */
public class AcountListAminParam {
    private String[] erbanNos;
    private String[] uids;
    private Integer gender;
    private Integer defType;
    private Integer hasCharge;
    private Date startDate;
    private Date endDate;

    public AcountListAminParam() {
    }

    public AcountListAminParam(String[] erbanNos, String[] uids, Integer gender, Integer defType, Integer hasCharge, Date startDate, Date endDate) {
        this.erbanNos = erbanNos;
        this.uids = uids;
        this.gender = gender;
        this.defType = defType;
        this.hasCharge = hasCharge;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String[] getErbanNos() {
        return erbanNos;
    }

    public void setErbanNos(String[] erbanNos) {
        this.erbanNos = erbanNos;
    }

    public String[] getUids() {
        return uids;
    }

    public void setUids(String[] uids) {
        this.uids = uids;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getDefType() {
        return defType;
    }

    public void setDefType(Integer defType) {
        this.defType = defType;
    }

    public Integer getHasCharge() {
        return hasCharge;
    }

    public void setHasCharge(Integer hasCharge) {
        this.hasCharge = hasCharge;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
