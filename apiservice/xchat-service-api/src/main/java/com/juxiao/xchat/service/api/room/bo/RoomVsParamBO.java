package com.juxiao.xchat.service.api.room.bo;

import java.util.List;

import lombok.Data;

/**
 * @author:tp
 * @date:2020年10月28日
 */
@Data
public class RoomVsParamBO {

	private Integer minutes;
	// 房主uid
	private Long roomUid;
	// 发起PK用户uid
	private Long startUser;
	//得分类型：0（按礼物价值）
	private Integer scoreType;
    //队列集
	List<RoomVsTeamParam> teams;
	

}
