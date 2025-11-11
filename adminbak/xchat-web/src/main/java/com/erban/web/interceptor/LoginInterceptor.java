package com.erban.web.interceptor;

import com.erban.main.service.AppVersionService;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.PropertyUtil;
import com.xchat.oauth2.service.core.util.StringUtils;
import com.xchat.oauth2.service.service.JedisService;
import com.xchat.oauth2.service.service.account.AccountBlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by liuguofu on 2017/6/3.
 */
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
    @Autowired
    JedisService jedisService;
    @Autowired
    private AccountBlockService accountBlockService;
    @Autowired
    private AppVersionService appVersionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String requestUri=request.getRequestURI();
        String requsetStr="";
        // 不打印获取数据的请求日志
        if(!requestUri.contains("get") && !requestUri.contains("list")) {
            Map paramsMap = request.getParameterMap();
            Iterator it = paramsMap.keySet().iterator();
            String params = "";
            while (it.hasNext()) {
                String paramName = (String) it.next();
                String paramValue = request.getParameter(paramName);
                //处理你得到的参数名与值
                params = params + paramName + "=" + paramValue + "&";
            }
            requsetStr="request uri=" + requestUri.toString() + "?" + params;
            logger.info(requsetStr);
        }

        if (method.getAnnotation(Authorization.class) == null || "test".equalsIgnoreCase(PropertyUtil.
                getProperty("SYS_ENV", "prod"))) {//不需要登录校验
            String version = request.getParameter("appVersion");
            BusiResult busiResult = appVersionService.checkVersion(version);
            if (!BlankUtil.isBlank(version) && busiResult.getCode() != 200 && jedisService.get("erban_check_version") != null) {
                logger.info("requsetUri={}, checkVersion code:{}, message:{}", requsetStr, busiResult.getCode()
                        , busiResult.getMessage());
                accountBlockService.doAccountBlock(Long.valueOf(request.getParameter("uid")));
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter printWriter=response.getWriter();
                printWriter.write("{\n" +
                        "\"code\": "+ busiResult.getCode()+",\n" +
                        "\"message\": \""+ busiResult.getMessage() +"\"\n" +
                        "}");
                printWriter.flush();
                printWriter.close();
                return false;
            }
            return true;
        }else{
            String uidstr=request.getParameter("uid");
            Long uid=Long.valueOf(uidstr);
            String ticket=request.getParameter("ticket");
            String version = request.getParameter("appVersion");

            BusiResult busiResult = appVersionService.checkVersion(version);
            if (!BlankUtil.isBlank(version) && busiResult.getCode() != 200) {
                logger.info("requsetUri={}, checkVersion code:{}, message:{}", requsetStr, busiResult.getCode()
                        , busiResult.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter printWriter=response.getWriter();
                printWriter.write("{\n" +
                        "\"code\": "+ busiResult.getCode()+",\n" +
                        "\"message\": \""+ busiResult.getMessage() +"\"\n" +
                        "}");
                printWriter.flush();
                printWriter.close();
                return false;
            }
            if(StringUtils.isEmpty(ticket)){
                logger.info("requsetUri="+requsetStr+"|||||||ticket is null.........");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter printWriter=response.getWriter();
                printWriter.write("{\n" +
                        "\"code\": 401,\n" +
                        "\"message\": \"need login!\"\n" +
                        "}");
                printWriter.flush();
                printWriter.close();
                return false;

            }
            String ticketCache=getTicketCacheByUid(uid);
            if(StringUtils.isEmpty(ticketCache)){
                logger.info("requsetUri="+requsetStr+"|||||||ticketCache is null.........");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter printWriter=response.getWriter();
                printWriter.write("{\n" +
                        "\"code\": 401,\n" +
                        "\"message\": \"need login!\"\n" +
                        "}");
                printWriter.flush();
                printWriter.close();
                return false;
            }
            if(!ticketCache.equals(ticket)){
                logger.info("requsetUri="+requsetStr+"|||||||illegal ticket.........");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter printWriter=response.getWriter();
                printWriter.write("{\n" +
                        "\"code\": 401,\n" +
                        "\"message\": \"need login!\"\n" +
                        "}");
                printWriter.flush();
                printWriter.close();
                return false;
            }
        }

        return true;
    }

    private String getTicketCacheByUid(Long uid){
        String ticketStr=jedisService.hget(RedisKey.uid_ticket.getKey(),uid.toString());
        if(StringUtils.isEmpty(ticketStr)){
            return null;
        }
        return ticketStr;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
