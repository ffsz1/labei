package com.erban.main.param.admin;

import com.erban.main.util.StringUtils;

/**
 * 后台管理-体现请求入参
 */
public class RoomFlowParam extends BaseParam{
    private Long erbanNo;//uid
    private String beginDate;//开始时间 yyyy-MM-dd
    private String endDate;//结束时间 yyyy-MM-dd
    private Byte roomType;
    private String groupCondition;
    private Integer roomTagId;
    private String sortName;
    private String sortOrder;
    private String orderByClauser;
    private Integer roomTag;
    private Long guildId;

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
        this.endDate = StringUtils.isBlank(endDate)?null:StringUtils.trim(endDate);
    }

    public Byte getRoomType() {
        return roomType;
    }

    public void setRoomType(Byte roomType) {
        this.roomType = roomType;
    }

    public String getGroupCondition() {
        return groupCondition;
    }

    public void setGroupCondition(String groupCondition) {
        this.groupCondition = groupCondition;
    }

    public Integer getRoomTagId() {
        return roomTagId;
    }

    public void setRoomTagId(Integer roomTagId) {
        this.roomTagId = roomTagId;
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

    public Integer getRoomTag() {
        return roomTag;
    }

    public void setRoomTag(Integer roomTag) {
        this.roomTag = roomTag;
    }
    
    public Long getGuildId() {
		return guildId;
	}

	public void setGuildId(Long guildId) {
		this.guildId = guildId;
	}

	@Override
    public String toString() {
        return "RoomFlowParam{" +
                "erbanNo=" + erbanNo +
                ", beginDate='" + beginDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", roomType=" + roomType +
                ", groupCondition='" + groupCondition + '\'' +
                ", roomTagId=" + roomTagId +
                ", sortName='" + sortName + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", orderByClauser='" + orderByClauser + '\'' +
                ", roomTag=" + roomTag +
                ", guildId=" + guildId +
                '}';
    }
}

