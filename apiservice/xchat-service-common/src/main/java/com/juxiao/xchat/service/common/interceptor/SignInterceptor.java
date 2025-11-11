package com.juxiao.xchat.service.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.annotation.Client;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.AESUtils;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.utils.MD5Utils;
import com.juxiao.xchat.base.utils.Utils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.common.sysconf.AppSignKeyManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class SignInterceptor extends BaseInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    @Autowired
    private AppSignKeyManager signKeyManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

//        SignVerification verification = method.getAnnotation(SignVerification.class);
//        if (verification == null) {
//            return true;
//        }
//
//        String sn = this.getSn(request);
//        if (!"prod".equalsIgnoreCase(env) && "123456".equalsIgnoreCase(sn)) {
//            return true;
//        }
//
//        String appVersion = request.getParameter("appVersion");
//        String uri = request.getRequestURI();
//        if (StringUtils.isBlank(appVersion)) {
//            logger.warn("[ 签名验证拦截 ] 无法获取到版本信息，进行拦截，接口:>{}", uri);
//            WebServiceMessage message = WebServiceMessage.failure(WebServiceCode.SIGN_AUTHORITY);
//            Boolean isAse = (Boolean) request.getAttribute("ase");
//            String body = JSON.toJSONString(message);
//            if (isAse != null && isAse) {
//                JSONObject object = new JSONObject();
//                object.put("ed", AESUtils.encrypt(body, aesConf.getKey(), aesConf.getIv()));
//                body = object.toJSONString();
//            }
//            HttpServletUtils.writeJson(response, body);
//            return false;
//        }
//
//        String os = request.getParameter("os");
//        if (StringUtils.isBlank(os)) {
//            logger.warn("[ 签名验证拦截 ] 未知客户端操作系统，接口:>{}，os:>{}", uri, os);
//            WebServiceMessage message = WebServiceMessage.failure(WebServiceCode.SIGN_AUTHORITY);
//            this.response(request, response, message);
//            return false;
//        }
//
//        if ("wxapp".equalsIgnoreCase(os) && Utils.version2long(appVersion) == Utils.version2long("1.0.9")) {
//            return true;
//        }
//
//
//        if (StringUtils.isBlank(sn)) {
//            logger.warn("[ 签名验证拦截 ] 无签名字段，进行拦截，接口:>{}", uri);
//            WebServiceMessage message = WebServiceMessage.failure(WebServiceCode.SIGN_AUTHORITY);
//            this.response(request, response, message);
//            return false;
//        }
//
//        String t = this.getTimeStamps(request);
//        Map<String, String> param = HttpServletUtils.getRequestParameterStr(request);
//        param.put("t", t);
//        param.remove("sn");
//        Client client = verification.client();
//        String signKey = signKeyManager.getAppSignKey(os, appVersion);
//        String preSign = client.sign(uri, param, signKey);
//        String sign = MD5Utils.encode(preSign);
//        if (sign.length() > 7) {
//            sign = sign.substring(0, 7);
//        }
//
//        if (!sn.equalsIgnoreCase(sign)) {
//            logger.warn("[ 签名验证拦截 ] 验证错误，接口:>{},sn:>{},签名字符:>{},签名:>{}", uri, sn, preSign, sign);
//            WebServiceMessage message = WebServiceMessage.failure(WebServiceCode.SIGN_AUTHORITY);
//            this.response(request, response, message);
//            return false;
//        }

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
}
