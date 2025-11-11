package com.xchat.oauth2.web.controller;

import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.common.exceptions.OAuth2Exception;
import com.xchat.oauth2.service.common.exceptions.ServiceErrorException;
import com.xchat.oauth2.service.common.status.OAuthStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author liuguofu
 * on 11/3/14.
 */
public class BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Object exceptionHandler(Exception e, HttpServletResponse response) throws Exception {
        if (!(e instanceof OAuth2Exception)) {
            LOG.error("account system Exception,cause by " + e.getMessage(), e);
            e = new ServiceErrorException("unknown error." + e.getMessage() + e);
        }
        LOG.warn("server error. msg:{}", e);
        response.setStatus(((OAuth2Exception) e).getHttpErrorCode());
        return e;
    }

    public class ReturnData {
        private int code;
        private String message;
        private Object data;

        public ReturnData(BusiStatus status) {
            this(status, null);
        }

        public ReturnData(BusiStatus status, Object data) {
            this.code = status.value();
            this.message = status.getReasonPhrase();
            this.data = data;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }
}
