package com.juxiao.xchat.service.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.AESUtils;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.AesConf;
import com.juxiao.xchat.manager.common.user.AccountManager;
import com.juxiao.xchat.service.common.sysconf.AppVersionService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginInterceptor extends BaseInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private AppVersionService appVersionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String requestUri = request.getRequestURI();
        String requsetStr = "";

        // 不打印获取数据的请求日志
        if (!requestUri.contains("get") && !requestUri.contains("list")) {
            String parameter = HttpServletUtils.getRequestParameter(request);
            requsetStr = requestUri + "?" + parameter;
        }

        String version = request.getParameter("appVersion");
        WebServiceCode code = appVersionService.checkVersion(version);
        if (method.getAnnotation(Authorization.class) == null || "local".equalsIgnoreCase(env)) {//不需要登录校验
            if (code != WebServiceCode.SUCCESS && redisManager.get("erban_check_version") != null) {
                String uid = request.getParameter("uid");
                if (StringUtils.isNumeric(uid)) {
                    accountManager.blockAccount(Long.valueOf(uid));
                }

                this.response(request, response, WebServiceMessage.failure(code));
                logger.info("[ 拦截器 ]拦截请求:>{}, 检查版本结果:>{}", requsetStr, code);
                return false;
            }
            return true;
        }

        if (StringUtils.isNotBlank(version) && code != WebServiceCode.SUCCESS) {
            logger.info("[ 拦截器 ]拦截请求:>{}, 检查版本结果:>{}", requsetStr, code);
            this.response(request, response, WebServiceMessage.failure(WebServiceCode.NO_AUTHORITY));
            return false;
        }

        String ticket = request.getParameter("ticket");
        if (StringUtils.isEmpty(ticket)) {
            logger.info("[ 拦截器 ]拦截请求:>{},上传 ticket 为空。", requsetStr);
            this.response(request, response, WebServiceMessage.failure(WebServiceCode.NO_AUTHORITY));
            return false;
        }

        String ticketCache = redisManager.hget(RedisKey.uid_ticket.getKey(), request.getParameter("uid"));
        if (StringUtils.isEmpty(ticketCache)) {
            logger.info("[ 拦截器 ]拦截请求:>{},缓存中的 ticket 为空，uid:>{}", requsetStr, request.getParameter("uid"));
            this.response(request, response, WebServiceMessage.failure(WebServiceCode.NO_AUTHORITY));
            return false;
        }

        if (!ticketCache.equals(ticket)) {
            logger.info("[ 拦截器 ]拦截请求:>{},ticket验证不通过，ticket:>{},缓存ticket:>{}", requsetStr, ticket, ticketCache);
            this.response(request, response, WebServiceMessage.failure(WebServiceCode.NO_AUTHORITY));
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
