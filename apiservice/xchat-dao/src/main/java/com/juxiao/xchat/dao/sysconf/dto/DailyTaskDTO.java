package com.juxiao.xchat.dao.sysconf.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 国庆活动每日任务
@author:tp
@date:2020年9月23日
*/
@Getter
@Setter
public class DailyTaskDTO {

	private Long uid;
	private String taskDay;
	private Integer taskType;
	private Integer progressValue;
}
