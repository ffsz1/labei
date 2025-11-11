package com.juxiao.xchat.dao.sysconf.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 国庆活动积分明细
@author:tp
@date:2020年9月23日
*/
@Getter
@Setter
public class IntegralDetailsDTO {

	private Long uid;
	private String detailDt;
	private String detailType;
	private String remark;
	private Integer  integralValue;
}
