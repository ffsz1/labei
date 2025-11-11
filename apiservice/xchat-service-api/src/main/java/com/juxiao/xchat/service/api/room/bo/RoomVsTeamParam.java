package com.juxiao.xchat.service.api.room.bo;

import java.util.List;

import lombok.Data;

/**
@author:tp
@date:2020年10月28日
*/
@Data
public class RoomVsTeamParam {

	//队伍序号：1（蓝方），2（红方）
	private Integer teamIndex;
	//一方用户集
	private List<Long> uids;
}
