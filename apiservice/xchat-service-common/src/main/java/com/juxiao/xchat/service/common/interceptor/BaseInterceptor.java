package com.juxiao.xchat.service.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.juxiao.xchat.base.utils.AESUtils;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.manager.common.conf.AesConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseInterceptor {

    @Value("${common.system.env}")
    protected String env = "prod";
    @Autowired
    protected AesConf aesConf;

    protected void response(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        Boolean isAse = (Boolean) request.getAttribute("ase");
        if (isAse == null || !isAse) {
            HttpServletUtils.writeJson(response, object);
            return;
        }

        Map<String, String> result = new HashMap<>(1);
        result.put("ed", AESUtils.encrypt(JSON.toJSONString(object), aesConf.getKey(), aesConf.getIv()));
        HttpServletUtils.writeJson(response, result);
    }
}
