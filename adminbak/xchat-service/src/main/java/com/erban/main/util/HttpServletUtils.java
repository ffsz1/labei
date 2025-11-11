package com.erban.main.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Servlet相关工具类
 *
 * @class: HttpServletUtils.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
public class HttpServletUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpServletUtils.class);

    /**
     * 获取IP地址
     *
     * @param request
     * @return
     * @author: chenjunsheng
     * @date 2018年4月27日
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (StringUtils.isNotEmpty(ip) && !ip.equals("127.0.0.1") && !ip.contains(":") && !ip.startsWith("192.168.")) {
            return ip;
        }

        ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip) && ip.contains(",")) {
            ip = ip.split(",")[0];
            if (ip != null && ip.contains(":")) {
                logger.warn("[ 获取IP地址 ] x-forwarded-for获取到:>{}", ip);
            }
        }

        if (ip == null || ip.length() == 0 || ip.contains(":") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            if (ip != null && ip.contains(":")) {
                logger.warn("[ 获取IP地址 ] Proxy-Client-IP获取到:>{}", ip);
            }
        }

        if (ip == null || ip.length() == 0 || ip.contains(":") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            if (ip != null && ip.contains(":")) {
                logger.warn("[ 获取IP地址 ] WL-Proxy-Client-IP获取到:>{}", ip);
            }
        }

        if (ip == null || ip.length() == 0 || ip.contains(":") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            if (ip != null && ip.contains(":")) {
                logger.warn("[ 获取IP地址 ] HTTP_CLIENT_IP获取到:>{}", ip);
            }
        }

        if (ip == null || ip.length() == 0 || ip.contains(":") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            if (ip != null && ip.contains(":")) {
                logger.warn("[ 获取IP地址 ] HTTP_X_FORWARDED_FOR获取到:>{}", ip);
            }
        }

        if (ip == null || ip.length() == 0 || ip.contains(":") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            if (ip != null && ip.contains(":")) {
                logger.warn("[ 获取IP地址 ] X-Real-IP获取到:>{}", ip);
            }
        }
        if (ip == null) {
            return request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 读取请求信息
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String readString(HttpServletRequest request) throws IOException {
        StringBuilder buffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            line = reader.readLine();
            while (line != null) {
                buffer.append(line);
                line = reader.readLine();
            }
            return buffer.toString();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 获取HttpServletRequest 请求参数
     *
     * @param request
     * @return
     */
    public static String getRequestParameter(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Iterator<String> iterator = parameterMap.keySet().iterator();
        StringBuilder builder = new StringBuilder();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = request.getParameter(key);
            //处理你得到的参数名与值
            builder.append(key).append("=").append(value).append("&");
        }

        if (builder.length() == 0) {
            return "";
        }
        return builder.substring(0, builder.length() - 1);
    }

    /**
     * 获取HttpServletRequest 请求参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> getRequestParameterStr(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Iterator<String> iterator = parameterMap.keySet().iterator();
        Map<String, String> param = new HashMap<>();
        String key;
        String value;
        while (iterator.hasNext()) {
            key = iterator.next();
            value = request.getParameter(key);
            param.put(key, value);
        }

        return param;
    }
    
    public static String getBrowserType(HttpServletRequest request) {
    	return request.getHeader("USER-AGENT");
    } 
}
