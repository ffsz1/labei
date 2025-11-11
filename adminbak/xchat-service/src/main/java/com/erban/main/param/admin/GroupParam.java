package com.erban.main.param.admin;

/**
 * 分组查询
 */
public enum GroupParam {
    HOUR("DATE_FORMAT(gr.created_time,'%Y-%m-%d-%H')","DATE_FORMAT(gr.created_time,'%Y-%m-%d-%H')","hour"),
    DAY("DATE_FORMAT(gr.created_time,'%Y-%m-%d')","DATE_FORMAT(gr.created_time,'%Y-%m-%d')","DAY"),
    WEEK("DATE_FORMAT(gr.created_time,'%Y-%u')","DATE_FORMAT(gr.created_time,'%Y-%u')","WEEK"),
    MONTH("DATE_FORMAT(gr.created_time,'%Y-%m')","DATE_FORMAT(gr.created_time,'%Y-%m')","MONTH");

    private String groupType;
    private String groupCondition;
    private String groupName;

    GroupParam(String groupType, String groupCondition, String groupName) {
        this.groupType = groupType;
        this.groupCondition = groupCondition;
        this.groupName = groupName;
    }

    public String getGroupType() {
        return groupType;
    }

    public String getGroupCondition() {
        return groupCondition;
    }

    public String getGroupName() {
        return groupName;
    }
}
