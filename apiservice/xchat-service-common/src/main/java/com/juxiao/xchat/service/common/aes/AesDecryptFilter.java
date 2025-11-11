package com.juxiao.xchat.service.common.aes;

import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AesDecryptFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String data = request.getParameter("ed");
        if (StringUtils.isBlank(data)) {
            request.setAttribute("ase", false);
            chain.doFilter(new StringEscapeServletRequestWrapper(request), response);
            return;
        }

        try {
            chain.doFilter(new AesDescryptServletRequestWrapper(request), response);
        } catch (Exception e) {
            String ip = HttpServletUtils.getRealIp(request);
            logger.error("{} [ {} ]请求:>{} 异常信息:", ip, request.getRequestURI(), data, e);
            HttpServletUtils.writeJson(response, WebServiceMessage.failure(WebServiceCode.SERVER_ERROR));
        }
    }


}
