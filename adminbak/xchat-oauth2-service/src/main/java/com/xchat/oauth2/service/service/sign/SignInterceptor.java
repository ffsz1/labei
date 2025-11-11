package com.xchat.oauth2.service.service.sign;

import com.google.gson.Gson;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.wx.MD5Utils;
import com.xchat.oauth2.service.core.util.HttpServletUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class SignInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    private Gson gson = new Gson();
    @Autowired
    private AppSignService signService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //不需要登录校验
        if (method.getAnnotation(SignVerification.class) == null) {
            return true;
        }

        String sn = this.getSn(request);
        if (!"prod".equalsIgnoreCase(GlobalConfig.sysEnv) && "123456".equalsIgnoreCase(sn)) {
            return true;
        }


        String os = request.getParameter("os");
        String appVersion = request.getParameter("appVersion");
        String uri = request.getRequestURI();
        if (StringUtils.isBlank(os) || StringUtils.isBlank(appVersion)) {
            // 拿不到appCode，再次去app_version进行校验
            logger.warn("[ 签名验证拦截 ] 无法获取到版本信息，进行拦截，接口:>{}", uri);
            BusiResult result = new BusiResult(BusiStatus.SIGN_AUTHORITY);
            this.print(response, gson.toJson(result));
            return false;
        }

        if (StringUtils.isBlank(sn)) {
            logger.warn("[ 签名验证拦截 ] 无签名字段，进行拦截，接口:>{}", uri);
            BusiResult result = new BusiResult(BusiStatus.SIGN_AUTHORITY);
            this.print(response, gson.toJson(result));
            return false;
        }

        String t = this.getTimeStamps(request);
        Map<String, String> param = HttpServletUtils.getRequestParameterStr(request);
        param.put("t", t);
        param.remove("sn");

        String preSign = signService.getPreSign(os, appVersion, uri, param);
        String sign = MD5Utils.encode(preSign);
        if (sign.length() > 7) {
            sign = sign.substring(0, 7);
        }

        if (!sn.equalsIgnoreCase(sign)) {
            logger.warn("[ 签名验证拦截 ] 验证错误，接口:>{},sn:>{},签名字符:>{},签名:>{}", uri, sn, preSign, sign);
            BusiResult result = new BusiResult(BusiStatus.SIGN_AUTHORITY);
            this.print(response, gson.toJson(result));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    private String getSn(HttpServletRequest request) {
        String sn = request.getHeader("sn");
        if (StringUtils.isNotBlank(sn)) {
            return sn;
        }
        return request.getParameter("sn");
    }

    private String getTimeStamps(HttpServletRequest request) {
        String t = request.getHeader("t");
        if (StringUtils.isNotBlank(t)) {
            return t;
        }
        return request.getParameter("t");
    }

    /**
     * 向页面返回消息
     *
     * @param response
     * @param body
     * @throws IOException
     */
    private void print(HttpServletResponse response, String body) {
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            writer = response.getWriter();
            writer.write(body);
            writer.flush();
        } catch (IOException e) {
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
