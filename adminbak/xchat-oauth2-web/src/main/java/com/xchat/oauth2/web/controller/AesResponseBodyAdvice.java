package com.xchat.oauth2.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.AESConfig;
import com.xchat.common.utils.AESUtils;
import com.xchat.oauth2.web.common.aes.AesConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice(basePackages = "com.xchat.oauth2")
public class AesResponseBodyAdvice implements ResponseBodyAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest serverHttpRequest, ServerHttpResponse response) {
        if (!(serverHttpRequest instanceof ServletServerHttpRequest)) {
            return body;
        }
        // http 统一返回 200, 如果有异常就提示消息
        response.setStatusCode(HttpStatus.OK);
        if (body instanceof Exception) {
            body = new BusiResult<>(BusiStatus.SERVERERROR, ((Exception) body).getMessage(), body);
        }

        ServletServerHttpRequest servletHttpRequest = (ServletServerHttpRequest) serverHttpRequest;
        if (servletHttpRequest.getServletRequest().getAttribute(AESConfig.DECRYPT_NAME) == Boolean.FALSE) {
            return body;
        }

        String content;
        try {
            content = AESUtils.encrypt(JSON.toJSONString(body), AesConf.AES_KEY, AesConf.AES_IV);
        } catch (Exception e) {
            logger.error("[ 返回信息 ]加密异常，返回信息：", JSON.toJSONString(body), e);
            content = "";
        }
        JSONObject ret = new JSONObject();
        ret.put(AESConfig.PARAM_NAME, content);
        return ret;
    }
}
