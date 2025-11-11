package com.erban.main.dto;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/11/12
 * @time 14:21
 */
public class RoomFlowEchartsParam {

    /** 按天统计 */
    public static final int DATE_TYPE_DAY = 1;
    /** 按周统计 */
    public static final int DATE_TYPE_WEEK = 2;
    /** 按月统计 */
    public static final int DATE_TYPE_MONTH = 3;
    /** 按年统计 */
    public static final int DATE_TYPE_YEAR = 4;


    private Integer classType;
    private Long uid;
    private List<Long> uidList;
    private String beginDate;
    private String endDate;
    private String minNum;
    private String maxNum;

    public Integer getClassType() {
        return classType;
    }

    public void setClassType(Integer classType) {
        this.classType = classType;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public List<Long> getUidList() {
        return uidList;
    }

    public void setUidList(List<Long> uidList) {
        this.uidList = uidList;
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

    public String getMinNum() {
        return minNum;
    }

    public void setMinNum(String minNum) {
        this.minNum = minNum;
    }

    public String getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(String maxNum) {
        this.maxNum = maxNum;
    }
}
