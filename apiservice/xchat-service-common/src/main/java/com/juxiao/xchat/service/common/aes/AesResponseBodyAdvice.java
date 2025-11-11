package com.juxiao.xchat.service.common.aes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.utils.AESUtils;
import com.juxiao.xchat.manager.common.conf.AesConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice(basePackages = "com.juxiao.xchat")
public class AesResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AesConf aesConf;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest serverHttpRequest, ServerHttpResponse response) {
        if (!(serverHttpRequest instanceof ServletServerHttpRequest)) {
            return body;
        }

        ServletServerHttpRequest servletHttpRequest = (ServletServerHttpRequest) serverHttpRequest;
        if (servletHttpRequest.getServletRequest().getAttribute("ase") == Boolean.FALSE) {
            return body;
        }

        String content;
        try {
            content = AESUtils.encrypt(JSON.toJSONString(body), aesConf.getKey(), aesConf.getIv());
        } catch (Exception e) {
            logger.error("[ 返回信息 ]加密异常，返回信息：", JSON.toJSONString(body), e);
            content = "";
        }
        JSONObject ret = new JSONObject();
        ret.put("ed", content);
        return ret;
    }
}
