package com.juxiao.xchat.dao.sysconf.dto;
/**
@author:tp
@date:2020年9月18日
*/

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TabBannerDTO {

	public List<TopTabDTO> tabs;
	// 开房图标显示状态 1.显示 2.隐藏
	public Integer iconStatus;
}
