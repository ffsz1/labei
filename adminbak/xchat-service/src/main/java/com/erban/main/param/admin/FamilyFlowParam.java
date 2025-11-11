package com.erban.main.param.admin;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.main.param.admin
 * @date 2018/9/4
 * @time 18:05
 */
public class FamilyFlowParam extends BaseParam {

    private Long familyId;
    /**
     * 开始时间 yyyy-MM-dd
     */
    private String beginDate;
    /**
     * 结束时间 yyyy-MM-dd
     */
    private String endDate;

    private String sortName;
    private String sortOrder;
    private String orderByClauser;

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getOrderByClauser() {
        return orderByClauser;
    }

    public void setOrderByClauser(String orderByClauser) {
        this.orderByClauser = orderByClauser;
    }
}
