package com.juxiao.xchat.dao.sysconf.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 顶部Tab
 * 
@author:tp
@date:2020年9月21日
*/
@Getter
@Setter
public class TopTabDTO {

	private String tabName;
    private String tabValue;
    private String nameSpace;
    private List<BannerDTO> banners;
}
