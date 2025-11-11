package com.juxiao.xchat.manager.common.aspect.impl;

import com.alibaba.fastjson.JSON;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.common.aspect.LogAspectManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.external.dingtalk.DingtalkChatbotManager;
import com.juxiao.xchat.manager.external.dingtalk.bo.DingtalkTextMessageBO;
import com.juxiao.xchat.manager.external.dingtalk.conf.DingTalkConf;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * @class: LoggerManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/27
 */
@Slf4j
@Service
public class LogAspectManagerImpl implements LogAspectManager {
    private final Logger httpslow = LoggerFactory.getLogger("com.juxiao.xchat.httpslow");
    @Autowired
    private DingTalkConf dingTalkConf;
    @Autowired
    private DingtalkChatbotManager dingtalkChatbotManager;
    @Autowired
    private SystemConf systemConf;

    @Override
    public Object logAround(ProceedingJoinPoint point) {
        long time;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = HttpServletUtils.getRealIp(request);
        String uri = request.getRequestURI();
        String args = this.getArgs(request);

        Object result;
        long start = System.currentTimeMillis();
        try {
            result = point.proceed();
            time = System.currentTimeMillis() - start;
        } catch (WebServiceException e) {
            time = System.currentTimeMillis() - start;
            result = WebServiceMessage.failure(e);
            LoggerFactory.getLogger(point.getTarget().getClass()).warn("{} [ {} ]请求:>{} 异常信息:{}", ip, uri, args, this.getSimpleStackTrace(e));
        } catch (Throwable e) {
            time = System.currentTimeMillis() - start;
            Class<?> clazz = this.getMethodReturnType(point);
            if (clazz == WebServiceMessage.class) {
                result = WebServiceMessage.failure(WebServiceCode.SERVER_ERROR);
            } else {
                result = null;
            }
            log.error("{} [ {} ]请求:>{} 异常信息:", ip, uri, args, e);
            this.sendAlarm(uri, e.getMessage());
        }

        HttpServletResponse response = attributes.getResponse();
        response.setHeader("time", String.valueOf(time));
        this.loginfo(point.getTarget().getClass(), ip, uri, args, result, time);
        return result;
    }

    private void sendAlarm(String uri, String exmsg) {
        String content = "接口【" + uri + "】报错";
        if (!"prod".equalsIgnoreCase(systemConf.getEnv())) {
            return;
        }

        String text = "[ 服务预警 ] " + systemConf.getEnvName() + "服务" + content + ":" + exmsg + "，预警级别：高";
        DingtalkTextMessageBO textMessage = new DingtalkTextMessageBO(text, dingTalkConf.getProgrammer(), false);
        try {
            dingtalkChatbotManager.send(dingTalkConf.getDevelopChatbot(), textMessage);
        } catch (Exception e) {
            log.error("【发送预警失败】发送钉钉机器人信息失败:>接口【{}】报错, 异常:{}", uri, e.getMessage());
        }
    }

    private void loginfo(Class<?> clazz, String ip, String uri, String args, Object result, long time) {
        if (result instanceof WebServiceMessage) {
            LoggerFactory.getLogger(clazz).info("{} [ {} ]请求:>({}) 返回:>{} 耗时:>{}毫秒", ip, uri, args, ((WebServiceMessage) result).getCode(), time);
        } else if (result instanceof Map) {
            LoggerFactory.getLogger(clazz).info("{} [ {} ]请求:>({}) 返回:>{} 耗时:>{}毫秒", ip, uri, args, JSON.toJSON(result), time);
        } else {
            LoggerFactory.getLogger(clazz).info("{} [ {} ]请求:>({}) 耗时:>{}毫秒", ip, uri, args, time);
        }

        if (time > 200) {
            if (result instanceof WebServiceMessage) {
                httpslow.info("{} [ {} ]请求:>({}) 返回:>{} 耗时:>{}毫秒", ip, uri, args, ((WebServiceMessage) result).getCode(), time);
            } else if (result instanceof Map) {
                httpslow.info("{} [ {} ]请求:>({}) 返回:>{} 耗时:>{}毫秒", ip, uri, args, JSON.toJSON(result), time);
            } else {
                httpslow.info("{} [ {} ]请求:>({}) 耗时:>{}毫秒", ip, uri, args, time);
            }
        }
    }

    /**
     * 组装请求参数
     *
     * @param request
     * @return
     * @author: chenjunsheng
     * @date 2018年6月27日
     */
    private String getArgs(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
        StringBuilder args = new StringBuilder();
        for (Map.Entry<String, String[]> entry : entrySet) {
            args.append(entry.getKey()).append("=").append(request.getParameter(entry.getKey())).append("&");
        }

        return args.length() <= 0 ? args.toString() : args.substring(0, args.length() - 1);
    }

    private Class<?> getMethodReturnType(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        return signature.getReturnType();
    }

    private String getSimpleStackTrace(Throwable throwable) {
        StackTraceElement[] stackTraces = throwable.getStackTrace();
        StringBuilder stackTraceInfo = new StringBuilder();
        stackTraceInfo.append(throwable.getMessage());
        for (StackTraceElement element : stackTraces) {
            String tmp = element.toString();
            // 只获取指定包的下的异常堆栈跟踪信息
            if (tmp.startsWith("com.juxiao.xchat")) {
                stackTraceInfo.append("\r\n\tat ").append(tmp);
            } else {
                break;
            }
        }

        return stackTraceInfo.toString();
    }
}
