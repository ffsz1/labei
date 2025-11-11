package com.erban.main.service.job;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Service;

/**
 * 
 * @author yanghaoyu 将quartz中的job注入到spring容器中，交给他管理。
 */
@Service("jobFactory")
public class JobFactory extends AdaptableJobFactory {

	@Autowired
	private AutowireCapableBeanFactory capableBeanFactory;

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		Object jobInstance = super.createJobInstance(bundle);
		/* 进行注入 */
		capableBeanFactory.autowireBean(jobInstance);
		return jobInstance;
	}
}
