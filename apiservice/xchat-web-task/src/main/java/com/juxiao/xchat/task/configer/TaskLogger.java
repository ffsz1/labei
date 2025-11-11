package com.juxiao.xchat.task.configer;

import com.juxiao.xchat.base.utils.DateFormatUtils;
import com.juxiao.xchat.manager.external.dingtalk.DingtalkChatbotManager;
import com.juxiao.xchat.manager.external.dingtalk.bo.DingtalkMessageBO;
import com.juxiao.xchat.manager.external.dingtalk.bo.DingtalkTextMessageBO;
import com.juxiao.xchat.manager.external.dingtalk.conf.DingTalkConf;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Jude
 * @class: TaskLogger.java
 */
@Aspect
@Component
@Slf4j
public class TaskLogger {
    @Autowired
    private DingTalkConf dingTalkConf;
    @Autowired
    private DingtalkChatbotManager dingtalkChatbotManager;


    @Value("${common.system.env}")
    private String env = "prod";

    /**
     * 接口统一日志输出
     *
     * @param point
     */
    @Around("execution(void com.juxiao.xchat.task.scheduler..*Task.*(..)))")
    public void logAround(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        String taskName = point.getTarget().getClass().getSimpleName() + "." + signature.getMethod().getName();
        long time;
        long startTime = System.currentTimeMillis();
        try {
            point.proceed();
            time = System.currentTimeMillis() - startTime;
        } catch (Throwable throwable) {
            time = System.currentTimeMillis() - startTime;
            log.error("[ {} ] 定时任务执行错误:", taskName, throwable);
            if ("prod".equalsIgnoreCase(env)) {
                //发送钉钉预警信息
                DingtalkMessageBO textMessage = new DingtalkTextMessageBO(taskName + "定时任务执行错误：" + throwable.getMessage() + "，预警级别：高", dingTalkConf.getProgrammer(), false);
                dingtalkChatbotManager.send(dingTalkConf.getDevelopChatbot(), textMessage);
            }
        }
        log.info("[ {} ] 定时任务执行完成，开始:>{}，耗时:>{}", taskName, DateFormatUtils.YYYY_MM_DD_HH_MM_SS.date2Str(new Date(startTime)), time);
    }
}
