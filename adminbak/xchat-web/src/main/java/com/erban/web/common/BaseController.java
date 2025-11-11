package com.erban.web.common;


import com.erban.main.service.common.JedisLockService;
import com.xchat.common.constant.Constant;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected JedisService jedisService;
    @Autowired
    protected JedisLockService jedisLockService;

    protected byte[] readBody(HttpServletRequest request) throws IOException {
        if (request.getContentLength() > 0) {
            byte[] body = new byte[request.getContentLength()];
            IOUtils.readFully(request.getInputStream(), body);
            return body;
        }else{
            return null;
        }
    }

}
