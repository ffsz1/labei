package com.erban.main.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class IpUtil {

    private static final Logger logger = LoggerFactory.getLogger(IpUtil.class);

    /**
     * 获取IP地址
     *
     * @author: chenjunsheng
     * @date 2018年4月27日
     * @param request
     * @return
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(ip) && !ip.equals("127.0.0.1") && !ip.contains(":") && !ip.startsWith("192.168.")) {
            return ip;
        }

        ip = request.getHeader("x-forwarded-for");
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip) && ip.contains(",")) {
            ip = ip.split(",")[0];
            if (ip != null && ip.contains(":")) {
                logger.error("[ 获取IP地址 ] x-forwarded-for获取到:>{}", ip);
            }
        }

        if (ip == null || ip.length() == 0 || ip.contains(":") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            if (ip != null && ip.contains(":")) {
                logger.error("[ 获取IP地址 ] Proxy-Client-IP获取到:>{}", ip);
            }
        }

        if (ip == null || ip.length() == 0 || ip.contains(":") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            if (ip != null && ip.contains(":")) {
                logger.error("[ 获取IP地址 ] WL-Proxy-Client-IP获取到:>{}", ip);
            }
        }

        if (ip == null || ip.length() == 0 || ip.contains(":") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            if (ip != null && ip.contains(":")) {
                logger.error("[ 获取IP地址 ] HTTP_CLIENT_IP获取到:>{}", ip);
            }
        }

        if (ip == null || ip.length() == 0 || ip.contains(":") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            if (ip != null && ip.contains(":")) {
                logger.error("[ 获取IP地址 ] HTTP_X_FORWARDED_FOR获取到:>{}", ip);
            }
        }

        if (ip == null || ip.length() == 0 || ip.contains(":") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            if (ip != null && ip.contains(":")) {
                logger.error("[ 获取IP地址 ] X-Real-IP获取到:>{}", ip);
            }
        }
        return ip;
    }

}
