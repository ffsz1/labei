package com.juxiao.xchat.manager.common.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.juxiao.xchat.base.spring.support.YamlPropertyLoaderFactory;

import lombok.Data;

/**
 * 国庆活动积分商城配置
@author:tp
@date:2020年9月23日
*/
@Data
@Component
@ConfigurationProperties(prefix = "common.integralmall")
@PropertySource(value = "classpath:xchat.manager.yml", factory = YamlPropertyLoaderFactory.class)
public class NationalDayConf {

	/**
	 * 限时头饰Id
	 */
	private Integer headwearLimitTimeId;
	
	/**
	 * 限时座驾Id
	 */
	private Integer gifCarLimitTimeId;
	
	
	/**
	 * 金币50
	 */
	private Integer gold;
	
	/**
	 * 商城头饰Id
	 */
	private Integer mallHeadwearId;
	
	/**
	 * 商城座驾Id
	 */
	private Integer mallGifCarId;
	
	
}
