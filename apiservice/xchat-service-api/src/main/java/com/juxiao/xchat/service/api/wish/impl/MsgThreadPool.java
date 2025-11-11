package com.juxiao.xchat.service.api.wish.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MsgThreadPool {
    @Bean(value = "ExecutorService")
    public ExecutorService getExecutorService(){
        return Executors.newCachedThreadPool();
    }
}
