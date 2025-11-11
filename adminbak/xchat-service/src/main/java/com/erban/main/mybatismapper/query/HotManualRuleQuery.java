package com.erban.main.mybatismapper.query;

public class HotManualRuleQuery {

    private Long erbanNo;
    private String weekDay;
    private Integer viewType;

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public Integer getViewType() {
        return viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    @Override
    public String toString() {
        return "HotManualRuleQuery{" +
                "erbanNo=" + erbanNo +
                ", weekDay='" + weekDay + '\'' +
                ", viewType=" + viewType +
                '}';
    }
}
