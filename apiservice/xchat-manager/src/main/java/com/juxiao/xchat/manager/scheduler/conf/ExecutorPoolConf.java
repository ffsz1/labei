package com.juxiao.xchat.manager.scheduler.conf;

import com.juxiao.xchat.base.spring.support.YamlPropertyLoaderFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @class: ExecutorPool
 * @author: chenjunsheng
 * @date 2018年4月27日
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "scheduling.executor")
@PropertySource(value = "classpath:xchat.manager.yml", factory = YamlPropertyLoaderFactory.class)
public class ExecutorPoolConf {

    private int corePoolSize = 30;

    private int maxPoolSize = 50;

    private int queueCapacity = 30;

    private int keepAliveSeconds = 300;

    private String threadNamePrefix = "xchat-task-";

    /**
     * 初始话线程池
     *
     * @return
     * @author: chenjunsheng
     * @date 2018年4月27日
     */
    @Bean
    public Executor executorPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
