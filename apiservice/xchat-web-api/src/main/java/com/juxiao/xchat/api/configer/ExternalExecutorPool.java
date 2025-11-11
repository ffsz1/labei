package com.juxiao.xchat.api.configer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class ExternalExecutorPool {

    private Integer corePoolSize = 30;

    private Integer maxPoolSize = 50;

    private Integer queueCapacity = 30;

    private Integer keepAliveSeconds = 300;

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
        executor.setThreadNamePrefix("xchat-api-task-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
