package com.juxiao.xchat.service.common.exception;

import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chris
 * @Title:
 * @date 2019-05-13
 * @time 17:31
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice {

    @ResponseBody
    @ExceptionHandler({HttpMessageNotReadableException.class, TypeMismatchException.class, MissingServletRequestParameterException.class})
    public WebServiceMessage badRequest400Exception(HttpServletRequest request, Exception e) {
        String ip = HttpServletUtils.getRealIp(request);
        String uri = request.getRequestURI();
        String parameter = HttpServletUtils.getRequestParameter(request);
        log.error("{}[ 响应400错误 ] 接口:>{}, 入参:>{}, 返回:>{}, 原因:>{}:{}", ip, uri, parameter, WebServiceCode.PARAM_EXCEPTION, e.getClass(), e.getMessage());
        return WebServiceMessage.failure(WebServiceCode.PARAM_EXCEPTION);
    }

    @ResponseBody
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public WebServiceMessage methodNotAllowed405Exception(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        String ip = HttpServletUtils.getRealIp(request);
        String uri = request.getRequestURI();
        String parameter = HttpServletUtils.getRequestParameter(request);
        log.error("{}[ 响应405错误 ] 接口:>{}, 入参:>{}, 返回:>{}, 支持方法:>{}, 请求方法:>{}, 原因:>{}", ip, uri, parameter, WebServiceCode.METHOD_NOT_ALLOWED, e.getSupportedHttpMethods(), e.getMethod(), e.getMessage());
        return WebServiceMessage.failure(WebServiceCode.METHOD_NOT_ALLOWED);
    }

    @ResponseBody
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public WebServiceMessage notAcceptable406Exception(HttpServletRequest request, HttpMediaTypeNotAcceptableException e) {
        String ip = HttpServletUtils.getRealIp(request);
        String uri = request.getRequestURI();
        String parameter = HttpServletUtils.getRequestParameter(request);
        log.error("{}[ 响应406错误 ] 接口:>{}, 入参:>{}, supportedMediaTypes:>{}, 原因:>{}", ip, uri, parameter, WebServiceCode.NOT_ACCEPTABLE, e.getSupportedMediaTypes(), e.getMessage());
        return WebServiceMessage.failure(WebServiceCode.NOT_ACCEPTABLE);
    }


    @ResponseBody
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public WebServiceMessage internalServerError500(HttpServletRequest request, Exception e) {
        String ip = HttpServletUtils.getRealIp(request);
        String uri = request.getRequestURI();
        String parameter = HttpServletUtils.getRequestParameter(request);
        log.error("{}[ 服务器内部错误 ] 接口:>{}, 入参:>{}, 返回:>{}, 原因:>{}", ip, uri, parameter, WebServiceCode.SERVER_ERROR, e.getMessage());
        return WebServiceMessage.failure(WebServiceCode.SERVER_ERROR);
    }
}
